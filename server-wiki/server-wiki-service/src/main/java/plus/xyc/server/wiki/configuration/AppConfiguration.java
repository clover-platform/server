package plus.xyc.server.wiki.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.zkit.support.server.ai.api.entity.Message;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppConfiguration {

    private String baseUrl;
    private List<Message> chatPrompts;
    private List<Message> writerPrompts;

}
