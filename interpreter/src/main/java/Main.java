import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser("(2+2)");
        Optional<Integer> result = parser.runProgram();
        result.ifPresent(System.out::println);
    }
}
