package plus.xyc.maven.plugin.i18n.utils;

import com.alibaba.fastjson2.JSONObject;
import org.apache.maven.plugin.logging.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropertiesUtils {

    public static JSONObject getContent(String path) throws Exception {
        JSONObject content = new JSONObject();
        Properties prop = new Properties();
        FileInputStream input = new FileInputStream(path);
        prop.load(input);
        prop.forEach((k, v) -> {
            content.put(k.toString(), v.toString());
        });
        return content;
    }

    public static void saveContent(String path, JSONObject content) throws Exception {
        Log log = LogUtils.get();
        log.info("saveContent: " + path);
        Properties prop = new Properties();
        prop.putAll(content);
        prop.store(new FileOutputStream(path), null);
    }

}
