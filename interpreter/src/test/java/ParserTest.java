import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ParserTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void clear() {
        System.setOut(originalOut);
    }

    @Test
    public void testParseSimpleCalculation() {
        Assertions.assertEquals(new Parser("(2+2)").runProgram().get(), 4);
    }

    @Test
    public void testParseMiddleCalculation() {
        Assertions.assertEquals(new Parser("(2+((3*4)/5))").runProgram().get(), 4);

    }

    @Test
    public void testParseIfExpression() {
        Assertions.assertEquals(new Parser("[((10+20)>(20+10))]?{1}:{0}").runProgram().get(), 0);

    }

    @Test
    public void testParseCallExpressionWithFunDefList() {
        Assertions.assertEquals(new Parser("g(x)={(f(x)+f((x/2)))}\n" +
                "f(x)={[(x>1)]?{(f((x-1))+f((x-2)))}:{x}}\n" +
                "g(10)").runProgram().get(), 60);
    }

    @Test
    public void testSyntaxError() {
        new Parser("1 + 2 + 3 + 4 + 5").runProgram();
        Assertions.assertEquals("SYNTAX ERROR\n", outContent.toString());
    }

    @Test
    public void testParameterNotFoundError() {
        new Parser("f(x)={y}\n" +
                "f(10)").runProgram();
        Assertions.assertEquals("PARAMETER NOT FOUND y:1\n", outContent.toString());
    }

    @Test
    public void testFunctionNotFoundError() {
        new Parser("g(x)={f(x)}\n" +
                "g(10)").runProgram();
        Assertions.assertEquals("FUNCTION NOT FOUND f:1\n", outContent.toString());
    }

    @Test
    public void testArgumentNumberMismatchError() {
        new Parser("g(x)={(x+1)}\n" +
                "g(10,20)").runProgram();
        Assertions.assertEquals("ARGUMENT NUMBER MISMATCH g:2\n", outContent.toString());
    }

    @Test
    public void testRuntimeError() {
        new Parser("g(a,b)={(a/b)}\n" +
                "g(10,0)").runProgram();
        Assertions.assertEquals("RUNTIME ERROR (10/0):1\n", outContent.toString());
    }
}
