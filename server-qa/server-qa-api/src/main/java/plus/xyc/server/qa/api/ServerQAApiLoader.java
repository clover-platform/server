package plus.xyc.server.qa.api;

import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackageClasses = ServerQAApiLoader.class)
public class ServerQAApiLoader {
}
