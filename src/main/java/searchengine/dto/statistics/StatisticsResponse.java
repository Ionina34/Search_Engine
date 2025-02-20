package searchengine.dto.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Класс предстовляет собой ответ на запрос ститистике.
 * Содержит инвормацию о успешности запроса, результате {@link StatisticsData} и статусе HTTP.
 */
@Data
public class StatisticsResponse {
    private boolean result;
    private StatisticsData statistics;
    /**
     * Поле содердит статус HTTP, связанный с запросом
     * Это поле игнорируется при сериализации JSON.
      */
    @JsonIgnore
    private HttpStatus status;
}
