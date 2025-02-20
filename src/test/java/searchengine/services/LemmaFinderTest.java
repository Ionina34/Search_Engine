package searchengine.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LemmaFinderTest {
    private LemmaFinder lemmaFinder;

    @BeforeEach
    public void setUp() {
        lemmaFinder = new LemmaFinder();
    }

    @Test
    @DisplayName("Empty text")
    public void testCollectLemmas_EmptyText_ReturnsEmptyList(){
        Map<String, Integer> result = lemmaFinder.collectLemmas("");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Single word")
    public void testCollectLemmas_SingleWord_ReturnMap(){
        Map<String,Integer> result=lemmaFinder.collectLemmas("кот");
        assertEquals(1,result.size());
        assertEquals(1,result.get("кот").intValue());
    }

    @Test
    @DisplayName("Multiple words")
    public void testCollectLemmas_MultipleWords_ReturnsMap(){
        Map<String,Integer> result=lemmaFinder.collectLemmas("кот кот собака кот птица");
        assertEquals(3, result.size());
        assertEquals(3,result.get("кот").intValue());
        assertEquals(1,result.get("собака").intValue());
        assertEquals(1,result.get("птица").intValue());
    }

    @Test
    @DisplayName("Multiple words with symbols")
    public void testCollectLemmas_MultipleWordsWithSymbols_ReturnsMap(){
        Map<String,Integer> result=lemmaFinder.collectLemmas("кот кот, собака кот птица. ?:6");
        assertEquals(3, result.size());
        assertEquals(3,result.get("кот").intValue());
        assertEquals(1,result.get("собака").intValue());
        assertEquals(1,result.get("птица").intValue());
    }

    @Test
    @DisplayName("Mismatch of the word with the normal form")
    public void testCheckingForAMatchOfTheNormalForm_NoMath_ReturnsFalse(){
        List<String> lemmas= List.of("кот", "собака");
        boolean result= lemmaFinder.checkingForAMatchOfTheNormalForm(lemmas,"птица");
        assertFalse(result);
    }

    @Test
    @DisplayName("Matching a word with the normal form")
    public void testCheckingForAMatchOfTheNormalForm_AMath_ReturnsTrue(){
        List<String> lemmas= List.of("кот", "собака");
        boolean result= lemmaFinder.checkingForAMatchOfTheNormalForm(lemmas,"кот");
        assertTrue(result);
    }

    @Test
    @DisplayName("Cleaning page of Html tage")
    void testClearPageOfHtmlTags_Text_ReturnsTextWithoutTags() {
        String input = "<html><body>Тестовый текст</body></html>";
        String result = lemmaFinder.clearPageOfHtmlTags(input);
        assertEquals("Тестовый текст", result.trim(), "Should remove HTML tags");
    }
}
