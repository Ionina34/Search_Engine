package searchengine.services.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import searchengine.dto.search.*;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.LemmaFinder;
import searchengine.services.SearchService;
import searchengine.services.entity.DataToCreateDetailedSearchItem;
import searchengine.services.entity.DataToFillListDetailedSearchItem;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Реализация сервиса поиска {@link SearchService}.
 */
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final LemmaRepository lemmaRepository;
    private final PageRepository pageRepository;
    private final SiteRepository siteRepository;
    private final IndexRepository indexRepository;

    private final LemmaFinder lemmaFinder;
    private final double percentOfTheNumberPages = 0.6;

    @Override
    public SearchResponse search(SearchDataRequest searchData) {
        if (searchData.getQuery().isEmpty()) {
            SearchResponseWithError response = new SearchResponseWithError();
            response.setResult(false);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError("Задан пустой поисковый запрос");
            return response;
        }
        SearchResponseNotError response = new SearchResponseNotError();

        Optional<Site> site = getSiteFromRequest(searchData.getSite());
        Map<String, Integer> lemmasInQuery = lemmaFinder.collectLemmas(searchData.getQuery());
        List<Map.Entry<String, Integer>> sortedLemmas = getListLemmasInAscendingOrder(lemmasInQuery, site.orElse(null));

        List<String> lemmas = sortedLemmas.stream()
                .map(Map.Entry::getKey)
                .toList();
        if (lemmas.isEmpty()) {
            return createEmptyResponse();
        }
        List<Page> pagesWithAllLemmas = getPagesWithAllLemmasFromQuery(lemmas, site.orElse(null));
        if (pagesWithAllLemmas.isEmpty()) {
            return createEmptyResponse();
        }

        DataToFillListDetailedSearchItem data = new DataToFillListDetailedSearchItem(
                site.orElse(null), pagesWithAllLemmas, lemmas
        );
        List<DetailedSearchItem> detailedSearchItems = fillTheListWithDetailedSearchItem(data, searchData.getOffset(), searchData.getLimit());

        response.setResult(true);
        response.setCount(pagesWithAllLemmas.size());
        response.setStatus(HttpStatus.OK);
        response.setData(detailedSearchItems);
        return response;
    }

    /**
     * Возвращает список лемм в порядке возрастания частоты.
     *
     * @param lemmasInQuery леммы с частотой запроса
     * @param site обект сайта, по которому выполняется поиск
     * @return отсортированный список пар лемма-частота
     */
    private List<Map.Entry<String, Integer>> getListLemmasInAscendingOrder(Map<String, Integer> lemmasInQuery, Site site) {
        Map<String, Integer> lemmasInQueryWithFrequency = getLemmasWithNumberOfPages(lemmasInQuery, site);

        List<Map.Entry<String, Integer>> lemmasOrderBy = new ArrayList<>(lemmasInQueryWithFrequency.entrySet());
        lemmasOrderBy.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        return lemmasOrderBy;
    }

    /**
     * Получает леммы с количеством страниц, на которых они встречаются
     *
     * @param lemmas карты лемм - частота
     * @param site объект сайта, по которому ведеться поиск
     * @return списко пар лемм-частота, частота взята из базы данных
     */
    private Map<String, Integer> getLemmasWithNumberOfPages(Map<String, Integer> lemmas, Site site) {
        long countPages = pageRepository.count();
        Optional<Integer> siteId = Optional.empty();
        if (site != null) {
            siteId = site.getId().describeConstable();
        }
        Iterator<Map.Entry<String, Integer>> entryIterator = lemmas.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, Integer> entry = entryIterator.next();
            String lemma = entry.getKey();
            Optional<Integer> frequency = lemmaRepository.findFrequencyByLemmaAndSiteId(lemma, siteId.orElse(null));
            frequency.ifPresentOrElse(f -> {
                double currentPercent = (double) f / countPages;
                if (currentPercent > percentOfTheNumberPages) {
                    entryIterator.remove();
                } else {
                    lemmas.put(lemma, f);
                }
            }, entryIterator::remove);
        }
        return lemmas;
    }

    /**
     * Получает список стрниц, содержащих все указанные
     * леммы, для заданного сайта.
     *
     * @param lemmas список лемм для поиска
     * @param site сайт, для которого осуществляется поиск
     * @return  список страниц, содержащих все леммы
     */
    private List<Page> getPagesWithAllLemmasFromQuery(List<String> lemmas, Site site) {
        Optional<Integer> siteId = Optional.empty();
        if (site != null) {
            siteId = site.getId().describeConstable();
        }
        List<Page> pagesWithAllLemmas = pageRepository.findPageByLemmaAndSiteId(lemmas.get(0), siteId.orElse(null));

        for (String lemma : lemmas.subList(1, lemmas.size())) {
            List<Integer> pagesId = pagesWithAllLemmas.stream()
                    .map(Page::getId)
                    .toList();
            pagesWithAllLemmas = pageRepository.findPageByLemmaAndPageId(lemma, pagesId);
        }
        return pagesWithAllLemmas;
    }

    /**
     * Заполняет список детализированных элементов поиска на основе
     * предоставленного контекста поиска, включая работу
     * с смещением и лимитом.
     *
     * @param data контекст для заполнения списка
     * @param offset смещение для выборки страниц
     * @param limit предел кол-ва возвращвемых страниц
     * @return список детализированных элементов поиска,
     * отсортированный и ограниченный
     */
    private List<DetailedSearchItem> fillTheListWithDetailedSearchItem(DataToFillListDetailedSearchItem data,
                                                                       int offset, int limit) {
        List<DetailedSearchItem> detailedSearchItems = new LinkedList<>();
        float maxRel = 0;
        for (Page page : data.getPagesWithAllLemmas()) {
            List<Integer> pageRanks = indexRepository.getRankByLemmasAndPageId(data.getLemmas(), page.getId());
            float absRel = pageRanks.stream().mapToInt(Integer::intValue).sum();
            if (maxRel < absRel) {
                maxRel = absRel;
            }
            DataToCreateDetailedSearchItem dataToCreate = new DataToCreateDetailedSearchItem(
                    data.getSite(), page, absRel, data.getLemmas()
            );
            DetailedSearchItem item = createdNewDetailedSearchItem(dataToCreate);
            detailedSearchItems.add(item);
        }

        sortingList(detailedSearchItems, maxRel);
        int end = Math.min(offset + limit, detailedSearchItems.size());
        return detailedSearchItems.subList(offset, end);
    }

    /**
     * Создает новый детализированный элемент поиска на
     * основе предоставленных данных.
     *
     * @param data даные необходимые для создания детализированного
     *             элемента поиска
     * @return созданный детадизированный элемент поиска
     */
    private DetailedSearchItem createdNewDetailedSearchItem(DataToCreateDetailedSearchItem data) {
        DetailedSearchItem item = new DetailedSearchItem();
        Site site = data.getSite();
        Page page = data.getPage();
        if (site == null) {
            site = siteRepository.findById(page.getSite().getId()).get();
        }
        item.setSite(site.getUrl());
        item.setSiteName(site.getName());
        item.setUri(page.getPath());
        String content = page.getContent();
        String titlePage = getTitlePage(content);
        item.setTitle(titlePage);
        item.setRelevance(data.getAbsRel());
        content = lemmaFinder.clearPageOfHtmlTags(content);
        String snippet = getSnippetByPage(content, data.getLemmas());
        item.setSnippet(snippet);
        return item;
    }

    /**
     * Извлекает заголовок страницы из содержимого HTML.
     *
     * @param content содержимое HTML страницы
     * @return заголовок страницы
     */
    private String getTitlePage(String content) {
        int start = content.indexOf("<title>");
        int end = content.indexOf("</title>");
        return content.substring(start + "<title>".length(), end);
    }

    /**
     * Возвращает сниппет страницы, выдляя найденные леммы.
     *
     * @param content содержимое страницы
     * @param lemmas список лемм для выделения
     * @return подготовленный сниппет с выделенными леммами
     */
    private String getSnippetByPage(String content, List<String> lemmas) {
        String[] words = content.split("[\\s\\-]");
        for (int i = 0; i < words.length; i++) {
            boolean matchNormForm = lemmaFinder.checkingForAMatchOfTheNormalForm(lemmas, words[i]);
            if (matchNormForm) {
                String regex = "<b>" + words[i] + "</b>";
                words[i] = regex;
            }
        }
        content = String.join(" ", words);
        String regexHighlightedWords = "((?:\\S+\\s+){0,4})((<b>.+?</b>\\s)+)";
        Pattern pattern = Pattern.compile(regexHighlightedWords);
        Matcher matcher = pattern.matcher(content);
        StringBuilder result = new StringBuilder();
        while (matcher.find() && result.length() < 256) {
            result.append(matcher.group(0)).append(". ");
        }
        return result.toString();
    }

    /**
     * Сортирует список элементов {@link DetailedSearchItem} по их
     * относительной релевантности.
     *
     * @param detailedSearchItems списко элементов для сортировки
     * @param maxRel максимальная релевантность, используемая для нормализации
     *           значений релевантности
     */
    private void sortingList(List<DetailedSearchItem> detailedSearchItems, float maxRel) {
        getRelativeRelevance(detailedSearchItems, maxRel);

        Collections.sort(detailedSearchItems, new Comparator<DetailedSearchItem>() {
            @Override
            public int compare(DetailedSearchItem o1, DetailedSearchItem o2) {
                if (o1.getRelevance() > o2.getRelevance()) {
                    return -1;
                }
                if (o1.getRelevance() < o2.getRelevance()) {
                    return 1;
                }
                return 0;
            }
        });
    }

    /**
     * Обновляет значения релевантности для каждого элемента в списке
     * {@link DetailedSearchItem},
     * нормализуя их по максимальному значению релевантности.
     *
     * @param detailedSearchItems список элементов,
     *                           для которых будет проведена нормализация
     * @param maxRel максимальная релевантность для норализации
     */
    private void getRelativeRelevance(List<DetailedSearchItem> detailedSearchItems, float maxRel) {
        for (DetailedSearchItem item : detailedSearchItems) {
            float rel = item.getRelevance();
            item.setRelevance(rel / maxRel);
        }
    }

    /**
     * Создает пустой ответ для поискового запроса.
     *
     * @return объект {@link SearchResponseNotError} с установленными
     * флагами умпеха и пустым списком данных
     */
    private SearchResponse createEmptyResponse() {
        SearchResponseNotError response = new SearchResponseNotError();
        response.setResult(true);
        response.setCount(0);
        response.setData(List.of());
        response.setStatus(HttpStatus.OK);
        return response;
    }

    /**
     * Извлекает объект {@link Site} из запроса по указанному URL
     * сайта.
     *
     * @param siteUrl URL сайта, для которого нужно получить объект
     * @return {@link Optional} содержащий объект {@link Site},
     * если он существует; иначе поустой объект
     */
    private Optional<Site> getSiteFromRequest(String siteUrl) {
        Optional<Site> site = Optional.empty();
        if (siteUrl != null) {
            site = siteRepository.findByUrl(siteUrl);
        }
        return site;
    }
}
