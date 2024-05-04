package plus.xyc.server.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"plus.xyc.server", "org.zkit.support"})
@MapperScan("plus.xyc.server.main.*.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"org.zkit.support"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
