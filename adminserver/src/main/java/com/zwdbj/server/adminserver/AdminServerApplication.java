package com.zwdbj.server.adminserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class AdminServerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AdminServerApplication.class);
        app.addListeners(new ApplicationStartedEventLister());
        ConfigurableApplicationContext applicationContext = app.run(args);
    }
}
