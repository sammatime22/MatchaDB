package com.matchadb.database;

import com.matchadb.generate.MatchaDbGenerateData;

import com.matchadb.models.MatchaDbCommandResult;
import com.matchadb.models.MatchaQuery;

import java.io.File;
import java.io.FilenameFilter;
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
        List<Object> expectedTable = MatchaDbGenerateData.generateClothesWebsiteAPITable();
        String tableName = "TestFileClothesWebsiteAPI";
        String emptyPath = "";

        // Location of the test file used
        String filename = "src/test/java/com/matchadb/resources/TestFileClothesWebsiteAPI.json";

        // Database name
        String databaseName = "TestFileClothesWebsiteAPI";

        // A timestamp before the start of the test, to make sure the timestamps in the db
        // are updated.
        long timeBeforeDBLoadAndLastUpdate = System.currentTimeMillis();

        // Instantiating the matchaDbTable Instance
        matchaDbTable = new MatchaDbTable(emptyPath);
        
        try {
            // Load in our real table
            matchaDbTable.loadData(new FileReader(filename), tableName);

            // Check to see that the metadata is appropriate
            MatchaDbCommandResult metadata = matchaDbTable.retrieveDbMetadata();
            HashMap<String, Object> metadataContents = metadata.getContents();

            // Check on Timestamps
            Assert.assertTrue(timeBeforeDBLoadAndLastUpdate <= (long) metadataContents.get("Upload Time"));
            Assert.assertTrue(timeBeforeDBLoadAndLastUpdate <= (long) metadataContents.get("Last Update Time"));

            // Check Flags
            Assert.assertTrue((boolean) metadataContents.get("Filled"));
            Assert.assertTrue(! (boolean) metadataContents.get("Corrupted"));
            
            // Check that the table name was properly set.
            Assert.assertTrue(tableName.equals((String) metadataContents.get("Table Name")));

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
        File testDirectoryFileObject = new File(testDirectory);
        int testFileLocation = 0;
        int endOfFile = -1;

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

            // With the exception of tabs, spaces, or returns, check that each character in the file matches
            File testFile = new File(testDirectoryFileObject.list()[testFileLocation]);
            try (FileReader testFileReader = new FileReader(testDirectory + testFile); FileReader templateFileReader = new FileReader(
                new File("src/test/java/com/matchadb/resources/TestFileClothesWebsiteAPI.json"))) {
                int place = 0;
                while (true) {
                    int templateCharacterValue = templateFileReader.read();
                    // Move until we are at a character we want to compare
                    while (templateCharacterValue == 10 || templateCharacterValue == 32) {
                        templateCharacterValue = templateFileReader.read();
                    }
                    int generatedFileCharacterValue = testFileReader.read();
                    // Move until we are at a character we want to compare
                    while (generatedFileCharacterValue == 10 || generatedFileCharacterValue == 32) {
                        generatedFileCharacterValue = testFileReader.read();
                    }
                    if (templateCharacterValue == endOfFile && generatedFileCharacterValue == endOfFile) {
                        break; // Done comparing the two files!
                    }
                    if ((templateCharacterValue != endOfFile && generatedFileCharacterValue == endOfFile) ||
                        (templateCharacterValue == endOfFile && generatedFileCharacterValue != endOfFile) ||
                        (templateCharacterValue != generatedFileCharacterValue)) {
                        // Either one of the files is larger than the other, or the characters do not match.
                        System.out.println("Failed in comparison for " + templateCharacterValue + " and " + generatedFileCharacterValue + " @" + place);
                        failed = true;
                        break;
                    }
                    place++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed in Filereader trycatch");
                failed = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed in load data trycatch");
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
            // Since this is just test code and we only expect there
            // to be files, ie no other directories, just recursively 
            // delete all files.
            for (File fileInDirectory : directory.listFiles()) {
                fileInDirectory.delete();
            }

            directory.delete();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    /**
     * Tests the system to get data from a specific portion of 
     * memory.
     */
    @Test
    public void testGetData() {
        // Location of the test file used
        String filename = "src/test/java/com/matchadb/resources/TestFileClothesWebsiteAPI.json";

        // We want to set up the MatchaQuery object for the following query
        // Select * from * where Item Name contains 's' and Item Price < 30.00
        // And the item is from the "Shoes" table
        List<HashMap<String, Object>> expectedObjects = 
            MatchaDbGenerateData.getClothesWebsiteItemsViaQueryParams("s", null, null, null, 30.00, "Shirts");

        MatchaQuery matchaQuery = new MatchaQuery(new String[]{ "Shirts" }, 
            new String[][]{{ "Item Name", "has", "'s'" }});

        matchaDbTable = new MatchaDbTable("");

        try {
            // Load in the data for the DB
            matchaDbTable.loadData(new FileReader(filename), "TestFileClothesWebsiteAPI");
            List<HashMap<String, Object>> actualObjects = 
                (List<HashMap<String, Object>>) matchaDbTable.getData(matchaQuery);

            for (HashMap expectedObject : expectedObjects) {
                boolean success = false;
                for (HashMap actualObject : actualObjects) {

                    if (expectedObject.get("Item Name").equals(actualObject.get("Item Name"))) {
                        if (expectedObject.get("Item Brand").equals(actualObject.get("Item Brand"))
                            && expectedObject.get("Item Price").equals(actualObject.get("Item Price"))
                            && expectedObject.get("Item Description").equals(actualObject.get("Item Description"))) {
                            success = true;
                            break;
                        } else {
                            Assert.fail();
                        }
                    }
                }
                
                // If the object didn't come up, we should just fail the test.
                if (!success) {
                    Assert.fail();
                }
            }
        } catch (FileNotFoundException fnfe) {
            // If a FileNotFoundException comes up, fail the test.
            fnfe.printStackTrace();
            Assert.fail();
        }
    }

    /**
     * Tests the system to post data from a specific portion of 
     * memory.
     */
    @Test
    public void testPostData() {
        // Not sure if we want to try inserting multiple items - maybe that doesn't
        // make sense actually
        HashMap<String, Object> fancyHatToAdd = MatchaDbGenerateData.newClothesItemsToInsert().get(0);

        // Query to get item

        // Query to insert item

        matchaDbTable = new MatchaDbTable("");

        try {
            // Maybe consider a before method..?
            // Load in the data for the DB
            matchaDbTable.loadData(new FileReader(filename), "TestFileClothesWebsiteAPI");
            // Show the item is not there (get)
            
            // Add new item

            // Search (get) and see that it is where it is expected
        } catch (FileNotFoundException fnfe) {
            // If a FileNotFoundException comes up, fail the test.
            fnfe.printStackTrace();
            Assert.fail();            
        }
    }

    /**
     * Tests the system to update data from a specific portion of 
     * memory.
     */
    @Test
    public void testUpdateData() {
        // Show the item is unmodified (get)

        // Update item

        // Search (get) and see that it is updated
    }

    /**
     * Tests the system to delete data from a specific portion of 
     * memory.
     */
    @Test
    public void testDeleteData() {
        // Show the item is there (get)

        // Delete item

        // Seaarch and see that it is no longer there
    }
}