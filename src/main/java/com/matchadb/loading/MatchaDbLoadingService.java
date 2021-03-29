package com.matchadb.loading;

import com.matchadb.database.MatchaDbTable;

import java.io.FileReader;
import java.io.FileNotFoundException;

import java.lang.System;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * The initial starting point of the application, which helps all services by 
 * providing any necessary environment details.
 */
public class MatchaDbLoadingService {

   //  @Autowired MatchaDbTable matchaDbTable;

    private static final Logger logger = LoggerFactory.getLogger(MatchaDbLoadingService.class);

    public MatchaDbLoadingService() { 
        // Empty Constructor
    }

    /**
     * Will make sure that all singletons are properly loaded, and so forth.
     */
    public void loadApplication(MatchaDbTable matchaDbTable) {
        try {
            matchaDbTable.loadData(
                new FileReader("src/test/java/com/matchadb/resources/TestFileClothesWebsiteAPI.json"),
                "A Table"    
            );
        } catch (FileNotFoundException fnfe) {
            logger.error(String.format("File %s coult not be found. Exiting.", "file"), fnfe);
            System.exit(1);
        }
    }

}