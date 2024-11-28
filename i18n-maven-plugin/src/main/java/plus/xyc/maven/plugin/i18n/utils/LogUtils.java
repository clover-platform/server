package plus.xyc.maven.plugin.i18n.utils;

import org.apache.maven.plugin.logging.Log;

public class LogUtils {

    private static final ThreadLocal<Log> threadLocal = new ThreadLocal<>();

    public static void set(Log log) {
        threadLocal.set(log);
    }

    public static Log get() {
        return threadLocal.get();
    }

}
