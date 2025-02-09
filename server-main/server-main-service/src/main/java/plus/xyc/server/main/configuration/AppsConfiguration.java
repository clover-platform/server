package plus.xyc.server.main.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import plus.xyc.server.main.config.entity.AppsItem;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "apps")
@Data
public class AppsConfiguration {

    private List<AppsItem> list;

}
