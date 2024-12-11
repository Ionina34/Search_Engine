package searchengine.services.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.indexing.*;
import searchengine.model.*;
import searchengine.repository.*;
import searchengine.services.IndexingPageService;
import searchengine.services.LemmaFinder;
import searchengine.task.GettingThePage;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class IndexingPageServiceImpl implements IndexingPageService {
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final LemmaRepository lemmaRepository;
    private final IndexRepository indexRepository;

    private final LemmaFinder lemmaFinder = new LemmaFinder();
    private final SitesList sitesList;

    @Override
    public IndexingResponse indexPage(String url) {
        boolean isTheUrl = checkingWhetherStringIsUrl(url);
        if (!isTheUrl) {
            return createErrorResponse("Некорректно введенная ссылка.");
        }

        Optional<Site> siteFromConfig = getSiteByPageUrlFromConfig(url);
        if (siteFromConfig.isEmpty()) {
            return createErrorResponse("Данная страница находится за пределами сайтов, указанных в конфигурационном файле.");
        }

        searchengine.model.Site site = getSiteByUrlFromDataBase(siteFromConfig.get());

        String pageUrl = url.replace(site.getUrl(), "");
        Optional<Page> page = pageRepository.findByPathAndSite(pageUrl, site);
        page.ifPresent(this::cleaningInfoAboutPage);

        try {
            GettingThePage gettingThePage = new GettingThePage(url, site);
            page = Optional.of(gettingThePage.getPageByUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (page.isPresent()) {
            pageRepository.save(page.get());
            getLemmasByPage(page.get(), site);
        }

        IndexingResponse response = new IndexingResponse();
        response.setResult(true);
        response.setStatus(HttpStatus.OK);
        return response;
    }

    @Override
    public void indexPages(Page[] pages) {
        for (Page page : pages) {
            if (page.getCode() / 100 == 2) {
                getLemmasByPage(page, page.getSite());
            }
        }
    }

    private Optional<Site> getSiteByPageUrlFromConfig(String url) {
        return sitesList.getSites()
                .stream()
                .filter(s -> url.contains(s.getUrl()))
                .findFirst();
    }

    private searchengine.model.Site getSiteByUrlFromDataBase(Site siteConf) {
        Optional<searchengine.model.Site> site = siteRepository.findByUrl(siteConf.getUrl());
        if (site.isEmpty()) {
            site = Optional.of(ObjectConverter.convertSiteConfigIntoSiteModel(siteConf));
            siteRepository.save(site.get());
        }
        return site.get();
    }

    private void cleaningInfoAboutPage(Page page) {
        List<Integer> lemmasId = indexRepository.findLemmasIdByPage(page.getId());
        indexRepository.deleteByPage(page.getId());
        lemmaRepository.updateFrequencyById(lemmasId);
        pageRepository.delete(page);
    }

    private void getLemmasByPage(Page page, searchengine.model.Site site) {
        try {
            String content = lemmaFinder.clearPageOfHtmlTags(page.getContent());
            Map<String, Integer> lemmas = lemmaFinder.collectLemmas(content);
            for (Map.Entry<String, Integer> entry : lemmas.entrySet()) {
                saveInfoAboutPage(entry, page, site);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void saveInfoAboutPage(Map.Entry<String, Integer> entry,
                                                Page page, searchengine.model.Site site) {
        Optional<Lemma> lemma = lemmaRepository.findByLemma(entry.getKey());
        if (lemma.isEmpty()) {
            Lemma newLemma = creatingNewObjectLemma(site, entry.getKey());
            lemma = Optional.of(newLemma);
        } else {
            int frequency = lemma.get().getFrequency() + 1;
            lemma.get().setFrequency(frequency);
        }
        lemmaRepository.save(lemma.get());

        Index newIndex = creatingNewObjectIndex(page, lemma.get(), entry.getValue());
        indexRepository.save(newIndex);
    }

    private Lemma creatingNewObjectLemma(searchengine.model.Site site, String lemma) {
        Lemma newLemma = new Lemma();
        newLemma.setSite(site);
        newLemma.setLemma(lemma);
        newLemma.setFrequency(1);
        return newLemma;
    }

    private Index creatingNewObjectIndex(Page page, Lemma lemma, Integer rank) {
        Index index = new Index();
        index.setPage(page);
        index.setLemma(lemma);
        index.setRank(rank);
        return index;
    }

    private boolean checkingWhetherStringIsUrl(String url) {
        String string = url.trim().toLowerCase();
        return string.startsWith("http://") || string.startsWith("https://");
    }

    private IndexingResponse createErrorResponse(String error) {
        IndexingResponseWithError response = new IndexingResponseWithError();
        response.setResult(false);
        response.setError(error);
        response.setStatus(HttpStatus.BAD_REQUEST);
        return response;
    }

}
