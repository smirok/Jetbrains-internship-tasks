import java.util.ArrayList;
import java.util.Optional;

public class Lexer {

    public String text;
    private int currentPosition = 0;

    public Lexer(String text) {
        this.text = text;
    }

    private ArrayList<Token> tokens = new ArrayList<>();

    public Token findNextToken() {
        for (Token token : Token.values()) {
            Optional<Integer> optionalEnd = token.indexAfterMatch(text, currentPosition);

            if (optionalEnd.isPresent()) {
                token.setValue(text.substring(currentPosition, optionalEnd.get()));
                tokens.add(token);
                currentPosition = optionalEnd.get();
                return token;
            }
        }
        return null;
    }

    public void lexAll() {
        while (currentPosition < text.length()) {
            findNextToken();
        }
    }

    public Token getToken(int index) {
        return tokens.get(index);
    }
}
