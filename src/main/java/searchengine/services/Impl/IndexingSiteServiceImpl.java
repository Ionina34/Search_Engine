package searchengine.services.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import searchengine.config.SitesList;
import searchengine.dto.indexing.*;
import searchengine.config.Site;

import searchengine.model.Page;
import searchengine.model.StatusIndexing;
import searchengine.repository.*;
import searchengine.services.*;
import searchengine.task.IndexingPageTask;
import searchengine.task.IndexingTask;
import searchengine.task.UpdateStatusTimeTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class IndexingSiteServiceImpl implements IndexingSiteService {
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final IndexRepository indexRepository;
    private final LemmaRepository lemmaRepository;
    private final IndexingPageService indexingPageService;

    private final SitesList sitesList;
    private final List<IndexingTask> tasks = new ArrayList<>();
    private boolean isStopped = false;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public IndexingResponse startIndexing() {
        if (IndexingTask.isIndexing()) {
            IndexingResponseWithError response = new IndexingResponseWithError();
            response.setResult(false);
            response.setStatus(HttpStatus.FORBIDDEN);
            response.setError("Индексация уже запущена");
            return response;
        }

        deleteDate();
        IndexingTask.indexingIsRunning();
        List<Site> sites = sitesList.getSites();
        try {
            for (Site siteConfig : sites) {
                searchengine.model.Site site = ObjectConverter.convertSiteConfigIntoSiteModel(siteConfig);
                site.setStatus(StatusIndexing.INDEXING);
                siteRepository.save(site);

                IndexingTask indexingTask = new IndexingTask(siteConfig.getUrl(), site);
                indexingTask.fork();
                UpdateStatusTimeTask.startingTaskToUpdateTheStatusTime(site);
                tasks.add(indexingTask);
            }
            waitingAndSavingIndexingResult();
            IndexingTask.indexingStopped();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        IndexingResponse response = new IndexingResponse();
        response.setStatus(HttpStatus.OK);
        response.setResult(true);
        return response;
    }

    @Override
    public IndexingResponse stopIndexing() {
        IndexingResponse response;
        if (IndexingTask.isIndexing()) {
            response = new IndexingResponse();
            response.setStatus(HttpStatus.OK);
            response.setResult(true);
            isStopped = true;
            IndexingTask.indexingStopped();
        } else {
            response = new IndexingResponseWithError();
            response.setResult(false);
            response.setStatus(HttpStatus.FORBIDDEN);
            ((IndexingResponseWithError) response).setError("Индексация не запущена");
        }

        return response;
    }

    private void cleaningResources() {
        IndexingTask.cleanVerifiedList();
        UpdateStatusTimeTask.stoppingTaskToUpdateTheStatusTime();
    }

    private void waitingAndSavingIndexingResult() {
        for (IndexingTask task : tasks) {
            List<Page> pages = task.join();
            searchengine.model.Site site = task.getSite();
            if (isStopped) {
                site.setStatus(StatusIndexing.FAILED);
                site.setLastError("Пользователь остановил индексиование.");
                isStopped = false;
            }
            siteRepository.save(site);
            pageRepository.saveAll(pages);
            addLemmasAndIndexesForPages(pages);
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            e.printStackTrace();
        } finally {
            cleaningResources();
        }
    }

    private void addLemmasAndIndexesForPages(List<Page> pages) {
        int cores = Runtime.getRuntime().availableProcessors();
        List<Page[]> pageList;
        if (pages != null) {
            pageList = getListPages(pages, cores);
            for (Page[] pages1 : pageList) {
                executorService.execute(new IndexingPageTask(pages1));
            }
        }
    }

    private List<Page[]> getListPages(List<Page> pages, int cores) {
        Page[] pagesArray = new Page[pages.size()];
        pages.toArray(pagesArray);
        List<Page[]> pagesList = new ArrayList<>();
        int size = pages.size() / cores;
        for (int i = 0; i < cores; i++) {
            if (i == cores - 1) {
                int sizeLast = size + (pages.size() - (size * cores));
                Page[] pages1 = new Page[sizeLast];
                System.arraycopy(pagesArray, size * i, pages1, 0, sizeLast);
                pagesList.add(pages1);
            } else {
                Page[] pages1 = new Page[size];
                System.arraycopy(pagesArray, size * i, pages1, 0, size);
                pagesList.add(pages1);
            }
        }
        return pagesList;
    }

    private void deleteDate() {
        indexRepository.deleteAllData();
        lemmaRepository.deleteAllData();
        pageRepository.deleteAllData();
        siteRepository.deleteAllData();
        updateIdentity();
    }

    private void updateIdentity() {
        pageRepository.updateIdentity();
        siteRepository.updateIdentity();
        indexRepository.updateIdentity();
        lemmaRepository.updateIdentity();
    }
}
