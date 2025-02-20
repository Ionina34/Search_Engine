package searchengine.task;

import lombok.extern.slf4j.Slf4j;
import searchengine.config.Init.IndexingPageInitializer;
import searchengine.model.Page;
import searchengine.services.IndexingPageService;

/**
 * Класс, представляющий задачу индексации страниц.
 * Реализует интерфейс {@link Runnable}, что позволяет выполнить
 * индексацию в отдельном потоке.
 */
@Slf4j
public class IndexingPageTask implements Runnable {
    private final Page[] pages;
    private final IndexingPageService indexingPageService;

    /**
     * Конструктор для создания задачи индексации с использованием сервиса
     * индексации по умолчанию.
     *
     * @param pages массив страниц, которые необходимо индексировать
     */
    public IndexingPageTask(Page[] pages) {
        this.pages = pages;
        indexingPageService = IndexingPageInitializer.getIndexingPageService();
    }

    /**
     * Конструктор для создания задачи индексации с использованием указанного сервиса индексации.
     *
     * @param pages Массив страниц, которые необходимо индексировать.
     * @param indexingPageService Сервис для индексации страниц.
     */
    public IndexingPageTask(Page[] pages, IndexingPageService indexingPageService) {
        this.pages = pages;
        this.indexingPageService = indexingPageService;
    }

    /**
     * Метод, выполняющий индексацию страниц.
     * Запускается в отдельном потоке. В случае возникновения исключения, оно будет
     * зафиксировано в логах.
     */
    @Override
    public void run() {
        try {
            indexingPageService.indexPages(pages);
        } catch (Exception e) {
            log.error("Error", e);
        }
    }
}
