package lesson_3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShortPhraseAssertionTest {

    @Test
    public void shortPhraseAssertionTest() {
        String phrase = "London is the capital of Great Britain";
        int actualPhraseLength = phrase.length();
        int expectedPhraseLength = 15;

        assertTrue(actualPhraseLength > expectedPhraseLength, "Phrase length <= 15");
    }
}
