package searchengine.services.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import searchengine.dto.search.SearchDataRequest;
import searchengine.dto.search.SearchResponse;
import searchengine.dto.search.SearchResponseNotError;
import searchengine.dto.search.SearchResponseWithError;
import searchengine.model.*;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.LemmaFinder;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SearchServiceImplTest {
    private PageRepository pageRepository;
    private SiteRepository siteRepository;
    private IndexRepository indexRepository;
    private LemmaRepository lemmaRepository;
    private LemmaFinder lemmaFinder;
    private SearchServiceImpl searchService;

    private List<Site> sitesDB = Arrays.asList(
            new Site(1, StatusIndexing.INDEXED, new Timestamp(System.currentTimeMillis()), "", "https://www.playback.ru/", "PlayBack.R"),
            new Site(2, StatusIndexing.INDEXED, new Timestamp(System.currentTimeMillis()), "", "https://www.skillbox.ru/", "Скиллбох")
    );

    private List<Page> pagesDB = Arrays.asList(
            new Page(1, sitesDB.get(0), "link3", 200, "<title>Смартфон Xiaomi Poco</title> M6 8/256 ГБ черный (РСТ)\n" +
                    "19530р.   Положить в корзину\n" +
                    "Есть на складе\n" +
                    "При оплате картой (возможна только при самовывозе): 18659 р.\n" +
                    "Вы можете заказать доставку товара,\n" +
                    "либо забрать его самостоятельно из пункта самовывоза"),
            new Page(2, sitesDB.get(0), "link4", 200, "<title>Смартфон Xiaomi Poco</title> M6 8/256 ГБ черный (РСТ)\n" +
                    "12400р.   Положить в корзину\n" +
                    "Есть на складе\n" +
                    "При оплате картой (возможна только при самовывозе): 12000 р.\n" +
                    "Вы можете заказать доставку товара,\n" +
                    "либо забрать его самостоятельно из пункта самовывоза"),
            new Page(3, sitesDB.get(1), "link8", 200, "<title>Смартфон Xiaomi Poco</title> M6 8/256 ГБ черный (РСТ)\n" +
                    "19530р.   Положить в корзину\n" +
                    "Есть на складе\n" +
                    "При оплате картой (возможна только при самовывозе): 17659 р.\n" +
                    "Вы можете заказать доставку товара,\n" +
                    "либо забрать его самостоятельно из пункта самовывоза")
    );
    List<Lemma> lemmasDB = Arrays.asList(
            new Lemma(1, sitesDB.get(0), "черный", 2),
            new Lemma(2, sitesDB.get(0), "смартфон", 5),
            new Lemma(3, sitesDB.get(1), "черный", 1),
            new Lemma(4, sitesDB.get(1), "смартфон", 5)
    );

    @BeforeEach
    public void setUp() {
        pageRepository = Mockito.mock(PageRepository.class);
        siteRepository = Mockito.mock(SiteRepository.class);
        lemmaRepository = Mockito.mock(LemmaRepository.class);
        indexRepository = Mockito.mock(IndexRepository.class);
        lemmaFinder = Mockito.mock(LemmaFinder.class);
        searchService = new SearchServiceImpl(lemmaRepository, pageRepository,
                siteRepository, indexRepository, lemmaFinder);
    }

    @Test
    @DisplayName("Query is empty")
    public void testSearch_QueryEmpty_ReturnsErrorResponse() {
        String query = "";
        SearchDataRequest dataRequest = new SearchDataRequest(query, 0, 20);
        SearchResponse response = searchService.search(dataRequest);

        assertFalse(response.isResult());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals("Задан пустой поисковый запрос", ((SearchResponseWithError) response).getError());
    }

    @Test
    @DisplayName("Query with lemmas is not from database")
    public void testSearch_LemmasNotDatabase_ReturnsEmptyList() {
        String query = "черный";
        SearchDataRequest dataRequest = new SearchDataRequest(query, 0, 20);

        when(lemmaFinder.collectLemmas(anyString())).thenReturn(new HashMap<>(Map.of("черный", 1)));
        when(pageRepository.count()).thenReturn(10L);
        when(lemmaRepository.findFrequencyByLemmaAndSiteId(anyString(),null)).thenReturn(Optional.empty());

        SearchResponse response = searchService.search(dataRequest);
        assertTrue(response.isResult());
        assertEquals(0, ((SearchResponseNotError) response).getCount());
        assertEquals(List.of(), ((SearchResponseNotError) response).getData());
        assertEquals(HttpStatus.OK, ((SearchResponseNotError) response).getStatus());
    }

    @Test
    @DisplayName("The pages on the query were not found")
    public void testSearch_PagesNotFound_ReturnsEmptyList() {
        String query = "Черный смартфон";
        SearchDataRequest dataRequest = new SearchDataRequest(query, 0, 20);

        Map<String, Integer> lemmas = new HashMap<>(Map.of("черный", 1,
                "смартфон", 1));

        when(lemmaFinder.collectLemmas(query)).thenReturn(lemmas);
        when(lemmaRepository.findFrequencyByLemmaAndSiteId(lemmas.keySet().toArray()[0].toString(),null)).thenReturn(Optional.of(57));
        when(lemmaRepository.findFrequencyByLemmaAndSiteId(lemmas.keySet().toArray()[1].toString(), null)).thenReturn(Optional.of(23));
        when(pageRepository.findPageByLemmaAndSiteId(anyString(),anyInt())).thenReturn(List.of());

        SearchResponse response = searchService.search(dataRequest);
        assertTrue(response.isResult());
        assertEquals(0, ((SearchResponseNotError) response).getCount());
        assertEquals(List.of(), ((SearchResponseNotError) response).getData());
        assertEquals(HttpStatus.OK, ((SearchResponseNotError) response).getStatus());
    }

    @Test
    @DisplayName("Search through all sites")
    public void testSearch_PagesFound_ReturnsFoundPagesAllSites() {
        String query = "Черный смартфон";
        SearchDataRequest dataRequest = new SearchDataRequest(query, 0, 20);

        Map<String, Integer> lemmas = new HashMap<>(Map.of("черный", 1,
                "смартфон", 1));

        when(lemmaFinder.collectLemmas(query)).thenReturn(lemmas);
        when(pageRepository.count()).thenReturn(100L);
        when(lemmaRepository.findFrequencyByLemmaAndSiteId(eq("черный"), null)).thenReturn(Optional.of(3));
        when(lemmaRepository.findFrequencyByLemmaAndSiteId(eq("смартфон"), null)).thenReturn(Optional.of(10));

        when(pageRepository.findPageByLemmaAndSiteId(anyString(),anyInt())).thenReturn(List.of());
        when(pageRepository.findPageByLemmaAndPageId(anyString(), anyList())).thenReturn(pagesDB);

        when(indexRepository.getRankByLemmasAndPageId(anyList(),
                anyInt())).thenReturn(List.of(1,1));

        when(siteRepository.findById(1)).thenReturn(Optional.of(sitesDB.get(0)));
        when(siteRepository.findById(2)).thenReturn(Optional.of(sitesDB.get(0)));

        when(lemmaFinder.clearPageOfHtmlTags(pagesDB.get(0).getContent())).thenReturn(pagesDB.get(0).getContent());
        when(lemmaFinder.clearPageOfHtmlTags(pagesDB.get(1).getContent())).thenReturn(pagesDB.get(1).getContent());
        when(lemmaFinder.clearPageOfHtmlTags(pagesDB.get(2).getContent())).thenReturn(pagesDB.get(2).getContent());

        when(lemmaFinder.checkingForAMatchOfTheNormalForm(anyList(),eq("черный"))).thenReturn(true);
        when(lemmaFinder.checkingForAMatchOfTheNormalForm(anyList(),eq("смартфон"))).thenReturn(true);
        when(lemmaFinder.checkingForAMatchOfTheNormalForm(anyList(),argThat(arg -> !arg.equals("черный") && !arg.equals("смартфон")))).thenReturn(false);

        SearchResponse response= searchService.search(dataRequest);
        assertTrue(response.isResult());
        assertEquals(3, ((SearchResponseNotError) response).getCount());
        assertEquals(HttpStatus.OK, ((SearchResponseNotError) response).getStatus());
    }

    @Test
    @DisplayName("Single site search")
    public void testSearch_PagesFound_ReturnsFoundPagesOneSites() {
        String query = "Черный смартфон";
        SearchDataRequest dataRequest = new SearchDataRequest(query, "https://www.playback.ru/", 0, 20);

        Map<String, Integer> lemmas = new HashMap<>(Map.of("черный", 1,
                "смартфон", 1));

        when(siteRepository.findByUrl(dataRequest.getSite())).thenReturn(Optional.of(sitesDB.get(0)));
        when(lemmaFinder.collectLemmas(query)).thenReturn(lemmas);
        when(pageRepository.count()).thenReturn(100L);
        when(lemmaRepository.findFrequencyByLemmaAndSiteId(eq("черный"), eq(1))).thenReturn(Optional.of(2));
        when(lemmaRepository.findFrequencyByLemmaAndSiteId(eq("смартфон"), eq(1))).thenReturn(Optional.of(8));

        when(pageRepository.findPageByLemmaAndSiteId(anyString(),anyInt())).thenReturn(List.of());
        when(pageRepository.findPageByLemmaAndPageId(anyString(), anyList())).thenReturn(List.of(pagesDB.get(0),pagesDB.get(1)));

        when(indexRepository.getRankByLemmasAndPageId(anyList(),
                anyInt())).thenReturn(List.of(1,1));

        when(siteRepository.findById(eq(1))).thenReturn(Optional.of(sitesDB.get(0)));

        when(lemmaFinder.clearPageOfHtmlTags(pagesDB.get(0).getContent())).thenReturn(pagesDB.get(0).getContent());
        when(lemmaFinder.clearPageOfHtmlTags(pagesDB.get(1).getContent())).thenReturn(pagesDB.get(1).getContent());

        when(lemmaFinder.checkingForAMatchOfTheNormalForm(anyList(),eq("черный"))).thenReturn(true);
        when(lemmaFinder.checkingForAMatchOfTheNormalForm(anyList(),eq("смартфон"))).thenReturn(true);
        when(lemmaFinder.checkingForAMatchOfTheNormalForm(anyList(),argThat(arg -> !arg.equals("черный") && !arg.equals("смартфон")))).thenReturn(false);

        SearchResponse response= searchService.search(dataRequest);
        assertTrue(response.isResult());
        assertEquals(2, ((SearchResponseNotError) response).getCount());
        assertEquals(HttpStatus.OK, ((SearchResponseNotError) response).getStatus());
    }
}


