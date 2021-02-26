package com.matchadb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.matchadb")
public class MatchaDbApplicationInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MatchaDbApplicationInitializer.class, args);
    }

}