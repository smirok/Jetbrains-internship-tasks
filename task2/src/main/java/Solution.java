import java.util.concurrent.*;
import java.util.regex.Pattern;

public class Solution {

    public static boolean matches(String text, String regex) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<Boolean> task = new Callable<Boolean>() {
            public Boolean call() {
                return Pattern.compile(regex).matcher(text).matches();
            }
        };
        Future<Boolean> future = executor.submit(task);
        try {
            Boolean res = future.get(5, TimeUnit.SECONDS);
            future.cancel(true);
            executor.shutdownNow();
            return res;
        } catch (TimeoutException ex) {
            future.cancel(true);
            executor.shutdownNow();
            return false;
        } catch (InterruptedException | ExecutionException e) {
            executor.shutdownNow();
        }
        return false;
    }

    public static void main(String[] args) {
        String p = "(a|aa)+";

        StringBuilder s = new StringBuilder();
        for (; s.length() < 10_000_000; s.append('a')) {
        }

        System.out.println(matches(s.toString(), p));

        System.out.println(matches("aaaaa", p));

        return;
    }
}
