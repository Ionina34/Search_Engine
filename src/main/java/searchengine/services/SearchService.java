package searchengine.services;

import searchengine.dto.search.SearchDataRequest;
import searchengine.dto.search.SearchResponse;

/**
 * Сервис производит поиск либо по всем страницам, либо если задан сайт
 * в пределах одного сайта.
 */
public interface SearchService {
    /**
     * Выполняет поиск по указанным данным
     *
     * @param searchData запрос на выполнение поиска
     * @return результат поиска, включая статусы и данные о найденных страницах
     */
    SearchResponse search(SearchDataRequest searchData);
}
