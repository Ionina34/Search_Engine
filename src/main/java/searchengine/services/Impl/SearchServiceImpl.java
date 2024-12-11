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

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final LemmaRepository lemmaRepository;
    private final PageRepository pageRepository;
    private final SiteRepository siteRepository;
    private final IndexRepository indexRepository;

    private final LemmaFinder lemmaFinder = new LemmaFinder();
    private final double percentOfTheNumberPages = 0.8;

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

        Map<String, Integer> lemmasInQuery = lemmaFinder.collectLemmas(searchData.getQuery());
        List<Map.Entry<String, Integer>> sortedLemmas = getListLemmasInAscendingOrder(lemmasInQuery);

        List<String> lemmas = sortedLemmas.stream()
                .map(Map.Entry::getKey)
                .toList();
        if (lemmas.isEmpty()) {
            return createEmptyResponse();
        }
        List<Page> pagesWithAllLemmas = getPagesWithAllLemmasFromQuery(lemmas);
        if (pagesWithAllLemmas.isEmpty()) {
            return createEmptyResponse();
        }

        DataToFillListDetailedSearchItem data = new DataToFillListDetailedSearchItem(
                searchData.getSite(), pagesWithAllLemmas, lemmas
        );
        List<DetailedSearchItem> detailedSearchItems = fillTheListWithDetailedSearchItem(data);

        response.setResult(true);
        response.setCount(pagesWithAllLemmas.size());
        response.setStatus(HttpStatus.OK);
        response.setData(detailedSearchItems);
        return response;
    }

    private List<Map.Entry<String, Integer>> getListLemmasInAscendingOrder(Map<String, Integer> lemmasInQuery) {
        Map<String, Integer> lemmasInQueryWithFrequency = getLemmasWithNumberOfPages(lemmasInQuery);

        List<Map.Entry<String, Integer>> lemmasOrderBy = new ArrayList<>(lemmasInQueryWithFrequency.entrySet());
        lemmasOrderBy.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        return lemmasOrderBy;
    }

    private Map<String, Integer> getLemmasWithNumberOfPages(Map<String, Integer> lemmas) {
        long countPages = pageRepository.count();
        Iterator<Map.Entry<String, Integer>> entryIterator = lemmas.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, Integer> entry = entryIterator.next();
            String lemma = entry.getKey();
            Optional<Integer> frequency = lemmaRepository.findFrequencyByLemma(lemma);
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

    private List<Page> getPagesWithAllLemmasFromQuery(List<String> lemmas) {
        List<Page> pagesWithAllLemmas = pageRepository.findPageByLemma(lemmas.get(0));

        for (String lemma : lemmas.subList(1, lemmas.size())) {
            List<Integer> pagesId = pagesWithAllLemmas.stream()
                    .map(Page::getId)
                    .toList();
            pagesWithAllLemmas = pageRepository.findPageByLemmaAndPageId(lemma, pagesId);
        }
        return pagesWithAllLemmas;
    }

    private List<DetailedSearchItem> fillTheListWithDetailedSearchItem(DataToFillListDetailedSearchItem data) {
        List<DetailedSearchItem> detailedSearchItems = new LinkedList<>();
        Optional<Site> siteConst = Optional.empty();
        Optional<Integer> siteId = Optional.empty();
        if (data.getSite() != null) {
            siteConst = Optional.of(siteRepository.getSiteByPath(data.getSite()));
            siteId = siteConst.get().getId().describeConstable();
        }
        float maxRel = 0;
        for (Page page : data.getPagesWithAllLemmas()) {
            List<Integer> pageRanks = indexRepository.getRankByLemmasAndPageIdAndSiteId(data.getLemmas(), page.getId(), siteId.orElse(null));
            float absRel = pageRanks.stream().mapToInt(Integer::intValue).sum();
            if (maxRel < absRel) {
                maxRel = absRel;
            }
            DataToCreateDetailedSearchItem dataToCreate = new DataToCreateDetailedSearchItem(
                    siteConst.orElse(null), page, absRel, data.getLemmas()
            );
            DetailedSearchItem item = createdNewDetailedSearchItem(dataToCreate);
            detailedSearchItems.add(item);
        }

        sortingList(detailedSearchItems, maxRel);
        return detailedSearchItems;
    }

    private DetailedSearchItem createdNewDetailedSearchItem(DataToCreateDetailedSearchItem data) {
        DetailedSearchItem item = new DetailedSearchItem();
        Site site = data.getSite();
        Page page = data.getPage();
        if (site != null) {
            item.setSite(site.getUrl());
            item.setSiteName(site.getName());
        } else {
            site = siteRepository.findById(page.getSite().getId()).get();
            item.setSite(site.getUrl());
            item.setSiteName(site.getName());
        }
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

    private String getTitlePage(String content) {
        int start = content.indexOf("<title>");
        int end = content.indexOf("</title>");
        return content.substring(start + "<title>".length(), end);
    }

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

    private void getRelativeRelevance(List<DetailedSearchItem> detailedSearchItems, float maxRel) {

        for (DetailedSearchItem item : detailedSearchItems) {
            float rel = item.getRelevance();
            item.setRelevance(rel / maxRel);
        }
    }

    private SearchResponse createEmptyResponse() {
        SearchResponseNotError response = new SearchResponseNotError();
        response.setResult(true);
        response.setCount(0);
        response.setData(List.of());
        response.setStatus(HttpStatus.OK);
        return response;
    }
}
