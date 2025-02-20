package searchengine.task;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

/**
 * Класс представляет собой задачу для индексирования страниц,
 * которая выполняется в виде рекурсивной задачи. Он
 * использует механизм Fork|Join для параллельного индексирования страниц,
 * извлекая ссылки из заданного URL.
 */
@Getter
@Slf4j
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

    /**
     * Метод выполняет основную логику индексирования.
     * Он извлекает страницу и ссылки, создает подзадачи для каждой найденной
     * ссылки и возвращает списко страниц.
     *
     * @return список страниц, найденных на заданном URL.
     */
    @Override
    protected List<Page> compute() {
        List<IndexingTask> subTasks = new LinkedList<>();
        List<Page> pages = new LinkedList<>();
        GettingThePage gettingThePage = new GettingThePage(url, site);

        try {
            Thread.sleep(500);
            PageDataContainer data = gettingThePage.getPageAndLinks();
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
        } catch (HttpStatusException e) {
            updateInfoAboutSiteAfterError("Сайт ответил статусом 5**");
            Page page = createErrorPage(e.getStatusCode(), gettingThePage.removeDomainFromThePath(url));
            return List.of(page);
        } catch (IOException | InterruptedException e) {
            updateInfoAboutSiteAfterError("Произошла непредвиденная ошибка");
            log.error("Error", e);
            return Collections.emptyList();
        }

        return combiningTheResults(subTasks, pages);
    }

    /**
     * Очищает списко проверенных URL
     */
    public static void cleanVerifiedList() {
        VERIFIED_LIST.clear();
    }

    /**
     * Устанавливает флаг, указывающий, что индексирование запущено.
     */
    public static synchronized void indexingIsRunning() {
        indexingInProgress.set(true);
    }

    /**
     * Устанавливает флаг, указывающий, что индексирование остановлено.
     */
    public static synchronized void indexingStopped() {
        indexingInProgress.set(false);
    }

    /**
     * Проверяет, выполняется ли индексирование в данный момент.
     *
     * @return true, если индексирование выполняется; иначе false
     */
    public static synchronized boolean isIndexing() {
        return indexingInProgress.get();
    }

    /**
     * Объеденяет результаты подзадач и возвращает общий список страниц.
     *
     * @param subTasks список подзадач
     * @param pages список страниц, найденных в текущей задаче
     * @return объединенный список
     */
    private List<Page> combiningTheResults(List<IndexingTask> subTasks, List<Page> pages) {
        for (IndexingTask task : subTasks) {
            pages.addAll(task.join());
        }
        return pages;
    }

    /**
     * Создает страницу с ошибкой, если индексирование не удалось.
     *
     * @param code код ошибки
     * @param url URL страницы, на которой произошла ошибка
     * @return объект Page {@link Page} с информацией об ошибке
     */
    private Page createErrorPage(int code, String url){
        Page page = new Page();
        page.setCode(code);
        page.setPath(url);
        page.setSite(site);
        page.setContent("");
        return page;
    }

    /**
     * Обновляет информацию о сайт после возникновенияя ошибки.
     *
     * @param error сообщение об ошибке
     */
    private void updateInfoAboutSiteAfterError(String error){
        site.setStatus(StatusIndexing.FAILED);
        site.setLastError(error);
    }
}
