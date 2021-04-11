import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Parser {

    private Integer currentPosition = 0;
    private final Lexer lexer;

    Parser(String text) {
        lexer = new Lexer(text);

        if (!lexer.lexAll()) {
            System.out.println("LEX ERROR");
            System.exit(0);
        }
    }

    private String acceptToken(Token token) {
        Entry<Token, String> entry = lexer.getTokenAndValue(currentPosition);
        if (entry.getKey().equals(token)) {
            currentPosition++;
            return entry.getValue();
        } else {
            throw new IllegalStateException();
        }
    }

    private boolean tryAcceptToken(Token token) {
        return lexer.getTokenAndValue(currentPosition).getKey().equals(token);
    }

    private String parseOperation() {
        if (tryAcceptToken(Token.PLUS)) {
            return acceptToken(Token.PLUS);
        }

        if (tryAcceptToken(Token.MINUS)) {
            return acceptToken(Token.MINUS);
        }

        if (tryAcceptToken(Token.MULT)) {
            return acceptToken(Token.MULT);
        }

        if (tryAcceptToken(Token.DIVIDE)) {
            return acceptToken(Token.DIVIDE);
        }

        if (tryAcceptToken(Token.MODULE)) {
            return acceptToken(Token.MODULE);
        }

        if (tryAcceptToken(Token.GT)) {
            return acceptToken(Token.GT);
        }

        if (tryAcceptToken(Token.LT)) {
            return acceptToken(Token.LT);
        }

        if (tryAcceptToken(Token.EQ)) {
            return acceptToken(Token.EQ);
        }

        return null;
    }

    private Identifier parseIdentifier() {
        return new Identifier(acceptToken(Token.IDENTIFIER));
    }

    private ConstantExpression parseConstantExpression() {
        if (tryAcceptToken(Token.MINUS)) {
            acceptToken(Token.MINUS);
            return new ConstantExpression(-Integer.parseInt(acceptToken(Token.NUMBER)));
        }
        return new ConstantExpression(Integer.parseInt(acceptToken(Token.NUMBER)));
    }

    private BinaryExpression parseBinaryExpression() {
        acceptToken(Token.LEFT_ROUND_BRACKET);
        Expression leftExpr = parseExpression();
        String op = parseOperation();
        Expression rightExpr = parseExpression();
        acceptToken(Token.RIGHT_ROUND_BRACKET);
        return new BinaryExpression(leftExpr, op, rightExpr);
    }

    private ArgumentList parseArgumentList() {
        ArrayList<Expression> exprList = new ArrayList<>();
        exprList.add(parseExpression());
        while (tryAcceptToken(Token.COMMA)) {
            acceptToken(Token.COMMA);
            exprList.add(parseExpression());
        }

        return new ArgumentList(exprList);
    }

    private CallExpression parseCallExpression() {
        Identifier id = parseIdentifier();
        acceptToken(Token.LEFT_ROUND_BRACKET);
        ArgumentList argumentList = parseArgumentList();
        acceptToken(Token.RIGHT_ROUND_BRACKET);
        return new CallExpression(id, argumentList);
    }

    private IfExpression parseIfExpression() {
        acceptToken(Token.LEFT_SQUARE_BRACKET);
        Expression condition = parseExpression();
        acceptToken(Token.RIGHT_SQUARE_BRACKET);
        acceptToken(Token.QUESTION);
        acceptToken(Token.LEFT_BRACE);
        Expression trueExpression = parseExpression();
        acceptToken(Token.RIGHT_BRACE);
        acceptToken(Token.COLON);
        acceptToken(Token.LEFT_BRACE);
        Expression falseExpression = parseExpression();
        acceptToken(Token.RIGHT_BRACE);

        return new IfExpression(condition, trueExpression, falseExpression);
    }

    private Expression parseExpression() {
        if (tryAcceptToken(Token.LEFT_SQUARE_BRACKET)) {
            return parseIfExpression();
        }

        if (tryAcceptToken(Token.LEFT_ROUND_BRACKET)) {
            return parseBinaryExpression();
        }

        if (tryAcceptToken(Token.IDENTIFIER)) {
            Identifier identifier = parseIdentifier();
            if (tryAcceptToken(Token.LEFT_ROUND_BRACKET)) { // call
                acceptToken(Token.LEFT_ROUND_BRACKET);
                ArgumentList argumentList = parseArgumentList();
                acceptToken(Token.RIGHT_ROUND_BRACKET);

                return new CallExpression(identifier, argumentList);
            } else { // id
                return identifier;
            }

        }
        return parseConstantExpression();
    }

    public List<String> parseParameterList() {
        ArrayList<String> parameterList = new ArrayList<>();
        parameterList.add(acceptToken(Token.IDENTIFIER));

        while (tryAcceptToken(Token.COMMA)) {
            acceptToken(Token.COMMA);
            parameterList.add(acceptToken(Token.IDENTIFIER));
        }

        return parameterList;
    }

    private FunctionDefinition parseFunctionDefinition() {
        try {
            String id = acceptToken(Token.IDENTIFIER);
            acceptToken(Token.LEFT_ROUND_BRACKET);
            List<String> parameterList = parseParameterList();
            acceptToken(Token.RIGHT_ROUND_BRACKET);
            acceptToken(Token.EQ);
            acceptToken(Token.LEFT_BRACE);
            Expression expression = parseExpression();
            acceptToken(Token.RIGHT_BRACE);

            return new FunctionDefinition(id, parameterList, expression);
        } catch (Exception e) {
            return null;
        }
    }

    private List<FunctionDefinition> parseFunctionDefinitionList() {
        ArrayList<FunctionDefinition> functionDefinitionList = new ArrayList<>();

        while (tryAcceptToken(Token.IDENTIFIER)) {
            int shapshot = currentPosition;
            FunctionDefinition functionDefinition = parseFunctionDefinition();

            if (functionDefinition != null) {
                functionDefinitionList.add(functionDefinition);
                acceptToken(Token.EOL);
            } else {
                currentPosition = shapshot;
                break;
            }
        }

        return functionDefinitionList;
    }

    public Integer parseProgram() {
        List<FunctionDefinition> functionDefinitionList = null;

        try {
            functionDefinitionList = parseFunctionDefinitionList();
        } catch (Exception e) {
            System.out.println("SYNTAX ERROR");
            System.exit(0);
        }

        HashMap<String, FunctionDefinition> functions = new HashMap<>();
        for (var elem : functionDefinitionList) {
            functions.put(elem.id, elem);
        }
        Program.functions = functions;

        Expression expression = null;
        try {
            expression = parseExpression();
        } catch (Exception e) {
            System.out.println("SYNTAX ERROR");
            System.exit(0);
        }

        try {
            return expression.execute(null);
        } catch (RuntimeException runtimeException) {
            System.out.println("RUNTIME ERROR");
            System.exit(0);
        }

        return null;
    }

    public static void main(String[] args) {
        Parser parser = new Parser("g(a,b)={(a/b)}\n" +
                "g(10,0)");
        System.out.println(parser.parseProgram());
    }
}
