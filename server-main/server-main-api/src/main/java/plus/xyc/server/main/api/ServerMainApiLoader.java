package plus.xyc.server.main.api;

import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackageClasses = ServerMainApiLoader.class)
public class ServerMainApiLoader {
}
