package searchengine.task;

import lombok.Getter;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchengine.config.RequestData;
import searchengine.config.Init.RequestDataInitializer;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.model.StatusIndexing;
import searchengine.task.entity.*;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class IndexingTask extends RecursiveTask<List<Page>> {
    private final String url;
    private final Site site;

    private static final RequestData data = RequestDataInitializer.getData();
    private final String CSS_QUERY = "a";
    private final String ATTRIBUTE_KEY = "href";

    private static final CopyOnWriteArraySet<String> VERIFIED_LIST = new CopyOnWriteArraySet<>();
    private static AtomicBoolean indexingInProgress = new AtomicBoolean(false);

    public IndexingTask(String url, Site site) {
        this.url = url;
        this.site = site;
    }

    @Override
    protected List<Page> compute() {
        List<IndexingTask> subTasks = new LinkedList<>();
        List<Page> pages = new LinkedList<>();
        GettingThePage gettingThePage = new GettingThePage(url, site);

        try {
            Thread.sleep(500);
            Data data = gettingThePage.getPageAndLinks();
            pages.add(data.getPage());
            Elements elements = data.getElements();
            for (Element element : elements) {
                String absUrl = element.absUrl(ATTRIBUTE_KEY);
                if (!absUrl.isEmpty() && absUrl.startsWith(url) &&
                        !absUrl.equals(url) &&
                        !VERIFIED_LIST.contains(absUrl) &&
                        !absUrl.contains("#") && indexingInProgress.get() &&
                        !site.getStatus().equals(StatusIndexing.FAILED)) {
                    VERIFIED_LIST.add(absUrl);
                    IndexingTask task = new IndexingTask(absUrl, site);
                    task.fork();
                    subTasks.add(task);
                }
            }
        }catch (HttpStatusException e){
            Page page = new Page();
            page.setCode(e.getStatusCode());
            page.setPath(gettingThePage.removeDomainFromThePath(url));
            page.setSite(site);
            page.setContent("");
            site.setStatus(StatusIndexing.FAILED);
            site.setLastError("Сайт ответил статусом 5**");
            return List.of(page);
        }
        catch (IOException | InterruptedException e) {
             site.setStatus(StatusIndexing.FAILED);
            //todo вместо этих ошибок где нибудь их интерпритировать на брлее понятный язык
            site.setLastError("Произошла непредвиденная ошибка");
            e.printStackTrace();
            return Collections.emptyList();
        }

        return combiningTheResults(subTasks, pages);
    }

    public static void cleanVerifiedList() {
        VERIFIED_LIST.clear();
    }

    public static void indexingIsRunning() {
        indexingInProgress.set(true);
    }

    public static void indexingStopped() {
        indexingInProgress.set(false);
    }

    public static boolean isIndexing() {
        return indexingInProgress.get();
    }

    private List<Page> combiningTheResults(List<IndexingTask> subTasks, List<Page> pages) {
        for (IndexingTask task : subTasks) {
            pages.addAll(task.join());
        }
        return pages;
    }

}
