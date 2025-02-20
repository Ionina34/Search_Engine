package searchengine.dto.indexing;

import lombok.Data;

/**
 * Класс, предоставляющий ответ на запрос индексации с возможной ошибкой.
 * Наследуется от класса {@link IndexingResponse} и добавляет поле
 * для хранения информации от ошибке.
 */
@Data
public class IndexingResponseWithError extends IndexingResponse {
    private String error;
}
