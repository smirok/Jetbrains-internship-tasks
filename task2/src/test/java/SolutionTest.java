import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class SolutionTest {
    private final int STRING_LENGTH = 100;
    private final int ALPHABET_SIZE = 26;

    @Test
    public void SimpleTest() {
        Assertions.assertTrue(Solution.matches("helloWorld", "\\w+"));
    }

    @Test
    public void EvilRegexTest() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 100_000; i++) {
            stringBuilder.append('a');
        }
        Assertions.assertFalse(Solution.matches(stringBuilder.toString(), "(a|aa)+"));
    }

    @Test
    public void stressTest() {
        String regexp = "\\d+";

        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < STRING_LENGTH; j++) {
                stringBuilder.append((char) (random.nextInt(ALPHABET_SIZE) + 'a'));
            }
            Assertions.assertFalse(Solution.matches(stringBuilder.toString(), regexp));
        }
    }
}
