package searchengine.config.Init;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import searchengine.repository.SiteRepository;

@Component
public class SiteRepositoryInitializer {
    @Getter
    private static SiteRepository siteRepository;

    @Autowired
    public SiteRepositoryInitializer(ApplicationContext context) {
        siteRepository = context.getBean(SiteRepository.class);
    }
}
