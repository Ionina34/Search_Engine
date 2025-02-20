package searchengine.dto.statistics;

import lombok.Data;

import java.util.List;

/**
 * Класс, предстовляющий ответ на запрос статистике.
 * Содержит общую статистику индексации по всем сайтам {@link TotalStatistics} и
 * список детализированных элементов статистики {@link DetailedStatisticsItem}
 */
@Data
public class StatisticsData {
    private TotalStatistics total;
    private List<DetailedStatisticsItem> detailed;
}
