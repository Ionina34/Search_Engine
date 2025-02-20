package searchengine.dto.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Класс предстовляет собой ответ на запрос поиска.
 * Он содеожит информацию о результате поиска и статусе HTTP
 */
@Data
public class SearchResponse {
    private boolean result;
    /**
     * Поле содержит статус HTTP, связанный с ответом поиска.
     * Это поле игнорируется при сериализации JSON
     */
    @JsonIgnore
    private HttpStatus status;
}
