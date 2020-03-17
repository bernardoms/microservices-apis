package com.bernardoms.clubmemberapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
public class ClubMemberApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClubMemberApiApplication.class, args);
    }

}
