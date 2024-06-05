package plus.xyc.server.wiki.api;

import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackageClasses = ServerWikiApiLoader.class)
public class ServerWikiApiLoader {
}
