package php.runtime;

public class Startup {
    public static boolean isShowTimeOfInitExtension() {
        return isShowInitDelay();
    }

    public static boolean isShowInitDelay() {
        return Boolean.getBoolean("jphp.showInitDelay") || isTracing();
    }

    public static boolean isTracing() {
        return Boolean.getBoolean("jphp.trace");
    }

    public static boolean isDebug() {
        return Boolean.getBoolean("jphp.debug");
    }

    public static void trace(String message) {
        if (isTracing()) {
            System.out.println("[TRACE] " + message);
        }
    }

    public static void traceWithTime(String message, long startTime) {
        if (isTracing()) {
            startTime = System.currentTimeMillis() - startTime;
            System.out.println("[TRACE] " + message + ", " + startTime +"ms");
        }
    }
}
