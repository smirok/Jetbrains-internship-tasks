import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LexerTest {

    @Test
    public void testLexSimple() {
        Lexer lexer = new Lexer("(2+2)");
        Assertions.assertEquals(lexer.findNextToken(), Token.LEFT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken(), Token.PLUS);
        Assertions.assertEquals(lexer.findNextToken(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken(), Token.RIGHT_ROUND_BRACKET);
    }

    @Test
    public void testLexMiddle() {
        Lexer lexer = new Lexer("(2+((3*4)/5))");
        Assertions.assertEquals(lexer.findNextToken(), Token.LEFT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken(), Token.PLUS);
        Assertions.assertEquals(lexer.findNextToken(), Token.LEFT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken(), Token.LEFT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken(), Token.MULT);
        Assertions.assertEquals(lexer.findNextToken(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken(), Token.RIGHT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken(), Token.DIVIDE);
        Assertions.assertEquals(lexer.findNextToken(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken(), Token.RIGHT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken(), Token.RIGHT_ROUND_BRACKET);
    }

    @Test
    public void testLexHard() {
        Lexer lexer = new Lexer("[((10+20)>(20+10))]?{1}:{0}");
        Assertions.assertEquals(lexer.findNextToken(), Token.LEFT_SQUARE_BRACKET);
        Assertions.assertEquals(lexer.findNextToken(), Token.LEFT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken(), Token.LEFT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken(), Token.PLUS);
        Assertions.assertEquals(lexer.findNextToken(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken(), Token.RIGHT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken(), Token.GT);
        Assertions.assertEquals(lexer.findNextToken(), Token.LEFT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken(), Token.PLUS);
        Assertions.assertEquals(lexer.findNextToken(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken(), Token.RIGHT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken(), Token.RIGHT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken(), Token.RIGHT_SQUARE_BRACKET);
        Assertions.assertEquals(lexer.findNextToken(), Token.QUESTION);
        Assertions.assertEquals(lexer.findNextToken(), Token.LEFT_BRACE);
        Assertions.assertEquals(lexer.findNextToken(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken(), Token.RIGHT_BRACE);
        Assertions.assertEquals(lexer.findNextToken(), Token.COLON);
        Assertions.assertEquals(lexer.findNextToken(), Token.LEFT_BRACE);
        Assertions.assertEquals(lexer.findNextToken(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken(), Token.RIGHT_BRACE);
    }
}
