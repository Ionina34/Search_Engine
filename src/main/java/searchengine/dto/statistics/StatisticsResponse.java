package searchengine.dto.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class StatisticsResponse {
    private boolean result;
    private StatisticsData statistics;
    @JsonIgnore
    private HttpStatus status;
}
