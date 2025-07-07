package plus.xyc.server.main.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "event")
@Data
public class EventConfiguration {

    private String projectTopic;
    private String teamTopic;

}
