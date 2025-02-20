package searchengine.services.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import searchengine.config.SitesList;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.dto.indexing.IndexingResponseWithError;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.task.IndexingTask;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IndexingSiteServiceImplTest {
    private SiteRepository siteRepository;
    private PageRepository pageRepository;
    private LemmaRepository lemmaRepository;
    private IndexRepository indexRepository;
    private SitesList sitesList;
    private IndexingSiteServiceImpl indexingSiteService;

    @BeforeEach
    public void setUp() {
        siteRepository = Mockito.mock(SiteRepository.class);
        pageRepository = Mockito.mock(PageRepository.class);
        lemmaRepository = Mockito.mock(LemmaRepository.class);
        indexRepository = Mockito.mock(IndexRepository.class);
        sitesList = Mockito.mock(SitesList.class);
        indexingSiteService = new IndexingSiteServiceImpl(siteRepository,
                pageRepository, indexRepository, lemmaRepository, sitesList);
    }

    @Test
    @DisplayName("Indexing is already running")
    public void testStartIndexing_IndexingAlreadyRunning_ReturnsErrorResponse() {
        IndexingTask.indexingIsRunning();
        IndexingResponse response = indexingSiteService.startIndexing();

        assertFalse(response.isResult());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatus());
        assertEquals("Индексация уже запущена", ((IndexingResponseWithError) response).getError());
    }

    @Test
    @DisplayName("Indexing is not running")
    public void testStartIndexing_IndexingNotRunning_ReturnsSuccessResponse() {
        when(sitesList.getSites()).thenReturn(Collections.emptyList());
        IndexingResponse response = indexingSiteService.startIndexing();
        assertTrue(response.isResult());
        assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    @DisplayName("Stopping indexing that is not running")
    public void testStopIndexing_IndexingNotRunning_ReturnsErrorResponse() {
        IndexingTask.indexingStopped();
        IndexingResponse response = indexingSiteService.stopIndexing();

        assertFalse(response.isResult());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatus());
        assertEquals("Индексация не запущена", ((IndexingResponseWithError) response).getError());
    }

    @Test
    @DisplayName("Stopping indexing")
    public void testStopIndexing_IndexingAlreadyRunning_ReturnsSuccessResponse() {
        IndexingTask.indexingIsRunning();
        IndexingResponse response = indexingSiteService.stopIndexing();

        assertTrue(response.isResult());
        assertFalse(IndexingTask.isIndexing());
        assertEquals(HttpStatus.OK, response.getStatus());
    }

}
