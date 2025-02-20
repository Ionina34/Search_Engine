package searchengine.dto.statistics;

import lombok.Data;

/**
 * Класс, предоставляющий собой детализированный элемент статистики.
 * Сожержит информацию о сайте, его названии,
 * статусе, време, ошибке, кол-ве страиц и лемм на сайте.
 */
@Data
public class DetailedStatisticsItem {
    private String url;
    private String name;
    private String status;
    private long statusTime;
    private String error;
    private int pages;
    private int lemmas;
}
