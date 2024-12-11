package searchengine.dto.search;

import lombok.Data;

@Data
public class SearchResponseWithError extends SearchResponse{
    private String error;
}
