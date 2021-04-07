public class Mapper {
    public static char intToChar(int index) {
        return (char) (index + 97);
    }

    public static int charToIndex(char symbol) {
        return symbol - 'a';
    }
}
