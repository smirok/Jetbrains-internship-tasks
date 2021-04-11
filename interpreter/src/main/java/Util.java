import java.util.HashMap;
import java.util.List;

class Program {
    static HashMap<String, FunctionDefinition> functions;
}

class FunctionDefinition {
    public String getId() {
        return id;
    }

    public List<String> getParameterList() {
        return parameterList;
    }

    public Expression getBody() {
        return body;
    }

    private final String id;
    private final List<String> parameterList;
    private final Expression body;

    FunctionDefinition(String id, List<String> parameterList, Expression body) {
        this.id = id;
        this.parameterList = parameterList;
        this.body = body;
    }
}

class InterpreterException extends Exception {
    public InterpreterException(String errorMessage) {
        super(errorMessage);
    }
}