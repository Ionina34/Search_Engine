package searchengine.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.model.StatusIndexing;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IndexingTaskTest {
    private IndexingTask indexingTask;
    private Site testSite;

    @BeforeEach
    public void setUp(){
        testSite = new Site();
        testSite.setUrl("http://example.com");
        testSite.setStatus(StatusIndexing.INDEXING);
        testSite.setLastError("");

        String testUrl="http://example.com";
        indexingTask=new IndexingTask(testUrl,testSite);
    }

    @Test
    public void testCompute_NotError_ReturnsListPage(){
        List<Page> pages= indexingTask.fork().join();

        assertNotNull(pages);
        assertFalse(pages.isEmpty());

        for(Page page : pages){
            assertNotNull(page.getPath());
            assertNotNull(page.getContent());
        }
    }

    @Test
    public void testCompute_WithError_ReturnsPage(){
        testSite.setStatus(StatusIndexing.FAILED);
        List<Page> pages= indexingTask.fork().join();

        assertEquals(1, pages.size());
        assertEquals(StatusIndexing.FAILED,testSite.getStatus());
    }
}
