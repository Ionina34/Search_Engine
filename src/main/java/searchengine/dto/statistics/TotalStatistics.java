package searchengine.dto.statistics;

import lombok.Data;

/**
 * Класс, представляющий общую статистику индексации.
 * Содержит информацию о кол-ве сайтов, страниц и лемм, а
 * так жестатус индексации.
 */
@Data
public class TotalStatistics {
    private int sites;
    private int pages;
    private int lemmas;
    private boolean indexing;
}
