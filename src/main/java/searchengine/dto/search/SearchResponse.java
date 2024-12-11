package searchengine.dto.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class SearchResponse {
    private boolean result;
    @JsonIgnore
    private HttpStatus status;
}
