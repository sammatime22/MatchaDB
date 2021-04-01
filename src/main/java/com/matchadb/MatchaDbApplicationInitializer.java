package com.matchadb;

import java.time.LocalDateTime; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchaDbApplicationInitializer {

    private final static Logger logger = LoggerFactory.getLogger(MatchaDbApplicationInitializer.class);

    public static void main(String[] args) {
        logger.info(String.format("MatchaDB Initiated at %s", LocalDateTime.now()));
        SpringApplication.run(MatchaDbApplicationInitializer.class, args);

        logger.info(String.format("MatchaDB Started at %s", LocalDateTime.now()));
    }

}