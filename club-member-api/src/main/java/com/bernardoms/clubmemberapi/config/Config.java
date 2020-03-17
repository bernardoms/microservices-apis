package com.bernardoms.clubmemberapi.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
@Configuration
@Getter
public class Config {

    @Value("${restTemplate.connectionTimout}")
    private int restTemplateTimeout;
    @Value("${restTemplate.readTimeout}")
    private int readTimeout;
    @Value("${team.endpoint}")
    private String teamEndpointAPI;


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(restTemplateTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .build();
    }
}
