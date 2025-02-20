package searchengine.services.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Реализация сервиса для индексации страниц {@link IndexingPageService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IndexingPageServiceImpl implements IndexingPageService {

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final LemmaRepository lemmaRepository;
    private final IndexRepository indexRepository;

    private final LemmaFinder lemmaFinder;
    private final SitesList sitesList;

    /**
     * Проверяет корректность URL, извлекает сайт из конфигурации
     * и сохраняет информацию о странице и леммах.
     */
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
        Optional<Page> page = Optional.empty();
        try {
            GettingThePage gettingThePage = new GettingThePage(url, site);
            String pageUrl = gettingThePage.removeDomainFromThePath(url);
            page = pageRepository.findByPathAndSite(pageUrl, site);
            page.ifPresent(this::cleaningInfoAboutPage);
            page = Optional.of(gettingThePage.getPageByUrl());
        } catch (IOException e) {
            log.error("Error", e);
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

    /**
     * Обрабатывает каждую страницу и извлекает леммы,
     * если код ответа страницы успешный
     */
    @Override
    public void indexPages(Page[] pages) {
        for (Page page : pages) {
            if (page.getCode() / 100 == 2) {
                getLemmasByPage(page, page.getSite());
            }
        }
    }

    /**
     * Получает сайт по URL страницы из конфигурации.
     *
     * @param url URL страницы
     * @return опциональный сайт {@link Site}
     */
    private Optional<Site> getSiteByPageUrlFromConfig(String url) {
        return sitesList.getSites()
                .stream()
                .filter(s -> url.contains(s.getUrl()))
                .findFirst();
    }

    /**
     * Получает сайт из базы данных по конфигурации сайта.
     * Если сайт не найден, он создается и сохранается в базе данных.
     *
     * @param siteConf конфигурационный сайт
     * @return объект сайта из базы данных {@link searchengine.model.Site}
     */
    private searchengine.model.Site getSiteByUrlFromDataBase(Site siteConf) {
        Optional<searchengine.model.Site> site = siteRepository.findByUrl(siteConf.getUrl());
        if (site.isEmpty()) {
            site = Optional.of(ObjectConverter.convertSiteConfigIntoSiteModel(siteConf));
            siteRepository.save(site.get());
        }
        return site.get();
    }

    /**
     * Очищает информацию о странице, удаляя леммы и самц страницу из базы данных.
     *
     * @param page страница для очистки
     */
    private void cleaningInfoAboutPage(Page page) {
        List<Integer> lemmasId = indexRepository.findLemmasIdByPage(page.getId());
        indexRepository.deleteByPage(page.getId());
        lemmaRepository.minusFrequencyByListId(lemmasId);
        pageRepository.delete(page);
    }

    /**
     * Извлекает леммы из содержимого страницы и сохраняет из в базе данных
     *
     * @param page страница для извлечения лемм
     * @param site сайт, к которому принадлежит страница
     */
    private void getLemmasByPage(Page page, searchengine.model.Site site) {
        try {
            String content = lemmaFinder.clearPageOfHtmlTags(page.getContent());
            Map<String, Integer> lemmas = lemmaFinder.collectLemmas(content);
            for (Map.Entry<String, Integer> entry : lemmas.entrySet()) {
                saveInfoAboutPage(entry, page, site);
            }
        } catch (Exception e) {
            log.error("Error", e);
        }
    }

    /**
     * Сохранаяет информацию о страницах и леммаз в базу данных.
     * Если леммы не существует, она создается.
     *
     * @param entry пара "лемма - частота"
     * @param page  страница, к которой относиться лемма
     * @param site  сайт, к которому принадлежит страница
     */
    private synchronized void saveInfoAboutPage(Map.Entry<String, Integer> entry,
                                                Page page, searchengine.model.Site site) {
        Optional<Lemma> lemma = lemmaRepository.findByLemmaAndSiteId(entry.getKey(), site.getId());
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

    /**
     * Создает новый объект леммы {@link Lemma}
     *
     * @param site  сайт, к которому привязана лемма
     * @param lemma значение леммы
     * @return новый объект леммы {@link Lemma}
     */
    private Lemma creatingNewObjectLemma(searchengine.model.Site site, String lemma) {
        Lemma newLemma = new Lemma();
        newLemma.setSite(site);
        newLemma.setLemma(lemma);
        newLemma.setFrequency(1);
        return newLemma;
    }

    /**
     * Создает новый объект индекс {@link Index}
     *
     * @param page  страница, к которому привязан индекс
     * @param lemma лемма, к которой привязан индекс
     * @param rank  часота
     * @return новый объект индекс {@link Index}
     */
    private Index creatingNewObjectIndex(Page page, Lemma lemma, Integer rank) {
        Index index = new Index();
        index.setPage(page);
        index.setLemma(lemma);
        index.setRank(rank);
        return index;
    }

    /**
     * Проверяет является ли строка ссылкой.
     *
     * @param url строка, которую необходимо проверить
     * @return результат проверки
     */
    private boolean checkingWhetherStringIsUrl(String url) {
        String string = url.trim().toLowerCase();
        return string.startsWith("http://") || string.startsWith("https://");
    }

    /**
     * Создает объект ответа с ошибкой.
     *
     * @param error ошибка
     * @return объек ответа
     */
    private IndexingResponse createErrorResponse(String error) {
        IndexingResponseWithError response = new IndexingResponseWithError();
        response.setResult(false);
        response.setError(error);
        response.setStatus(HttpStatus.BAD_REQUEST);
        return response;
    }

}
