package searchengine.services;

import searchengine.dto.indexing.IndexingResponse;

/**
 * Этот интерфейс отвечает за обработку индексации сайта
 * и сохранение информации о сайте и его страницах в базу данных.
 */
public interface IndexingSiteService {
    /**
     * Запускает процесс индексации сайтов, если он не запущен.
     *
     * @return объект ответа {@link IndexingResponse} с результатом выполнения
     */
    IndexingResponse startIndexing();

    /**
     * Останавливает процесс индексации, если он запущен
     *
     * @return объект ответа {@link IndexingResponse} с результатом выполения
     */
    IndexingResponse stopIndexing();

}
