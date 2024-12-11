package searchengine.services.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import searchengine.model.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DataToFillListDetailedSearchItem {
    private String site;
    private List<Page> pagesWithAllLemmas;
    private List<String> lemmas;
}
