package com.gianfranco.stock.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RestConfig {

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .readTimeout(Duration.ofSeconds(5));
    }
}
