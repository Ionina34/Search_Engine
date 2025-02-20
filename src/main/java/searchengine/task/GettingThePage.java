package searchengine.task;

import lombok.Getter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import searchengine.config.Init.RequestDataInitializer;
import searchengine.config.RequestData;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.task.entity.PageDataContainer;

import java.io.IOException;

/**
 * Класс для получения страницы по URL и извлечения ссылок из нее.
 */
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

    /**
     * Получает объект Page {@link Page} по заданному URL
     *
     * @return объект Page {@link Page}, сожрежащий информацию о странице
     * @throws IOException если произошла ошибка при подключении к URL
     */
    public Page getPageByUrl() throws IOException {
        Connection.Response response = connect();
        Document doc = response.parse();
        return creatingAnObjectPage(url, response, doc);
    }

    /**
     * Получает объект Data {@link PageDataContainer}, содеражщий страницу и ссылки на ней.
     *
     * @return Data {@link PageDataContainer}, содержащий страницу и элементы
     * @throws IOException если произошла ошибка при подлкючении к URL
     */
    public PageDataContainer getPageAndLinks() throws IOException{
        PageDataContainer data = new PageDataContainer();
        Connection.Response response = connect();
        Document doc = response.parse();
        Page page = creatingAnObjectPage(url, response, doc);
        data.setPage(page);
        data.setElements(doc.select(CSS_QUERY));
        return data;
    }

    /**
     * Устанавливает соединение с заданным URL.
     *
     * @return ответ соединения
     * @throws IOException если произошла ошибка при подлкючении к URL
     */
    private Connection.Response connect() throws IOException {
        Connection connection = Jsoup.connect(url).timeout(2000);
        if(data!= null){
            connection.userAgent(data.getUser())
                    .referrer(data.getReferrer());
        }

        return connection.execute();
    }

    /**
     * Создает объект Page {@link Page}, на основе URL, ответа и документа
     *
     * @param url URL странице
     * @param response ответ соедения
     * @param doc документ, полученный с URL
     * @return объект Page {@link Page}
     */
    private Page creatingAnObjectPage(String url, Connection.Response response, Document doc){
        Page page = new Page();
        page.setSite(site);
        page.setPath(removeDomainFromThePath(url));
        page.setCode(response.statusCode());
        page.setContent(doc.toString());
        return page;
    }

    /**
     * Удаляет домен из обсолютного URL и возвращает относительный путь.
     *
     * @param absUrl абсолютный URL
     * @return относительный путь
     */
    public String removeDomainFromThePath(String absUrl) {
        return absUrl.replace(site.getUrl(), "");
    }
}
