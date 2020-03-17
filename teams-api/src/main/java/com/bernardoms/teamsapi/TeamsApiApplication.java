package com.bernardoms.teamsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableHystrix
public class TeamsApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeamsApiApplication.class, args);
    }
}
