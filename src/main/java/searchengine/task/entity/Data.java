package searchengine.task.entity;

import org.jsoup.select.Elements;

@lombok.Data
public class Data {
    private searchengine.model.Page page;
    private Elements elements;
}
