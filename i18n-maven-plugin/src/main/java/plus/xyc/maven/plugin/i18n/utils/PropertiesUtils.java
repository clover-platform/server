package plus.xyc.maven.plugin.i18n.utils;

import com.alibaba.fastjson2.JSONObject;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesUtils {

    public static JSONObject getContent(String key) throws Exception {
        JSONObject content = new JSONObject();
        Properties prop = new Properties();
        FileInputStream input = new FileInputStream(key);
        prop.load(input);
        prop.forEach((k, v) -> {
            content.put(k.toString(), v.toString());
        });
        return content;
    }

}
