import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Token {
    NUMBER("\\d+"),
    IDENTIFIER("[0-9a-zA-Z_]+"),
    PLUS("\\+"),
    MINUS("-"),
    MULT("\\*"),
    DIVIDE("/"),
    MODULE("%"),
    LT("<"),
    GT(">"),
    EQ("="),
    LEFT_ROUND_BRACKET("\\("),
    RIGHT_ROUND_BRACKET("\\)"),
    COMMA(","),
    LEFT_SQUARE_BRACKET("\\["),
    RIGHT_SQUARE_BRACKET("\\]"),
    QUESTION("\\?"),
    COLON(":"),
    LEFT_BRACE("\\{"),
    RIGHT_BRACE("\\}"),
    EOL("\\n");

    private final String str;

    Token(String str) {
        this.str = str;
    }

    public Optional<Integer> indexAfterMatch(String s, int pos) {
        Matcher matcher = Pattern.compile("^" + str).matcher(s);
        return matcher.region(pos, s.length()).find() ? Optional.of(matcher.end()) : Optional.empty();
    }
}
