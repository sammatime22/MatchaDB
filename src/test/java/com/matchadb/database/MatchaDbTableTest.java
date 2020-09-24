package com.matchadb.database;

import org.junit.jupiter.api.Test;
import mockit.Tested;

/**
 * Tests the DB Table methods.
 */
public class MatchaDbTableTest {

    @Tested MatchaDbTable matchaDbTable;

    /**
     * Tests the loading in of data from a JSON file to 
     * the database.
     */
    @Test
    public void testLoadDataTestFileClothesWebsiteAPI() {
        String filename = "TestFileClothesWebsiteAPI.json";
        try {

            // 1. Provide TestFileClothesWebsiteAPI to matchaDbTable
            matchaDbTable.loadData(new FileReader(filename));

            // 2. Run through generated table and compare that what was generated 
            //    by our data generator is exactly what the DB Table looks like

        }
    }

    /**
     * Tests that, upon exiting the application, the DbTable can save the 
     * table to a JSON file.
     */
    public void testSaveData() {

    }

    /**
     * Tests that, given a command, the DB can run said command.
     */
    public void testRunCommand() {

    }

    /**
     * Tests the self searching mechanism.
     */
    @Test
    public void testSearchForData() {

    }

    /**
     * Tests the system to get data from a specific portion of 
     * memory.
     */
    @Test
    public void testGetData() {

    }

    /**
     * Tests the system to post data from a specific portion of 
     * memory.
     */
    @Test
    public void testPostData() {

    }

    /**
     * Tests the system to update data from a specific portion of 
     * memory.
     */
    @Test
    public void testUpdateData() {

    }

    /**
     * Tests the system to delete data from a specific portion of 
     * memory.
     */
    @Test
    public void testDeleteData() {

    }
}