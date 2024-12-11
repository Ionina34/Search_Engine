package searchengine.task;

import lombok.Getter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import searchengine.config.Init.RequestDataInitializer;
import searchengine.config.RequestData;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.task.entity.Data;

import java.io.IOException;

@Getter
public class GettingThePage {
    private final String url;
    private final Site site;

    private final String CSS_QUERY = "a";
    private static final RequestData data = RequestDataInitializer.getData();

    public GettingThePage(String url, Site site) {
        this.url = url;
        this.site = site;
    }

    public Page getPageByUrl() throws IOException {
        Connection.Response response = connect();
        Document doc = response.parse();
        return creatingAnObjectPage(url, response, doc);
    }

    public Data getPageAndLinks() throws IOException{
        Data data = new Data();
        Connection.Response response = connect();
        Document doc = response.parse();
        Page page = creatingAnObjectPage(url, response, doc);
        data.setPage(page);
        data.setElements(doc.select(CSS_QUERY));
        return data;
    }

    private Connection.Response connect() throws IOException {
        return Jsoup.connect(url)
                .timeout(20000)
                .userAgent(data.getUser())
                .referrer(data.getReferrer())
                .execute();
    }

    private Page creatingAnObjectPage(String url, Connection.Response response, Document doc){
        Page page = new Page();
        page.setSite(site);
        page.setPath(removeDomainFromThePath(url));
        page.setCode(response.statusCode());
        page.setContent(doc.toString());
        return page;
    }

    public String removeDomainFromThePath(String absUrl) {
        return absUrl.replace(site.getUrl(), "");
    }
}
