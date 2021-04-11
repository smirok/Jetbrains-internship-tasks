import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class Solution {
    public static final int ALPHABET_SIZE = 26;

    private String[] words = null;
    private final boolean[][] adjacent = new boolean[ALPHABET_SIZE][ALPHABET_SIZE];
    private final VertexState[] vertexStates = new VertexState[ALPHABET_SIZE];
    private final ArrayList<Character> newAlphabet = new ArrayList<>();

    {
        Arrays.fill(vertexStates, VertexState.NOT_VISITED);
    }

    private static class DirectedEdge {
        public DirectedEdge(char from, char to) {
            this.from = from;
            this.to = to;
        }

        char from, to;
    }

    private enum VertexState {
        NOT_VISITED,
        VISITING,
        LEAVED
    }

    /**
     * @param largerStr  assumed large string
     * @param smallerStr assumed small string
     * @return Optional(to from) if strings do not match in the character
     * Optional.empty() if smaller string is a prefix of larger string
     * @throws InvalidAlphabetException if larger string is a prefix of smaller string
     */
    private Optional<DirectedEdge> getDirectedEdgeByDifference(String largerStr, String smallerStr)
            throws InvalidAlphabetException {
        int largerIndex = 0;
        int smallerIndex = 0;
        while (largerIndex < largerStr.length() && smallerIndex < smallerStr.length()) {
            if (largerStr.charAt(largerIndex) != smallerStr.charAt(smallerIndex)) {
                return Optional.of(
                        new DirectedEdge(largerStr.charAt(largerIndex), smallerStr.charAt(smallerIndex))
                );
            }

            largerIndex++;
            smallerIndex++;
        }

        if (largerIndex == largerStr.length() && smallerIndex < smallerStr.length()) {
            throw new InvalidAlphabetException(
                    "The early word is lexicographically smaller than later word for any alphabet"
            );
        }

        return Optional.empty();
    }

    public void readInput(InputStream inputStream) {
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(inputStream))) {
            int wordsCount = Integer.parseInt(reader.readLine());
            words = new String[wordsCount];
            for (int i = 0; i < wordsCount; i++) {
                words[i] = reader.readLine();
            }
        } catch (IOException ioException) {
            System.out.println("Incorrect input");
            System.exit(0);
        }
    }

    private void buildDirectedGraph() throws InvalidAlphabetException {
        for (int i = 0; i < words.length - 1; i++) {
            Optional<DirectedEdge> edgeOptional = getDirectedEdgeByDifference(words[i], words[i + 1]);

            if (edgeOptional.isPresent()) {
                DirectedEdge edge = edgeOptional.get();
                adjacent[Mapper.charToIndex(edge.from)][Mapper.charToIndex(edge.to)] = true;
            }
        }
    }

    /**
     * @return true if graph contains cycle
     * false otherwise
     */
    private boolean findCycle() {
        boolean isFoundCycle = false;
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            isFoundCycle |= dfs(i);
        }
        return isFoundCycle;
    }


    /**
     * @param vertex - root of dfs tree
     * @return true if current connectivity component contains cycle
     * false otherwise
     */
    private boolean dfs(int vertex) {
        if (vertexStates[vertex] == VertexState.LEAVED) {
            return false;
        }
        if (vertexStates[vertex] == VertexState.VISITING) {
            return true;
        }

        vertexStates[vertex] = VertexState.VISITING;
        boolean foundCycle = false;
        for (int to = 0; to < ALPHABET_SIZE; to++) {
            if (adjacent[vertex][to]) {
                foundCycle |= dfs(to);
            }
        }
        vertexStates[vertex] = VertexState.LEAVED;

        newAlphabet.add(Mapper.intToChar(vertex));
        return foundCycle;
    }

    /**
     * @param inputStream - the stream through which we get the data
     * @return Optional.empty() if correct alphabet not found
     * Optional with alphabet otherwise
     */
    public Optional<String> solve(InputStream inputStream) {
        readInput(inputStream);

        try {
            buildDirectedGraph();
        } catch (InvalidAlphabetException exception) {
            return Optional.empty();
        }

        if (findCycle()) {
            return Optional.empty();
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = ALPHABET_SIZE - 1; i >= 0; i--) {
                stringBuilder.append(newAlphabet.get(i));
            }
            return Optional.of(stringBuilder.toString());
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        Optional<String> optionalAlphabet = solution.solve(System.in);

        if (optionalAlphabet.isPresent()) {
            System.out.println(optionalAlphabet.get());
        } else {
            System.out.println("Impossible");
        }
    }
}
