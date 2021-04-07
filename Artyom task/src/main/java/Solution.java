import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
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

    private Optional<DirectedEdge> getDirectedEdgeByDifference(String largerStr, String smallerStr) {
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
            throw new IllegalStateException(
                    "The early word is lexicographically smaller than the late word for any alphabet"
            );
        }

        return Optional.empty();
    }

    public void readInput(InputStream inputStream) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(inputStream));
        int wordsCount = Integer.parseInt(reader.readLine());
        words = new String[wordsCount];
        for (int i = 0; i < wordsCount; i++) {
            words[i] = reader.readLine();
        }
    }

    private void buildDirectedGraph() {
        for (int i = 0; i < words.length; i++) {
            for (int j = i + 1; j < words.length; j++) {
                Optional<DirectedEdge> edgeOptional = getDirectedEdgeByDifference(words[i], words[j]);

                if (edgeOptional.isPresent()) {
                    DirectedEdge edge = edgeOptional.get();
                    adjacent[Mapper.charToIndex(edge.from)][Mapper.charToIndex(edge.to)] = true;
                }
            }
        }
    }

    private boolean findCycle() {
        boolean isFoundCycle = false;
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            isFoundCycle |= dfs(i);
        }
        return isFoundCycle;
    }

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

    public Optional<String> solve(InputStream inputStream) throws IOException {
        readInput(inputStream);

        try {
            buildDirectedGraph();
        } catch (IllegalStateException exception) {
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

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();
        Optional<String> optionalAlphabet = solution.solve(System.in);
        if (optionalAlphabet.isPresent()) {
            System.out.println(optionalAlphabet.get());
        } else {
            System.out.println("Impossible");
        }
    }
}
