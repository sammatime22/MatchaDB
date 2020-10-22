package com.matchadb.database;

import com.matchadb.generate.MatchaDbGenerateData;

import com.matchadb.models.MatchaDbCommandResult;

import java.io.File;
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
        // An empty dropoff path - not used in this test
        String emptyPath = null;

        // Location of the test file used
        String filename = "src/test/java/com/matchadb/resources/TestFileClothesWebsiteAPI.json";

        // Database name
        String databaseName = "TestFileClothesWebsiteAPI";

        // A timestamp before the start of the test, to make sure the timestamps in the db
        // are updated.
        long timeBeforeDBLoadAndLastUpdate = System.currentTimeMillis();

        // The expected tables within the DB.
        List<String> expectedTables = new ArrayList<String>() {{
           add("Shirts"); add("Pants"); add("Hats"); add("Shoes");
        }};

        // Instantiating the matchaDbTable Instance
        matchaDbTable = new MatchaDbTable(emptyPath);
        
        try {
            // Load in our real table
            matchaDbTable.loadData(new FileReader(filename), "TestFileClothesWebsiteAPI");

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
    @Test
    public void testSaveData() {
        // Path to be used in the test.
        // We'll check to see if this path actually exists and is usable before starting.
        String testDirectory = "testdirectory/"; 

        // Clean/Create the test directory
        developTestDirectory(testDirectory);

        // Build the database
        matchaDbTable = new MatchaDbTable(testDirectory);

        // Location of the test file used
        String filename = "src/test/java/com/matchadb/resources/TestFileClothesWebsiteAPI.json";        

        boolean failed = false;

        try {
            // Load in the data for the DB
            matchaDbTable.loadData(new FileReader(filename), "TestFileClothesWebsiteAPI");

            // Save the data and check that the data within the file is as expected.
            matchaDbTable.saveData();
        } catch (Exception e) {
            e.printStackTrace();
            failed = true;
        }
        // Remove the test directory
        deleteTestDirectory(testDirectory);

        if (failed) {
            Assert.fail();
        }
    }

    /**
     * Develops the test directory given a test path. If the directory does not
     * exist, it will be created, and likewise, if it is full, will be cleaned.
     * Will fail if an exception occurs.
     *
     * @param testDirectory The test directory to be used.
     */
    public void developTestDirectory(String testDirectory) {
        File directory = new File(testDirectory);
        try {
            // Were we given a directory or a spot that could be If not, fail.
            if (directory.isFile()) {
                throw new Exception("Was given something other than a directory.");
            }

            // Does the directory exist? If not, make it.
            if (!directory.exists()) {
                directory.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    /**
     * Deletes the test directory after the test is done.
     *
     * @param testDirectory The test directory to be used.
     */
    public void deleteTestDirectory(String testDirectory) {
        File directory = new File(testDirectory);

        try {
            directory.delete();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    /**
     * Tests that, given a command, the DB can run said command.
     */
    @Test
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

    /**
     * Develops the test directory/cleans out the test directory. Will fail if 
     * an error occurs in the development of the directory.
     *
     * @param pathToDirectory The path to the directory.
     */
    private void prepareTestDirectory(String pathToDirectory) {

    }
}