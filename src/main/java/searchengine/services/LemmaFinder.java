package searchengine.services;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.*;

public class LemmaFinder {
    private LuceneMorphology morphology;
    private final String regax = "[^А-Яа-я\\s]";
    private final String[] particlesName = new String[]{"МЕЖД", "ПРЕД", "СОЮЗ", "ЧАСТ"};

    public LemmaFinder() {
        try {
            morphology = new RussianLuceneMorphology();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

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

    private String[] getArrayWordsInNormalForm(String text) {
        String[] words = preparingTextForProcessing(text);
        if(words.length==0){
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

    public boolean checkingForAMatchOfTheNormalForm(List<String> lemmas, String word){
        String[] words = preparingTextForProcessing(word);
        if(words.length==0 || words[0].isEmpty()){
            return false;
        }
        String normForm = morphology.getNormalForms(words[0]).get(0);
        return lemmas.stream()
                .anyMatch(normForm::contains);
    }

    private String[] preparingTextForProcessing(String text) {
        return text.replaceAll(regax, " ")
                .replaceAll("\\s{2,}", " ")
                .toLowerCase()
                .split("\\s");
    }

    public String clearPageOfHtmlTags(String content) {
        String regax = "<[^>]+>";
        content = content.replaceAll(regax, " ")
                .replaceAll("\\s{2,}", " ")
                .replaceFirst(" ","");
        return content;
    }
}
