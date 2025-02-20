package searchengine.dto.search;

import lombok.Data;

import java.util.List;

/**
 * Класс, представляющий ответ на поисковой запрос без ошибок.
 * Содержит кол-во найденных элементов и список детализированных
 * элементов поиска {@link DetailedSearchItem}.
 */
@Data
public class SearchResponseNotError extends SearchResponse{
    private int count;
    private List<DetailedSearchItem> data;
}
