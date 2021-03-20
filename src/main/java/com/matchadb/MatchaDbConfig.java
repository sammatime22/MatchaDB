package com.matchadb;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class MatchaDbConfig {

    private final String DROPOFF_PATH = "/tmp";

    @Bean
    public String getDropoffPath() {
        return this.DROPOFF_PATH;
    }
}
