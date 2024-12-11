package searchengine.dto.indexing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class IndexingResponse{
    private boolean result;
    @JsonIgnore
    private HttpStatus status;
}
