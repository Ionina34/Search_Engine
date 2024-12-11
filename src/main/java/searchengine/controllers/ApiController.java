package searchengine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.dto.search.SearchDataRequest;
import searchengine.dto.search.SearchResponse;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.IndexingPageService;
import searchengine.services.IndexingSiteService;
import searchengine.services.SearchService;
import searchengine.services.StatisticsService;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final StatisticsService statisticsService;
    private final IndexingSiteService indexingSiteService;
    private final IndexingPageService indexingPageService;
    private final SearchService searchService;

    public ApiController(StatisticsService statisticsService, IndexingSiteService indexingService, IndexingPageService indexingPageService, SearchService searchService)
    {
        this.statisticsService = statisticsService;
        this.indexingSiteService =indexingService;
        this.indexingPageService = indexingPageService;
        this.searchService = searchService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        StatisticsResponse response = statisticsService.getStatistics();
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<IndexingResponse> startIndexing(){
        IndexingResponse response= indexingSiteService.startIndexing();
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/stopIndexing")
    public  ResponseEntity<IndexingResponse> stopIndexing(){
        IndexingResponse response = indexingSiteService.stopIndexing();
        return new ResponseEntity<>(response,response.getStatus());
    }

    @PostMapping("/indexPage")
    public ResponseEntity<IndexingResponse> indexPage(@RequestParam String url){
        IndexingResponse response = indexingPageService.indexPage(url);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponse> search(@ModelAttribute SearchDataRequest data){
        SearchResponse response = searchService.search(data);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
