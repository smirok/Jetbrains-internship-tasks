import java.util.HashMap;
import java.util.List;

public interface Expression {
    Integer execute(HashMap<String, Integer> args);
}

class Identifier implements Expression {
    private final String id;

    Identifier(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public Integer execute(HashMap<String, Integer> args) {
        if (!args.containsKey(id)) {
            System.out.println("PARAMETER NOT FOUND " + id + ":<line>\n");
            System.exit(0);
        }
        return args.get(id);
    }
}

class ConstantExpression implements Expression {
    private final Integer number;

    ConstantExpression(Integer number) {
        this.number = number;
    }

    @Override
    public Integer execute(HashMap<String, Integer> unused) {
        return number;
    }
}

class BinaryExpression implements Expression {
    private final Expression leftExpression;
    private final Expression rightExpression;
    private final String operation;

    BinaryExpression(Expression leftExpression, String operation, Expression rightExpression) {
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
                return leftExpression.execute(args) / rightExpression.execute(args);
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

class ArgumentList implements Expression {
    private final List<Expression> args;

    public List<Expression> getArgs() {
        return args;
    }

    ArgumentList(List<Expression> args) {
        this.args = args;
    }

    @Override
    public Integer execute(HashMap<String, Integer> unused) {
        return 0;
    }
}

class CallExpression implements Expression {
    private final Identifier identifier;
    private final ArgumentList argumentList;

    CallExpression(Identifier identifier, ArgumentList argumentList) {
        this.identifier = identifier;
        this.argumentList = argumentList;
    }

    @Override
    public Integer execute(HashMap<String, Integer> args) {
        if (!Program.functions.containsKey(identifier.getId())) {
            System.out.println("FUNCTION NOT FOUND " + identifier.getId() + ":<line>\n");
            System.exit(0);
        }
        FunctionDefinition function = Program.functions.get(identifier.getId());

        HashMap<String, Integer> newArgs = new HashMap<>();

        if (argumentList.getArgs().size() != function.parameterList.size()) {
            System.out.println("ARGUMENT NUMBER MISMATCH " + identifier.getId() + ":<line>\n");
            System.exit(0);
        }

        for (int i = 0; i < function.parameterList.size(); i++) {
            newArgs.put(function.parameterList.get(i), argumentList.getArgs().get(i).execute(args));
        }
        return function.body.execute(newArgs);
    }
}

class IfExpression implements Expression {
    private final Expression condition;
    private final Expression trueExpression;
    private final Expression falseExpression;

    IfExpression(Expression condition, Expression trueExpression, Expression falseExpression) {
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