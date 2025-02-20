package searchengine.services;

import searchengine.dto.statistics.StatisticsResponse;

/**
 * Этот интерфейс предоставляет метрики по индексированным сайтам,
 * страницам и леммам.
 */
public interface StatisticsService {
    /**
     * Получает статистику по сайтам, страницам и леммам.
     *
     * @return {@link StatisticsResponse} объект, содержащий информацию о статистике
     */
    StatisticsResponse getStatistics();
}
