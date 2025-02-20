package searchengine.dto.search;

import lombok.Data;

/**
 * Класс, предстовляющий ответ на запрос поиска с возможной ошибкой.
 * Наследуется от класса {@link SearchResponse} и добавляет поле
 * для хранения информации об ошибке.
 */
@Data
public class SearchResponseWithError extends SearchResponse{
    private String error;
}
