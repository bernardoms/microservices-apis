package com.bernardoms.teamsapi.config;

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
    @Value("${campaign.endpoint}")
    private String campaignEndpoint;
    @Value("${campaign.size.page: 100}")
    private int sizePage;


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(restTemplateTimeout))
                .setReadTimeout(Duration.ofSeconds(readTimeout))
                .build();
    }
}
