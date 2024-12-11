package searchengine.services.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.statistics.DetailedStatisticsItem;
import searchengine.dto.statistics.StatisticsData;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.TotalStatistics;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.StatisticsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final LemmaRepository lemmaRepository;

    private final SitesList sites;

    @Override
    public StatisticsResponse getStatistics() {
        TotalStatistics total = new TotalStatistics();
        int numIndexedSites = siteRepository.getNumSites();
        total.setSites(numIndexedSites);
        total.setIndexing(numIndexedSites != 0);

        List<DetailedStatisticsItem> detailed = new ArrayList<>();
        List<Site> sitesList = sites.getSites();
        for (int i = 0; i < sitesList.size(); i++) {
            Site site = sitesList.get(i);
            DetailedStatisticsItem item = new DetailedStatisticsItem();
            item.setName(site.getName());
            item.setUrl(site.getUrl());

            Optional<searchengine.model.Site> siteDB = siteRepository.findByName(site.getName());
            int pages = siteDB.isPresent() ? pageRepository.countPagesBySiteId(siteDB.get().getId()) : 0;
            int lemmas = siteDB.isPresent() ? lemmaRepository.countLemmasBySiteId(siteDB.get().getId()) : 0;

            item.setPages(pages);
            item.setLemmas(lemmas);
            item.setStatus(siteDB.isPresent() ? siteDB.get().getStatus().toString() : "NOT INDEXED");
            item.setError(siteDB.isPresent() ? siteDB.get().getLastError() : "Сайт не был индексирован");
            item.setStatusTime(siteDB.isPresent() ? siteDB.get().getStatusTime().getTime() : 0);

            total.setPages(total.getPages() + pages);
            total.setLemmas(total.getLemmas() + lemmas);
            detailed.add(item);
        }

        StatisticsResponse response = new StatisticsResponse();
        StatisticsData data = new StatisticsData();
        data.setTotal(total);
        data.setDetailed(detailed);
        response.setStatistics(data);
        response.setResult(true);
        response.setStatus(HttpStatus.OK);
        return response;
    }
}
