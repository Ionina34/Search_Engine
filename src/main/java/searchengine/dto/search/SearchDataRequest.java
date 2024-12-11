package searchengine.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchDataRequest {
    private String query;
    private String site;
    private int offset;
    private int limit;

    public SearchDataRequest(String query, int offset, int limit) {
        this.query = query;
        this.offset = offset;
        this.limit = limit;
    }
}
