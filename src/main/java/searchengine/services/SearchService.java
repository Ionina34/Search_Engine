package searchengine.services;

import searchengine.dto.search.SearchDataRequest;
import searchengine.dto.search.SearchResponse;

public interface SearchService {
    SearchResponse search(SearchDataRequest searchData);
}
