package com.example.aoo.config;

import com.example.aoo.service.DateTimeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public DateTimeService timeService() {
        return new DateTimeService();
    }
}
