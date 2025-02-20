package searchengine.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import searchengine.model.Page;
import searchengine.services.IndexingPageService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class IndexingPageTaskTest {
    private Page[] pages;
    private IndexingPageService indexingPageService;

    @BeforeEach
    public void setUp() {
        pages = new Page[]{
                new Page("http://test.com/page1", "Content of page 1"),
                new Page("http://test.com/page2", "Content of page 2")
        };
        indexingPageService = Mockito.mock(IndexingPageService.class);
    }

    @Test
    public void testRun(){
        IndexingPageTask task = new IndexingPageTask(pages, indexingPageService);

        assertDoesNotThrow(task::run);
    }
}
