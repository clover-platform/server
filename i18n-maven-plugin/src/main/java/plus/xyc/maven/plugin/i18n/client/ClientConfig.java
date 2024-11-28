package plus.xyc.maven.plugin.i18n.client;

import lombok.Data;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import plus.xyc.maven.plugin.i18n.client.entity.Language;
import plus.xyc.maven.plugin.i18n.utils.GitUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Data
public class ClientConfig {

    private String basedir;
    private String module;
    private String token;
    private List<String> files;
    private List<Language> languages;
    private String branch;

    public static ClientConfig parse(String basedir, String configPath) {
        SystemStreamLog log = new SystemStreamLog();
        ClientConfig config = new ClientConfig();
        String token = System.getenv("I18N_TOKEN");
        try{
            String path = new File(configPath).getAbsolutePath();
            InputStream input = Files.newInputStream(Paths.get(path));
            Yaml yaml = new Yaml(new Constructor(ClientConfig.class, new LoaderOptions()));
            config = yaml.load(input);
        }catch (Exception e) {
            log.error("init error: " + e.getMessage(), e);
        }
        config.setBasedir(basedir);
        config.setToken(token);
        config.setBranch(GitUtils.getCurrentBranch());
        return config;
    }

}