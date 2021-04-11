import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class Solution {
    private static final int RUNNING_TIME = 5000;

    public static boolean matches(String text, String regex) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        Thread funcThread = new Thread() {
            public void run() {
                try {
                    atomicBoolean.set(Pattern.compile(regex).matcher(text).matches());
                } catch (StackOverflowError error) {
                    atomicBoolean.set(false);
                }
            }
        };

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(RUNNING_TIME);
                } catch (InterruptedException ignored) {
                }
            }
        };

        timerThread.setDaemon(true);
        timerThread.start();

        funcThread.setDaemon(true);
        funcThread.start();

        while (true) {
            if (!timerThread.isAlive() || !funcThread.isAlive()) {
                return atomicBoolean.get();
            }
        }
    }
}
