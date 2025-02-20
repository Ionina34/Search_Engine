package searchengine.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * Класс отвечает за извлечение лемм из текста, их нормализацию и проверку
 * соответствий нормальным формам слов. Он использует библеотеку Lucene
 * для морфологического анализа.
 */
@Component
@Slf4j
public class LemmaFinder {
    private LuceneMorphology morphology;
    private final String regax = "[^А-Яа-я\\s]";
    private final String[] particlesName = new String[]{"МЕЖД", "ПРЕД", "СОЮЗ", "ЧАСТ"};

    /**
     * Конструктор класса, инициализирует морфологический анализатор
     *
     * @throws IOException в случае, если не удается инициализировать
     *                     морфологический анализатор
     */
    public LemmaFinder() {
        try {
            morphology = new RussianLuceneMorphology();
        } catch (IOException e) {
            log.error("Error", e);
        }
    }

    /**
     * Собирает леммы из переданного текста, возвращая их частотный словарь.
     *
     * @param text исзодный текст для обработки
     * @return словарь, где ключ - лемма, значание - частота появления леммы
     * в тексте
     */
    public Map<String, Integer> collectLemmas(String text) {
        if (text.isEmpty()) {
            return Collections.emptyMap();
        }

        HashMap<String, Integer> lemmas = new HashMap<>();
        String[] wordsNormal = getArrayWordsInNormalForm(text);
        for (String word : wordsNormal) {
            if (!lemmas.containsKey(word)) {
                lemmas.put(word, 1);
            } else {
                Integer value = lemmas.get(word);
                lemmas.put(word, ++value);
            }
        }
        return lemmas;
    }

    /**
     * Получает массив нормализованных слов из текста
     *
     * @param text
     * @return
     */
    private String[] getArrayWordsInNormalForm(String text) {
        String[] words = preparingTextForProcessing(text);
        if (words.length == 0) {
            return new String[0];
        }
        return Arrays.stream(words)
                .map(morphology::getNormalForms)
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0))
                .filter(w -> {
                    String wordInfo = String.join(", ", morphology.getMorphInfo(w));
                    return Arrays.stream(particlesName)
                            .noneMatch(wordInfo::contains);
                })
                .toArray(String[]::new);
    }

    /**
     * Проверяет, совпадает ли нормализованная форма слова с какой-либо из
     * переданных лемм.
     *
     * @param lemmas список лемм для проверки
     * @param word   слово для проверки на соотвествие
     * @return true, если слово соотвествует хотя бы по одной лемме,
     * иначе false
     */
    public boolean checkingForAMatchOfTheNormalForm(List<String> lemmas, String word) {
        String[] words = preparingTextForProcessing(word);
        if (words.length == 0 || words[0].isEmpty()) {
            return false;
        }
        String normForm = morphology.getNormalForms(words[0]).get(0);
        return lemmas.stream()
                .anyMatch(normForm::contains);
    }

    /**
     * Подготовливает текст для обработки: удаляет нежелательные символы
     * и преобразует текст в массив слов.
     *
     * @param text исходный текст
     * @return массив слов после предобработки
     */
    private String[] preparingTextForProcessing(String text) {
        return text.replaceAll(regax, " ")
                .replaceAll("\\s{2,}", " ")
                .toLowerCase()
                .split("\\s");
    }

    /**
     * Удаляет HTML-теги из теста контента и возвращает очищенный текст.
     *
     * @param content исходный текст с HTML-тегами
     * @return очищенный текст без HTML-тегов
     */
    public String clearPageOfHtmlTags(String content) {
        String regax = "<[^>]+>";
        content = content.replaceAll(regax, " ")
                .replaceAll("\\s{2,}", " ")
                .replaceFirst(" ", "");
        return content;
    }
}
