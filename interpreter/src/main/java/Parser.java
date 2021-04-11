import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

public class Parser {

    private Integer currentPosition = 0;
    private final Lexer lexer;

    Parser(String text) {
        lexer = new Lexer(text);
    }

    private String acceptToken(Token token) throws InterpreterException {
        if (currentPosition >= lexer.getTokensCount()) {
            throw new InterpreterException("SYNTAX ERROR");
        }
        Entry<Token, String> entry = lexer.getTokenAndValue(currentPosition);
        if (entry.getKey().equals(token)) {
            currentPosition++;
            return entry.getValue();
        } else {
            throw new InterpreterException("SYNTAX ERROR");
        }
    }

    private boolean tryAcceptToken(Token token) {
        return lexer.getTokenAndValue(currentPosition).getKey().equals(token);
    }

    private String parseOperation() throws InterpreterException {
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

    private Identifier parseIdentifier(int line) throws InterpreterException {
        return new Identifier(line, acceptToken(Token.IDENTIFIER));
    }

    private ConstantExpression parseConstantExpression(int line) throws InterpreterException {
        if (tryAcceptToken(Token.MINUS)) {
            acceptToken(Token.MINUS);
            return new ConstantExpression(line, -Integer.parseInt(acceptToken(Token.NUMBER)));
        }
        return new ConstantExpression(line, Integer.parseInt(acceptToken(Token.NUMBER)));
    }

    private BinaryExpression parseBinaryExpression(int line) throws InterpreterException {
        acceptToken(Token.LEFT_ROUND_BRACKET);
        Expression leftExpr = parseExpression(line);
        String op = parseOperation();
        Expression rightExpr = parseExpression(line);
        acceptToken(Token.RIGHT_ROUND_BRACKET);
        return new BinaryExpression(line, leftExpr, op, rightExpr);
    }

    private ArgumentList parseArgumentList(int line) throws InterpreterException {
        ArrayList<Expression> exprList = new ArrayList<>();
        exprList.add(parseExpression(line));
        while (tryAcceptToken(Token.COMMA)) {
            acceptToken(Token.COMMA);
            exprList.add(parseExpression(line));
        }

        return new ArgumentList(line, exprList);
    }

    private CallExpression parseCallExpression(int line) throws InterpreterException {
        Identifier id = parseIdentifier(line);
        acceptToken(Token.LEFT_ROUND_BRACKET);
        ArgumentList argumentList = parseArgumentList(line);
        acceptToken(Token.RIGHT_ROUND_BRACKET);
        return new CallExpression(line, id, argumentList);
    }

    private IfExpression parseIfExpression(int line) throws InterpreterException {
        acceptToken(Token.LEFT_SQUARE_BRACKET);
        Expression condition = parseExpression(line);
        acceptToken(Token.RIGHT_SQUARE_BRACKET);
        acceptToken(Token.QUESTION);
        acceptToken(Token.LEFT_BRACE);
        Expression trueExpression = parseExpression(line);
        acceptToken(Token.RIGHT_BRACE);
        acceptToken(Token.COLON);
        acceptToken(Token.LEFT_BRACE);
        Expression falseExpression = parseExpression(line);
        acceptToken(Token.RIGHT_BRACE);

        return new IfExpression(line, condition, trueExpression, falseExpression);
    }

    private Expression parseExpression(int line) throws InterpreterException {
        if (tryAcceptToken(Token.LEFT_SQUARE_BRACKET)) {
            return parseIfExpression(line);
        }

        if (tryAcceptToken(Token.LEFT_ROUND_BRACKET)) {
            return parseBinaryExpression(line);
        }

        if (tryAcceptToken(Token.IDENTIFIER)) {
            Identifier identifier = parseIdentifier(line);
            if (tryAcceptToken(Token.LEFT_ROUND_BRACKET)) { // call
                acceptToken(Token.LEFT_ROUND_BRACKET);
                ArgumentList argumentList = parseArgumentList(line);
                acceptToken(Token.RIGHT_ROUND_BRACKET);

                return new CallExpression(line, identifier, argumentList);
            } else { // id
                return identifier;
            }

        }
        return parseConstantExpression(line);
    }

    public List<String> parseParameterList() throws InterpreterException {
        ArrayList<String> parameterList = new ArrayList<>();
        parameterList.add(acceptToken(Token.IDENTIFIER));

        while (tryAcceptToken(Token.COMMA)) {
            acceptToken(Token.COMMA);
            parameterList.add(acceptToken(Token.IDENTIFIER));
        }

        return parameterList;
    }

    private FunctionDefinition parseFunctionDefinition(int line) {
        try {
            String id = acceptToken(Token.IDENTIFIER);
            acceptToken(Token.LEFT_ROUND_BRACKET);
            List<String> parameterList = parseParameterList();
            acceptToken(Token.RIGHT_ROUND_BRACKET);
            acceptToken(Token.EQ);
            acceptToken(Token.LEFT_BRACE);
            Expression expression = parseExpression(line);
            acceptToken(Token.RIGHT_BRACE);

            return new FunctionDefinition(id, parameterList, expression);
        } catch (Exception e) {
            return null;
        }
    }

    private List<FunctionDefinition> parseFunctionDefinitionList() throws InterpreterException {
        ArrayList<FunctionDefinition> functionDefinitionList = new ArrayList<>();

        int currentLine = 1;
        while (tryAcceptToken(Token.IDENTIFIER)) {
            int snapshot = currentPosition;
            FunctionDefinition functionDefinition = parseFunctionDefinition(currentLine);

            if (functionDefinition != null) {
                functionDefinitionList.add(functionDefinition);
                acceptToken(Token.EOL);
                currentLine++;
            } else {
                currentPosition = snapshot;
                break;
            }
        }

        return functionDefinitionList;
    }

    private Integer parseProgram() throws InterpreterException {
        if (!lexer.lexAll()) {
            throw new InterpreterException("SYNTAX ERROR");
        }

        List<FunctionDefinition> functionDefinitionList = parseFunctionDefinitionList();

        HashMap<String, FunctionDefinition> functions = new HashMap<>();
        for (FunctionDefinition function : functionDefinitionList) {
            functions.put(function.getId(), function);
        }
        Program.functions = functions;

        Expression expression = parseExpression(Program.functions.size() + 1);

        if (currentPosition != lexer.getTokensCount()) {
            throw new InterpreterException("SYNTAX ERROR");
        }

        return expression.execute(null);
    }

    public Optional<Integer> runProgram() {
        try {
            return Optional.of(parseProgram());
        } catch (InterpreterException interpreterException) {
            System.out.println(interpreterException.getMessage());
            return Optional.empty();
        }
    }

    public static void main(String[] args) {
        Parser parser = new Parser("(2+2)");
        Optional<Integer> result = parser.runProgram();
        if (result.isPresent())
            System.out.println(result);
    }
}
