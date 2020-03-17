package com.bernardoms.clubmemberapi.integration.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestConfig {
    @Bean(name="wireMockBean", initMethod = "start", destroyMethod = "stop")
    @Primary
    public WireMockServer wireMockBean() {
        return new WireMockServer(WireMockConfiguration.wireMockConfig()
                .port(3002)
                .usingFilesUnderClasspath("wiremock"));
    }

}
