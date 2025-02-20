package searchengine.services.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import searchengine.config.SitesList;
import searchengine.dto.statistics.DetailedStatisticsItem;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.TotalStatistics;
import searchengine.model.Site;
import searchengine.model.StatusIndexing;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StatisticsServiceImplTest {
    private SiteRepository siteRepository;
    private PageRepository pageRepository;
    private LemmaRepository lemmaRepository;
    private SitesList sitesList;

    private StatisticsServiceImpl statisticsService;
    private Random random = new Random();

    private List<Site> sitesDB = Arrays.asList(
            new Site(1, StatusIndexing.INDEXED, new Timestamp(System.currentTimeMillis()), "", "https://www.playback.ru/", "PlayBack.R"),
            new Site(2, StatusIndexing.INDEXED, new Timestamp(System.currentTimeMillis()), "", "https://www.skillbox.ru/", "Скиллбох")
    );

    private List<searchengine.config.Site> sitesConf = Arrays.asList(
            new searchengine.config.Site("https://www.playback.ru/", "PlayBack.R"),
            new searchengine.config.Site("https://www.skillbox.ru/", "Скиллбох")
    );

    @BeforeEach
    public void setUp() {
        siteRepository = Mockito.mock(SiteRepository.class);
        pageRepository = Mockito.mock(PageRepository.class);
        lemmaRepository = Mockito.mock(LemmaRepository.class);
        sitesList = Mockito.mock(SitesList.class);
        statisticsService = new StatisticsServiceImpl(siteRepository, pageRepository,
                lemmaRepository, sitesList);
    }

    @Test
    @DisplayName("Get statistics")
    public void testStatistics_SitesThereAre_ReturnsList() {
        when(siteRepository.getNumSites()).thenReturn(sitesDB.size());
        when(sitesList.getSites()).thenReturn(sitesConf);
        when(siteRepository.findByName(eq("PlayBack.R"))).thenReturn(Optional.of(sitesDB.get(0)));
        when(siteRepository.findByName(eq("Скиллбох"))).thenReturn(Optional.of(sitesDB.get(1)));

        int randPages1=random.nextInt(10, 1000 + 1);
        int randPages2=random.nextInt(10, 1000 + 1);
        int randLemmas1=random.nextInt(10, 1000 + 1);
        int randLemmas2=random.nextInt(10, 1000 + 1);
        when(pageRepository.countPagesBySiteId(eq(1))).thenReturn(randPages1);
        when(pageRepository.countPagesBySiteId(eq(2))).thenReturn(randPages2);
        when(lemmaRepository.countLemmasBySiteId(eq(1))).thenReturn(randLemmas1);
        when(lemmaRepository.countLemmasBySiteId(eq(2))).thenReturn(randLemmas2);

        StatisticsResponse response = statisticsService.getStatistics();

        assertTrue(response.isResult());
        List<DetailedStatisticsItem> items= response.getStatistics().getDetailed();
        TotalStatistics total= response.getStatistics().getTotal();
        assertEquals(items.size(),sitesConf.size());
        assertEquals(total.getSites(),sitesDB.size());
        assertEquals(total.getPages(),randPages1+randPages2);
        assertEquals(total.getLemmas(),randLemmas1+randLemmas2);
    }
}
