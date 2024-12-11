package searchengine.services.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import searchengine.model.Page;
import searchengine.model.Site;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DataToCreateDetailedSearchItem {
   private Site site;
   private Page page;
   private float absRel;
   private List<String> lemmas;
}
