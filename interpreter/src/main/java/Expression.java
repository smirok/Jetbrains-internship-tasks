import java.util.HashMap;
import java.util.List;

public interface Expression {
    Integer execute(HashMap<String, Expression> args);
}

class Identifier implements Expression {
    private String id;

    Identifier(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public Integer execute(HashMap<String, Expression> args) {
        return 0;
    }
}

class ConstantExpression implements Expression {
    private Integer number;

    ConstantExpression(Integer number) {
        this.number = number;
    }

    @Override
    public Integer execute(HashMap<String, Expression> args) {
        return number;
    }
}

class BinaryExpression implements Expression {
    private Expression leftExpression, rightExpression;
    private String operation;

    BinaryExpression(Expression leftExpression, String operation, Expression rightExpression) {
        this.leftExpression = leftExpression;
        this.operation = operation;
        this.rightExpression = rightExpression;
    }

    @Override
    public Integer execute(HashMap<String, Expression> args) {
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
    private List<Expression> args;

    public List<Expression> getArgs() {
        return args;
    }

    ArgumentList(List<Expression> args) {
        this.args = args;
    }

    @Override
    public Integer execute(HashMap<String, Expression> args) {
        return 0;
    }
}

class CallExpression implements Expression {
    private Identifier identifier;
    private ArgumentList argumentList;

    CallExpression(Identifier identifier, ArgumentList argumentList) {
        this.identifier = identifier;
        this.argumentList = argumentList;
    }

    @Override
    public Integer execute(HashMap<String, Expression> args) {
        FunctionDefinition function = Program.functions.get(identifier.getId());

        HashMap<String, Expression> newArgs = new HashMap<>();
        for (int i = 0; i < function.parameterList.size(); i++) {
            newArgs.put(function.parameterList.get(i), argumentList.getArgs().get(i));
        }
        return function.body.execute(newArgs);
    }
}

class IfExpression implements Expression {
    private Expression condition, trueExpression, falseExpression;

    IfExpression(Expression condition, Expression trueExpression, Expression falseExpression) {
        this.condition = condition;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
    }

    @Override
    public Integer execute(HashMap<String, Expression> args) {
        return 0;
    }
}