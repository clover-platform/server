package plus.xyc.maven.plugin.i18n.client;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import org.apache.maven.plugin.logging.Log;
import plus.xyc.maven.plugin.i18n.utils.LogUtils;

import java.io.IOException;
import java.util.Objects;

public class Client {

    private final Log log;
    private final ClientConfig config;
    private final OkHttpClient httpClient = new OkHttpClient();

    public Client(String basedir, String configPath) {
        this.log = LogUtils.get();
        log.info("basedir: " + basedir);
        log.info("configPath: " + configPath);
        this.config = ClientConfig.parse(basedir, configPath);
        log.info("config: " + JSON.toJSONString(config));
    }

    public void push() throws IOException {
        createBranchIfNotExists(config.getBranch());
    }

    public void createBranchIfNotExists(String branch) throws IOException {
        log.info("create branch if not exists");
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject data = new JSONObject();
        data.put("branch", branch);
        RequestBody body = RequestBody.create(data.toJSONString(), JSON);
        Request request = new Request.Builder()
                .url(config.getDomain() + "/api/i18n/open/" + config.getModule() + "/branch/create")
                .header("Authorization", "Bearer " + config.getToken())
                .post(body).build();
        Response response = httpClient.newCall(request).execute();
        log.info("response: " + Objects.requireNonNull(response.body()).string());
        response.close();
    }

}
