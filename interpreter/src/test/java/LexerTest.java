import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LexerTest {

    @Test
    public void testLexSimple() {
        Lexer lexer = new Lexer("(2+2)");
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.LEFT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.PLUS);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.RIGHT_ROUND_BRACKET);
    }

    @Test
    public void testLexMiddle() {
        Lexer lexer = new Lexer("(2+((3*4)/5))");
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.LEFT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.PLUS);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.LEFT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.LEFT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.MULT);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.RIGHT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.DIVIDE);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.RIGHT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.RIGHT_ROUND_BRACKET);
    }

    @Test
    public void testLexHard() {
        Lexer lexer = new Lexer("[((10+20)>(20+10))]?{1}:{0}");
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.LEFT_SQUARE_BRACKET);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.LEFT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.LEFT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.PLUS);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.RIGHT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.GT);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.LEFT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.PLUS);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.RIGHT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.RIGHT_ROUND_BRACKET);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.RIGHT_SQUARE_BRACKET);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.QUESTION);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.LEFT_BRACE);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.RIGHT_BRACE);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.COLON);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.LEFT_BRACE);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.NUMBER);
        Assertions.assertEquals(lexer.findNextToken().getKey(), Token.RIGHT_BRACE);
    }

    @Test
    public void testLexError() {
        Assertions.assertFalse(new Lexer("hello world@").lexAll());
    }
}
