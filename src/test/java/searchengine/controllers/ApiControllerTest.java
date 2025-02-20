package searchengine.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.dto.search.SearchDataRequest;
import searchengine.dto.search.SearchResponse;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.IndexingPageService;
import searchengine.services.IndexingSiteService;
import searchengine.services.SearchService;
import searchengine.services.StatisticsService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ApiController.class)
public class ApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService statisticsService;
    @MockBean
    private IndexingSiteService indexingSiteService;
    @MockBean
    private IndexingPageService indexingPageService;
    @MockBean
    private SearchService searchService;

    @Test
    @DisplayName("/api/statistics")
    public void testStatistics_ReturnsResponse() throws Exception{
        StatisticsResponse response = new StatisticsResponse();
        response.setResult(true);
        response.setStatus(HttpStatus.OK);

        when(statisticsService.getStatistics()).thenReturn(response);

        mockMvc.perform(get("/api/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("true"));
    }

    @Test
    @DisplayName("/api/startIndexing")
    public void testStartIndexing_ReturnsResponse() throws Exception{
        IndexingResponse response= new IndexingResponse();
        response.setStatus(HttpStatus.OK);
        response.setResult(true);

        when(indexingSiteService.startIndexing()).thenReturn(response);

        mockMvc.perform(get("/api/startIndexing"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("/api/stopIndexing")
    public void testStopIndexing_ReturnsResponse() throws Exception{
        IndexingResponse response= new IndexingResponse();
        response.setStatus(HttpStatus.OK);
        response.setResult(true);

        when(indexingSiteService.stopIndexing()).thenReturn(response);

        mockMvc.perform(get("/api/stopIndexing"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("/api/indexPage")
        public void testIndexPage_ReturnResponse() throws Exception {
        IndexingResponse response = new IndexingResponse();
        response.setResult(true);
        response.setStatus(HttpStatus.OK);
        when(indexingPageService.indexPage(anyString())).thenReturn(response);

        mockMvc.perform(post("/api/indexPage").param("url",
                "http://example.com"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("/api/search")
    public void testSearch_ReturnsResponse() throws Exception {
        SearchResponse response = new SearchResponse();
        response.setStatus(HttpStatus.OK);
        response.setResult(true);
        when(searchService.search(any(SearchDataRequest.class))).thenReturn(response);

        mockMvc.perform(get("/api/search").param("query", "test"))
                .andExpect(status().isOk());
    }
}
