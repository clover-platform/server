package plus.xyc.maven.plugin.i18n.client;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.maven.plugin.logging.Log;
import plus.xyc.maven.plugin.i18n.client.entity.ClientConfig;
import plus.xyc.maven.plugin.i18n.client.entity.Result;
import plus.xyc.maven.plugin.i18n.exception.ApiException;
import plus.xyc.maven.plugin.i18n.logger.HTTPLogger;
import plus.xyc.maven.plugin.i18n.utils.LogUtils;
import plus.xyc.maven.plugin.i18n.utils.PropertiesUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Client {

    private final Log log;
    private final ClientConfig config;
    private OkHttpClient httpClient;
    private final MediaType TYPE = MediaType.parse("application/json;charset=utf-8");

    public Client(String basedir, String configPath) {
        this.log = LogUtils.get();
        log.info("basedir: " + basedir);
        log.info("configPath: " + configPath);
        this.config = ClientConfig.parse(basedir, configPath);
        log.info("config: " + JSON.toJSONString(config));
        this.initHttpClient();
    }

    private void initHttpClient() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HTTPLogger()); // 创建拦截对象
        if(config.getDebug()) logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 这一句一定要记得写，否则没有数据输出
        httpClient = new OkHttpClient.Builder().addNetworkInterceptor(logInterceptor).build();
    }

    public void push() throws ApiException, IOException {
        boolean branchSuccess = createBranchIfNotExists(config.getBranch());
        log.info("branchSuccess: " + branchSuccess);
        if(!branchSuccess) {
            throw new ApiException("create branch failed");
        }
        // 读取国际化文件
        JSONObject fullContent = new JSONObject();
        config.getFiles().forEach(item -> {
            File file = new File(config.getBasedir(), item + ".properties");
            String name = file.getName().replace(".properties", "");
            String path = file.getAbsolutePath();
            log.info(name);
            log.info(path);
            try {
                JSONObject content = PropertiesUtils.getContent(path);
                content.forEach((k, v) -> {
                    fullContent.put(name + "." + k, v);
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        RequestBody body = RequestBody.create(fullContent.toJSONString(), TYPE);
        Request request = new Request.Builder()
                .url(config.getDomain() + "/api/i18n/open/" + config.getModule() + "/branch/" + config.getBranch() + "/entry/push")
                .header("Authorization", "Bearer " + config.getToken())
                .post(body).build();
        Response response = httpClient.newCall(request).execute();
        Result<?> result = JSON.parseObject(Objects.requireNonNull(response.body()).string(), Result.class);
        log.info("create branch if not exists, result: " + JSON.toJSONString(result));
        response.close();
        if(!result.getSuccess()) {
            throw new ApiException("push failed");
        }
        log.info("push success");
    }

    public boolean createBranchIfNotExists(String branch) throws IOException {
        log.info("create branch if not exists, branch: " + branch);
        JSONObject data = new JSONObject();
        data.put("name", branch);
        RequestBody body = RequestBody.create(data.toJSONString(), TYPE);
        Request request = new Request.Builder()
                .url(config.getDomain() + "/api/i18n/open/" + config.getModule() + "/branch/create/if/not/exist")
                .header("Authorization", "Bearer " + config.getToken())
                .post(body).build();
        Response response = httpClient.newCall(request).execute();
        Result<?> result = JSON.parseObject(Objects.requireNonNull(response.body()).string(), Result.class);
        log.info("create branch if not exists, result: " + JSON.toJSONString(result));
        response.close();
        return result.getSuccess();
    }

}
