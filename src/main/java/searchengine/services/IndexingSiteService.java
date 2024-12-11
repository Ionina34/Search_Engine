package searchengine.services;

import searchengine.dto.indexing.IndexingResponse;

public interface IndexingSiteService {
    IndexingResponse startIndexing();

    IndexingResponse stopIndexing();

}
