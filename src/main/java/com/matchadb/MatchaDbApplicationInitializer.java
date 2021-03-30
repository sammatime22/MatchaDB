package com.matchadb;

import com.matchadb.database.MatchaDbTable;

import com.matchadb.loading.MatchaDbLoadingService;

import java.lang.Thread;
import java.lang.InterruptedException;

import java.time.LocalDateTime; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchaDbApplicationInitializer {

    @Autowired static MatchaDbTable matchaDbTable;

    private final static Logger logger = LoggerFactory.getLogger(MatchaDbApplicationInitializer.class);

    public static void main(String[] args) {
        logger.info(String.format("MatchaDB Initiated at %s", LocalDateTime.now()));
        SpringApplication.run(MatchaDbApplicationInitializer.class, args);

        MatchaDbLoadingService loadingService = new MatchaDbLoadingService(); 

        while (matchaDbTable == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                logger.error("Interrupted Exception thrown.", ie);
            }
            logger.info("Waiting on MatchaDbTable Initialization for loading...");
        }

        loadingService.loadApplication(matchaDbTable);

        logger.info(String.format("MatchaDB Started at %s", LocalDateTime.now()));
    }

}