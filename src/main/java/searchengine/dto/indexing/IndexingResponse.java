package searchengine.dto.indexing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Класс предоставляет собой ответ на запрос индексации.
 * <p>
 *     Он содержит информацию о результате индексации и статусе HTTP.
 * </p>
 */
@Data
public class IndexingResponse{
    private boolean result;

    /**
     * Поле содержит статус HTTP, связанный с ответом индексации.
     * Это поле игнорируется при сериализации JSON.
     */
    @JsonIgnore
    private HttpStatus status;
}
