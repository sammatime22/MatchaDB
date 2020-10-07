package com.matchadb.database;

import com.matchadb.generate.MatchaDbGenerateData;

import java.io.FileReader;
import java.io.FileNotFoundException;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import mockit.Tested;

/**
 * Tests the DB Table methods.
 */
public class MatchaDbTableTest {

    @Tested MatchaDbTable matchaDbTable;

    // Load in our mock table for comparison. We might not end up using this.
    // HashMap<String, Object> mockClothesWebsiteAPITable =
    //     MatchaDbGenerateData.generateClothesWebsiteAPITable();

    /**
     * Sets up everything we'll need before the test starts.
     */
    @Before
    public void testSetup() {
       
    }

    /**
     * The following tests run through all of the public facing operations of
     * the database in the scenario of a Clothes Website API.
     */
    @Test
    public void testLoadDataTestFileClothesWebsiteAPI() {
        String filename = "src/test/java/com/matchadb/resources/TestFileClothesWebsiteAPI.json";
        matchaDbTable = new MatchaDbTable();
        try {
            // Load in our real table
            matchaDbTable.loadData(new FileReader(filename));

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            Assert.fail();
        }
        
        // Check to see that the size is equal
        // Assert.assertEquals(0l, matchaDbTable.getTableSizeInBytes());
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