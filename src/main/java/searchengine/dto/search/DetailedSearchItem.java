package searchengine.dto.search;

import lombok.Data;

/**
 * Класс представляющий детализированный элемент поиска.
 * Содержит информацию о сайте, его названии, URI, загаловке,
 * фрагменте и релевантности.
 */
@Data
public class DetailedSearchItem {
    private String site;
    private String siteName;
    private String uri;
    private String title;
    private String snippet;
    private float relevance;
}
