package searchengine.task;

import searchengine.config.Init.IndexingPageInitializer;
import searchengine.model.Page;
import searchengine.services.IndexingPageService;

public class IndexingPageTask implements Runnable {
    private final Page[] pages;
    private final IndexingPageService indexingPageService;

    public IndexingPageTask(Page[] pages) {
        this.pages = pages;
        indexingPageService = IndexingPageInitializer.getIndexingPageService();
    }

    @Override
    public void run() {
        try {
            indexingPageService.indexPages(pages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
