package searchengine.services;

import searchengine.dto.indexing.IndexingResponse;
import searchengine.model.Page;

/**
 * Этот интерфейс отвечает за обработку индексации страниц, включая
 * извлечение лемм из содержимого страниц
 * и сохранение информации о страницах и леммах в базе данных.
 */
public interface IndexingPageService {
    /**
     * Индексирует страницу по заданному URL.
     *
     * @param url URL страницы для индексации
     * @return объект ответа индексации с результатом и статусом
     */
    IndexingResponse indexPage(String url);

    /**
     * Индексирует массив страниц.
     *
     * @param pageList
     */
    void indexPages(Page[] pageList);
}
