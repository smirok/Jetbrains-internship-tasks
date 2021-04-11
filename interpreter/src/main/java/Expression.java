import java.util.HashMap;
import java.util.List;

public abstract class Expression {
    int line = 0;

    abstract Integer execute(HashMap<String, Integer> args);
}

class Identifier extends Expression {
    private final String id;

    Identifier(int line, String id) {
        this.line = line;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public Integer execute(HashMap<String, Integer> args) {
        if (!args.containsKey(id)) {
            System.out.println("PARAMETER NOT FOUND " + id + ":" + line + "\n");
            System.exit(0);
        }
        return args.get(id);
    }
}

class ConstantExpression extends Expression {
    private final Integer number;

    ConstantExpression(int line, Integer number) {
        this.line = line;
        this.number = number;
    }

    @Override
    public Integer execute(HashMap<String, Integer> unused) {
        return number;
    }
}

class BinaryExpression extends Expression {
    private final Expression leftExpression;
    private final Expression rightExpression;
    private final String operation;

    BinaryExpression(int line, Expression leftExpression, String operation, Expression rightExpression) {
        this.line = line;
        this.leftExpression = leftExpression;
        this.operation = operation;
        this.rightExpression = rightExpression;
    }

    @Override
    public Integer execute(HashMap<String, Integer> args) {
        switch (operation) {
            case ("+"):
                return leftExpression.execute(args) + rightExpression.execute(args);
            case ("-"):
                return leftExpression.execute(args) - rightExpression.execute(args);
            case ("*"):
                return leftExpression.execute(args) * rightExpression.execute(args);
            case ("/"):
                Integer dividend = 0, divisor = 0;
                try {
                    dividend = leftExpression.execute(args);
                    divisor = rightExpression.execute(args);
                    return dividend / divisor;
                } catch (RuntimeException runtimeException) {
                    System.out.println("RUNTIME ERROR (" + dividend + "/" + divisor + "):" + line);
                    System.exit(0);
                }
            case ("%"):
                return leftExpression.execute(args) % rightExpression.execute(args);
            case ("<"):
                return leftExpression.execute(args) < rightExpression.execute(args) ? 1 : 0;
            case (">"):
                return leftExpression.execute(args) > rightExpression.execute(args) ? 1 : 0;
            case ("="):
                return leftExpression.execute(args).equals(rightExpression.execute(args)) ? 1 : 0;
        }
        return 0;
    }
}

class ArgumentList extends Expression {
    private final List<Expression> args;

    public List<Expression> getArgs() {
        return args;
    }

    ArgumentList(int line, List<Expression> args) {
        this.line = line;
        this.args = args;
    }

    @Override
    public Integer execute(HashMap<String, Integer> unused) {
        return 0;
    }
}

class CallExpression extends Expression {
    private final Identifier identifier;
    private final ArgumentList argumentList;

    CallExpression(int line, Identifier identifier, ArgumentList argumentList) {
        this.line = line;
        this.identifier = identifier;
        this.argumentList = argumentList;
    }

    @Override
    public Integer execute(HashMap<String, Integer> args) {
        if (!Program.functions.containsKey(identifier.getId())) {
            System.out.println("FUNCTION NOT FOUND " + identifier.getId() + ":" + line + "\n");
            System.exit(0);
        }
        FunctionDefinition function = Program.functions.get(identifier.getId());

        HashMap<String, Integer> newArgs = new HashMap<>();

        if (argumentList.getArgs().size() != function.getParameterList().size()) {
            System.out.println("ARGUMENT NUMBER MISMATCH " + identifier.getId() + ":" + line + "\n");
            System.exit(0);
        }

        for (int i = 0; i < function.getParameterList().size(); i++) {
            newArgs.put(function.getParameterList().get(i), argumentList.getArgs().get(i).execute(args));
        }
        return function.getBody().execute(newArgs);
    }
}

class IfExpression extends Expression {
    private final Expression condition;
    private final Expression trueExpression;
    private final Expression falseExpression;

    IfExpression(int line, Expression condition, Expression trueExpression, Expression falseExpression) {
        this.line = line;
        this.condition = condition;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
    }

    @Override
    public Integer execute(HashMap<String, Integer> args) {
        if (condition.execute(args).equals(1)) {
            return trueExpression.execute(args);
        } else {
            return falseExpression.execute(args);
        }
    }
}