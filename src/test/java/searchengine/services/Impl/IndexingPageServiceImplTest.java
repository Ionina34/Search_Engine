package searchengine.services.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import searchengine.config.SitesList;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.dto.indexing.IndexingResponseWithError;
import searchengine.config.Site;
import searchengine.model.Page;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.LemmaFinder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IndexingPageServiceImplTest {
    private SiteRepository siteRepository;
    private PageRepository pageRepository;
    private LemmaRepository lemmaRepository;
    private IndexRepository indexRepository;
    private SitesList sitesList;
    private LemmaFinder lemmaFinder;
    private IndexingPageServiceImpl indexingPageService;

    @BeforeEach
    public void setUp() {
        siteRepository = Mockito.mock(SiteRepository.class);
        pageRepository = Mockito.mock(PageRepository.class);
        lemmaRepository = Mockito.mock(LemmaRepository.class);
        indexRepository = Mockito.mock(IndexRepository.class);
        sitesList = Mockito.mock(SitesList.class);
        lemmaFinder = Mockito.mock(LemmaFinder.class);
        indexingPageService = new IndexingPageServiceImpl(siteRepository,
                pageRepository, lemmaRepository, indexRepository, lemmaFinder, sitesList);
    }

    @Test
    @DisplayName("Checking for a an incorrectly entered link")
    public void testIndexPage_InvalidUrl_ReturnsErrorResponse() {
        String invalidUrl = "invalid-url";
        IndexingResponse response = indexingPageService.indexPage(invalidUrl);
        assertFalse(response.isResult());
        assertEquals("Некорректно введенная ссылка.", ((IndexingResponseWithError) response).getError());
    }

    @Test
    @DisplayName("Checking for an off-site link")
    public void testIndexPage_UrlNotInConfig_ReturnsErrorResponse() {
        String validUrl = "https://skillbox.com";
        Site site = new Site();
        site.setUrl("https://www.playback.ru/");
        when(sitesList.getSites()).thenReturn(List.of(site));

        IndexingResponse response = indexingPageService.indexPage(validUrl);

        assertFalse(response.isResult());
        assertEquals("Данная страница находится за пределами сайтов, указанных в конфигурационном файле.",
                ((IndexingResponseWithError) response).getError());
        verify(sitesList, times(1)).getSites();
    }

    @Test
    @DisplayName("Checking for an valid link")
    public void testIndexPage_ValidUrl_SavesPage() {
        String validUrl = "https://www.playback.ru/basket.html";
        Site siteConf = new Site();
        siteConf.setUrl("https://www.playback.ru/");

        searchengine.model.Site site = ObjectConverter.convertSiteConfigIntoSiteModel(siteConf);
        Page page = new Page();
        page.setPath(validUrl.replace(siteConf.getUrl(), ""));
        page.setSite(site);

        when(sitesList.getSites()).thenReturn(List.of(siteConf));
        when(siteRepository.findByUrl(siteConf.getUrl())).thenReturn(Optional.of(site));
        when(pageRepository.findByPathAndSite(anyString(), any())).thenReturn(Optional.empty());

        when(pageRepository.save(any())).thenReturn(page);
        when(lemmaFinder.clearPageOfHtmlTags(anyString())).thenReturn("some content");
        when(lemmaFinder.collectLemmas(anyString())).thenReturn(Collections.emptyMap());

        IndexingResponse response = indexingPageService.indexPage(validUrl);
        assertTrue(response.isResult());
        assertEquals(HttpStatus.OK, response.getStatus());


        verify(lemmaRepository,times(0)).findByLemmaAndSiteId(anyString(),anyInt());
        verify(lemmaRepository,times(0)).save(any());
        verify(indexRepository,times(0)).save(any());
    }
}
