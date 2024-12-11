package searchengine.services;

import searchengine.dto.indexing.IndexingResponse;
import searchengine.model.Page;


public interface IndexingPageService {
    IndexingResponse indexPage(String url);

    void indexPages(Page[] pageList);
}
