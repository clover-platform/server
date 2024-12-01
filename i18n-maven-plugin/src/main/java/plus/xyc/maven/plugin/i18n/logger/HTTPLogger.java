package plus.xyc.maven.plugin.i18n.logger;


import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import plus.xyc.maven.plugin.i18n.utils.LogUtils;

public class HTTPLogger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(@NotNull String message) {
        LogUtils.get().info(message);
    }
}
