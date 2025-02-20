package searchengine.config.Init;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import searchengine.repository.SiteRepository;

/**
 * Инициализатор для репозитория, работающего с моделью {@link searchengine.model.Site}.
 * <p>
 *     Этот класс отвечает за инициализацию {@link SiteRepository} из контекста приложения.
 * </p>
 */
@Component
public class SiteRepositoryInitializer {
    @Getter
    private static SiteRepository siteRepository;

    /**
     * Конструктор, который получает {@link ApplicationContext} и инициализирует
     * {@link SiteRepository}.
     *
     * @param context контекст приложения, используемый для получения бина {@link SiteRepository}
     */
    @Autowired
    public SiteRepositoryInitializer(ApplicationContext context) {
        siteRepository = context.getBean(SiteRepository.class);
    }
}
