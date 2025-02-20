package searchengine.task.entity;

import org.jsoup.select.Elements;

/**
 * Класс представляет собой контейнер для хранения информации о странице
 * и связанных с ней элементах.
 */
@lombok.Data
public class PageDataContainer {
    private searchengine.model.Page page;
    private Elements elements;
}
