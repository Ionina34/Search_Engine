package searchengine.config.Init;

import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import searchengine.services.IndexingPageService;

/**
 * Инициализатор для сервиса индексации страниц.
 * <p>
 *     Этот класс отвчает за иниализацию {@link IndexingPageService}
 *     из контекста приложения.
 * </p>
 */
@Component
public class IndexingPageInitializer {
    @Getter
    private static IndexingPageService indexingPageService;

    /**
     * Конструктор, который получает {@link ApplicationContext} и
     * инициализирует {@link IndexingPageService}.
     *
     * @param context контекст приложения, используемый
     * для полученичя бина {@link IndexingPageService}
     */
    public IndexingPageInitializer(ApplicationContext context) {
        indexingPageService = context.getBean(IndexingPageService.class);
    }
}
