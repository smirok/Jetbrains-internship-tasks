import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.util.Optional;

public class Lexer {

    public String text;
    private int currentPosition = 0;

    public Lexer(String text) {
        this.text = text;
    }

    private final ArrayList<Entry<Token, String>> tokens = new ArrayList<>();

    public int getTokensCount() {
        return tokens.size();
    }

    public Entry<Token, String> findNextToken() {
        for (Token token : Token.values()) {
            Optional<Integer> optionalEnd = token.indexAfterMatch(text, currentPosition);

            if (optionalEnd.isPresent()) {
                Entry<Token, String> result = new SimpleEntry<>(
                        token, text.substring(currentPosition, optionalEnd.get())
                );
                currentPosition = optionalEnd.get();
                tokens.add(result);
                return result;
            }
        }

        return null;
    }

    public boolean lexAll() {
        while (currentPosition < text.length()) {
            if (findNextToken() == null) {
                return false;
            }
        }

        return true;
    }

    public Entry<Token, String> getTokenAndValue(int index) {
        return tokens.get(index);
    }
}
