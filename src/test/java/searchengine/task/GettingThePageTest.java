package searchengine.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.model.StatusIndexing;
import searchengine.task.entity.PageDataContainer;

import java.io.IOException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

public class GettingThePageTest {
    private Site site;
    private String testUrl="";
    private GettingThePage gettingThePage;

    @BeforeEach
    public void setUp(){
        testUrl="https://ru.wikipedia.org/wiki/Заглавная_страница";
        site=new Site(1, StatusIndexing.INDEXING,new Timestamp(System.currentTimeMillis()), null,"https://ru.wikipedia.org/", "Test Site");
        gettingThePage = new GettingThePage(testUrl, site);
    }

    @Test
    @DisplayName("Getting page by url")
    public void testGettingPageByUrl_ReturnsPage() throws IOException {
        Page page= gettingThePage.getPageByUrl();

        assertEquals(200,page.getCode());
        assertNotNull(page.getContent());
        assertEquals("wiki/Заглавная_страница", page.getPath());
    }

    @Test
    @DisplayName("Geting page and links")
    public void testGetPageAndLinks_ReturnsData() throws IOException{
        PageDataContainer data = gettingThePage.getPageAndLinks();

        assertNotNull(data);
        assertNotNull(data.getPage());
        assertNotNull(data.getElements());

        assertFalse(data.getElements().isEmpty());
    }

    @Test
    @DisplayName("Remove domain from the path")
    public void testRemoveDomainFromThePath_ReturnsLink(){
        String result= gettingThePage.removeDomainFromThePath(testUrl);

        assertEquals("wiki/Заглавная_страница", result);
    }
}
