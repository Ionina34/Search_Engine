package searchengine.config.Init;

import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import searchengine.services.IndexingPageService;

@Component
public class IndexingPageInitializer {
    @Getter
    private static IndexingPageService indexingPageService;

    public IndexingPageInitializer(ApplicationContext context) {
        indexingPageService = context.getBean(IndexingPageService.class);
    }
}
