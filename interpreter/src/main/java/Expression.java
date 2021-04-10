import java.util.List;

public interface Expression {
    Integer execute();
}

class Identifier implements Expression {
    private String id;

    Identifier(String id) {
        this.id = id;
    }

    @Override
    public Integer execute() {
        return null;
    }
}

class ConstantExpression implements Expression {
    private Integer number;

    ConstantExpression(Integer number) {
        this.number = number;
    }

    @Override
    public Integer execute() {
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
    public Integer execute() {
        switch (operation) {
            case ("+"):
                return leftExpression.execute() + rightExpression.execute();
            case ("-"):
                return leftExpression.execute() - rightExpression.execute();
            case ("*"):
                return leftExpression.execute() * rightExpression.execute();
            case ("/"):
                return leftExpression.execute() / rightExpression.execute();
            case ("%"):
                return leftExpression.execute() % rightExpression.execute();
            case ("<"):
                return leftExpression.execute() < rightExpression.execute() ? 1 : 0;
            case (">"):
                return leftExpression.execute() > rightExpression.execute() ? 1 : 0;
            case ("="):
                return leftExpression.execute().equals(rightExpression.execute()) ? 1 : 0;
        }
        return 0;
    }
}

class ArgumentList implements Expression {
    private List<Expression> args;

    ArgumentList(List<Expression> args) {
        this.args = args;
    }

    @Override
    public Integer execute() {
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
    public Integer execute() {
        return 0;
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
    public Integer execute() {
        return 0;
    }
}