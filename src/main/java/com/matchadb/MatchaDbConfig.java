package com.matchadb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class MatchaDbConfig {

    private final static Logger logger = LoggerFactory.getLogger(MatchaDbConfig.class);

    private final String DROPOFF_PATH = "/tmp";

    @Bean
    public String getDropoffPath() {
        logger.info(String.format("Using dropoff path: %s", DROPOFF_PATH));
        return this.DROPOFF_PATH;
    }
}
