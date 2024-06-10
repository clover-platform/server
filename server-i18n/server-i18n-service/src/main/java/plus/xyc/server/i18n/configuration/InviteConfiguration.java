package plus.xyc.server.i18n.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.invite")
@Data
public class InviteConfiguration {

    private String base;
    private Long expireAt;

}
