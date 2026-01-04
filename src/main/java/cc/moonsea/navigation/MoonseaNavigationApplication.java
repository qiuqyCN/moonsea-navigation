package cc.moonsea.navigation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import cc.moonsea.navigation.config.AppConfig;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class MoonseaNavigationApplication {

    static void main(String[] args) {
        SpringApplication.run(MoonseaNavigationApplication.class, args);
    }

}
