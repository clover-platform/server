package plus.xyc.server.wiki.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppConfiguration {

    private String baseUrl;
    private List<String> chatRules;
    private List<String> writerRules;

}
