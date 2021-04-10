import java.util.HashMap;
import java.util.List;

class Program {
    static HashMap<String, FunctionDefinition> functions;
}

class FunctionDefinition {
    String id;
    List<String> parameterList = null;
    Expression body;

    FunctionDefinition(String id, List<String> parameterList, Expression body) {
        this.id = id;
        this.parameterList = parameterList;
        this.body = body;
    }
}
