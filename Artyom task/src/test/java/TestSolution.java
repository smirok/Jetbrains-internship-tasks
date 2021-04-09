import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class TestSolution {

    private final int STRING_LENGTH = 100;
    private Solution solution = new Solution();
    private String[] buffer = null;

    static class StringComparator implements Comparator<String> {
        private HashMap<Character, Integer> alphabetOrder = new HashMap<>();

        StringComparator(String alphabet) {
            for (int i = 0; i < Solution.ALPHABET_SIZE; i++) {
                alphabetOrder.put(alphabet.charAt(i), i);
            }
        }

        @Override
        public int compare(String that, String other) {
            int indexThat = 0;
            int indexOther = 0;
            while (indexThat < that.length() && indexOther < other.length()) {
                if (alphabetOrder.get(that.charAt(indexThat)) < alphabetOrder.get(other.charAt(indexOther))) {
                    return 1;
                }
                if (alphabetOrder.get(that.charAt(indexThat)) > alphabetOrder.get(other.charAt(indexOther))) {
                    return -1;
                }

                indexThat++;
                indexOther++;
            }
            if (indexThat == that.length() && indexOther == other.length()) {
                return 0;
            }
            if (indexThat == that.length()) {
                return 1;
            }
            return -1;
        }
    }

    private InputStream generateInputStreamWithRandomStrings(int bufferSize) {
        Random random = new Random();
        buffer = new String[bufferSize];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(bufferSize)
                .append('\n');

        for (int i = 0; i < bufferSize; i++) {
            StringBuilder localStringBuilder = new StringBuilder();
            for (int j = 0; j < STRING_LENGTH; j++) {
                localStringBuilder.append((char) (random.nextInt(Solution.ALPHABET_SIZE) + 'a'));
            }
            localStringBuilder.append('\n');
            String name = localStringBuilder.toString();
            buffer[i] = name;
            stringBuilder.append(name);
        }

        return new ByteArrayInputStream(stringBuilder.toString().getBytes());
    }

    @BeforeEach
    public void before() {
        solution = new Solution();
    }

    @Test
    public void testSmall() throws IOException {
        for (int i = 0; i < 10000; i++) {
            int n = 10;
            Optional<String> alphabet = Optional.empty();
            while (alphabet.isEmpty()) {
                alphabet = solution.solve(generateInputStreamWithRandomStrings(n));
                solution = new Solution();
            }

            String[] sortedByCmp = new String[buffer.length];
            Arrays.stream(buffer)
                    .sorted(new StringComparator(alphabet.get()).reversed())
                    .collect(Collectors.toList())
                    .toArray(sortedByCmp);

            Assertions.assertTrue(Arrays.equals(buffer, sortedByCmp));
        }
    }

    @Test
    public void testMiddle() throws IOException {
        for (int i = 0; i < 10; i++) {
            int n = 20;
            Optional<String> alphabet = Optional.empty();
            while (alphabet.isEmpty()) {
                alphabet = solution.solve(generateInputStreamWithRandomStrings(n));
                solution = new Solution();
            }

            String[] sortedByCmp = new String[buffer.length];
            Arrays.stream(buffer)
                    .sorted(new StringComparator(alphabet.get()).reversed())
                    .collect(Collectors.toList())
                    .toArray(sortedByCmp);

            Assertions.assertTrue(Arrays.equals(buffer, sortedByCmp));
        }
    }
}
