package com.matchadb.database;

import com.matchadb.generate.MatchaDbGenerateData;

import com.matchadb.models.MatchaDbCommandResult;

import java.io.FileReader;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import mockit.Tested;
import mockit.Mocked;

/**
 * Tests the DB Table methods.
 */
public class MatchaDbTableTest {

    @Tested MatchaDbTable matchaDbTable;

    /**
     * The following test checks to see that our Load Data method works appropriately.
     */
    @Test
    public void testLoadDataTestFileClothesWebsiteAPI() {
        // Location of the test file used
        String filename = "src/test/java/com/matchadb/resources/TestFileClothesWebsiteAPI.json";

        // A timestamp before the start of the test, to make sure the timestamps in the db
        // are updated.
        long timeBeforeDBLoadAndLastUpdate = System.currentTimeMillis();

        // The expected tables within the DB.
        List<String> expectedTables = new ArrayList<String>() {{
           add("Shirts"); add("Pants"); add("Hats"); add("Shoes");
        }};

        // Instantiating the matchaDbTable Instance
        matchaDbTable = new MatchaDbTable();
        
        try {
            // Load in our real table
            matchaDbTable.loadData(new FileReader(filename));

            // Check to see that the metadata is appropriate
            MatchaDbCommandResult metadata = matchaDbTable.retrieveDbMetadata();
            HashMap<String, Object> metadataContents = metadata.getContents();

            // Check on Timestamps
            Assert.assertTrue(timeBeforeDBLoadAndLastUpdate < (long) metadataContents.get("Upload Time"));
            Assert.assertTrue(timeBeforeDBLoadAndLastUpdate < (long) metadataContents.get("Last Update Time"));

            // Check Flags
            Assert.assertTrue((boolean) metadataContents.get("Filled"));
            Assert.assertTrue(! (boolean) metadataContents.get("Corrupted"));

            // Check "Tables" Listing
            Assert.assertTrue(expectedTables.size() == ((List<String>) metadataContents.get("Tables")).size());
            for (String table : (List<String>) metadataContents.get("Tables")) {
                Assert.assertTrue(expectedTables.contains(table));
            }

        } catch (FileNotFoundException fnfe) {
            // If a FileNotFoundException comes up, fail the test.
            fnfe.printStackTrace();
            Assert.fail();
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