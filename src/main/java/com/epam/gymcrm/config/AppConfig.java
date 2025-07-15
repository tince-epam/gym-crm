package com.epam.gymcrm.config;

import com.epam.gymcrm.storage.Storage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.epam.gymcrm")
public class AppConfig {

    @Bean
    public Storage storage() {
        return new Storage();
    }
}
