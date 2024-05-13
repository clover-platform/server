package plus.xyc.server.i18n.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "i18n")
@Data
public class AppConfiguration {

    private String fallbackLanguage;
    private String dataFallbackLanguage;

}
