package searchengine.dto.search;

import lombok.Data;

import java.util.List;

@Data
public class SearchResponseNotError extends SearchResponse{
    private int count;
    private List<DetailedSearchItem> data;
}
