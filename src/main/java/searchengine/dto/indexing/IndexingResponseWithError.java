package searchengine.dto.indexing;

import lombok.Data;

@Data
public class IndexingResponseWithError extends IndexingResponse {
    private String error;
}
