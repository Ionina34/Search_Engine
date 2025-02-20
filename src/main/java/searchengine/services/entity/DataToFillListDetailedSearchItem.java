package searchengine.services.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import searchengine.model.Page;
import searchengine.model.Site;

import java.util.List;
import java.util.Optional;

/**
 * Класс, представляющий даннные для заполнения списка
 * детализированных элементов поиска.
 */
@Getter
@Setter
@AllArgsConstructor
public class DataToFillListDetailedSearchItem {
    private Site site;
    private List<Page> pagesWithAllLemmas;
    private List<String> lemmas;
}
