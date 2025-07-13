package com.epam.gymcrm.config;

import com.epam.gymcrm.storage.Storage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Storage storage() {
        return new Storage();
    }
}
