package com.matchadb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.EnableAutoConfiguration;

// @Configuration
// @ComponentScan({"com.matchadb.surface"})
@SpringBootApplication
public class MatchaDbApplicationInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MatchaDbApplicationInitializer.class, args);
    }

}