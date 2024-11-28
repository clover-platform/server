package plus.xyc.maven.plugin.i18n.client;

import com.alibaba.fastjson2.JSON;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

public class Client {

    private final Log log;
    private final ClientConfig config;

    public Client(String basedir, String configPath) {
        this.log = new SystemStreamLog();
        log.info("basedir: " + basedir);
        log.info("configPath: " + configPath);
        this.config = ClientConfig.parse(basedir, configPath);
        log.info("config: " + JSON.toJSONString(config));
    }

    public void push() {
        log.info("push to " + config.getModule());
    }

}
