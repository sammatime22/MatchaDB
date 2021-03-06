package com.matchadb.database;

import com.matchadb.generate.MatchaDbGenerateData;

import com.matchadb.models.query.MatchaGetQuery;
import com.matchadb.models.query.MatchaPostQuery;
import com.matchadb.models.query.MatchaUpdateQuery;
import com.matchadb.models.query.MatchaDeleteQuery;

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

/**
 * Tests the DB Table methods.
 */
public class MatchaDbTableTest {

    // The name of the Clothes website API.
    private final String TEST_FILE_CLOTHES_WEBSITE_API = "TestFileClothesWebsiteAPI";

    // The relative filepath to the Clothes website API JSON file.
    private final String TEST_FILE_CLOTHES_WEBSITE_API_JSON_FILE 
        = "src/test/java/com/matchadb/resources/TestFileClothesWebsiteAPI.json";

    // An empty dropoff path - Used when we won't be using the "saveData" method.
    private final String EMPTY_DROPOFF_PATH = "";

    // Keys for all of the objects in the Clothes website API table
    private final String ITEM_NAME = "Item Name";

    private final String ITEM_BRAND = "Item Brand";

    private final String ITEM_PRICE = "Item Price";

    private final String ITEM_DESCRIPTION = "Item Description";

    // Table names within the Clothing website API.
    private final String SHIRTS_TABLE = "Shirts";

    private final String HATS_TABLE = "Hats";

    // Different table operators.
    private final String HAS_OPERATION = "has";

    private final String IS_OPERATION = "is";

    private final String LESS_THAN = "<";

    private final String GREATER_THAN = ">";

    private final String ALL_TABLES = "*";

    // Different queries usable on the table.
    private final String UPDATE = "Update";

    MatchaDbTable matchaDbTable;

    /**
     * The following test checks to see that our Load Data method works appropriately.
     */
    @Test
    public void testLoadDataTestFileClothesWebsiteAPI() {
        HashMap<String, Object> expectedTable 
            = MatchaDbGenerateData.generateClothesWebsiteAPITable();
        String tableName = TEST_FILE_CLOTHES_WEBSITE_API;

        // Location of the test file used
        String filename = TEST_FILE_CLOTHES_WEBSITE_API_JSON_FILE;

        // Database name
        String databaseName = TEST_FILE_CLOTHES_WEBSITE_API;

        // A timestamp before the start of the test, to make sure the timestamps in the db
        // are updated.
        long timeBeforeDBLoadAndLastUpdate = System.currentTimeMillis();

        // Instantiating the matchaDbTable Instance
        matchaDbTable = new MatchaDbTable(EMPTY_DROPOFF_PATH);
        
        try {
            // Load in our real table
            matchaDbTable.loadData(new FileReader(filename), tableName);

            // Check to see that the metadata is appropriate
            HashMap<String, Object> metadataContents = matchaDbTable.retrieveDbMetadata();

            // Check on Timestamps
            Assert.assertTrue(timeBeforeDBLoadAndLastUpdate 
                <= (long) metadataContents.get("Upload Time"));
            Assert.assertTrue(timeBeforeDBLoadAndLastUpdate 
                <= (long) metadataContents.get("Last Update Time"));

            // Check Flags
            Assert.assertTrue((boolean) metadataContents.get("Filled"));
            Assert.assertTrue(!(boolean) metadataContents.get("Corrupted"));
            
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
        String filename = TEST_FILE_CLOTHES_WEBSITE_API_JSON_FILE;        

        boolean failed = false;

        try {
            // Load in the data for the DB
            matchaDbTable.loadData(new FileReader(filename), TEST_FILE_CLOTHES_WEBSITE_API);

            // Save the data and check that the data within the file is as expected.
            matchaDbTable.saveData();

            // With the exception of tabs, spaces, or returns, check that each character in the 
            // file matches.
            File testFile = new File(testDirectoryFileObject.list()[testFileLocation]);
            try (FileReader testFileReader = new FileReader(testDirectory + testFile); 
                FileReader templateFileReader 
                    = new FileReader(new File(TEST_FILE_CLOTHES_WEBSITE_API_JSON_FILE))) {

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
                    if (templateCharacterValue == endOfFile 
                        && generatedFileCharacterValue == endOfFile) {
                        break; // Done comparing the two files!
                    }
                    if ((templateCharacterValue != endOfFile 
                            && generatedFileCharacterValue == endOfFile)
                        || (templateCharacterValue == endOfFile 
                            && generatedFileCharacterValue != endOfFile) 
                        || (templateCharacterValue != generatedFileCharacterValue)) {
                        // Either one of the files is larger than the other, or the characters do 
                        // not match.
                        System.out.println("Failed in comparison for " + templateCharacterValue
                            + " and " + generatedFileCharacterValue + " @" + place);
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
     * Tests the system to get data from a specific portion of 
     * memory.
     */
    @Test
    public void testGetData() {
        // Location of the test file used
        String filename = TEST_FILE_CLOTHES_WEBSITE_API_JSON_FILE;

        // We want to set up the MatchaQuery object for the following query
        // Select * from * where Item Name contains 's'
        // And the item is from the "Shoes" table
        List<HashMap<String, Object>> expectedObjects 
            = MatchaDbGenerateData
                .getClothesWebsiteItemsViaQueryParams("s", null, null, null, null, SHIRTS_TABLE);

        MatchaGetQuery matchaQuery = new MatchaGetQuery(new String[]{SHIRTS_TABLE}, 
            new String[][]{{ITEM_NAME, HAS_OPERATION, "'s'"}});

        matchaDbTable = new MatchaDbTable(EMPTY_DROPOFF_PATH);

        try {
            // Load in the data for the DB
            matchaDbTable.loadData(new FileReader(filename), TEST_FILE_CLOTHES_WEBSITE_API);
            List<HashMap<String, Object>> actualObjects = 
                (List<HashMap<String, Object>>) matchaDbTable.getData(matchaQuery);

            // Find the matching object from our mock data, and check to see that the contents 
            // match.
            expectedVersusActualClothingWebsiteAPICheck(expectedObjects, actualObjects);
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
        String fancyHatToAddAsJSON = MatchaDbGenerateData.newClothesItemToInsert();

        // Query to get item
        MatchaGetQuery matchaQueryGetFancyHat = new MatchaGetQuery(new String[] {HATS_TABLE},
            new String[][] {{ITEM_NAME, HAS_OPERATION, "'Trendy Hat'"}}
        );

        // Query to insert item, which in JSON might come in as...
        // "Place": "To Hats", 
        // "Insert": ""{ \"Item Name\": \"Trendy Hat\", \"Item Brand\": \"qwertu\"," +
        //        "\"Item Description\": \"A hat with a feather for a feather.\", 
        // \"Item Price\": 9000000.95 }""
        MatchaPostQuery matchaQueryInsertFancyHat = new MatchaPostQuery(new String[] {HATS_TABLE},
            new String[][] {{}},
            new String[][] {{fancyHatToAddAsJSON}}
        );
        matchaDbTable = new MatchaDbTable(EMPTY_DROPOFF_PATH);
        String filename = TEST_FILE_CLOTHES_WEBSITE_API_JSON_FILE;

        try {
            // Maybe consider a before method..?
            // Load in the data for the DB
            matchaDbTable.loadData(new FileReader(filename), TEST_FILE_CLOTHES_WEBSITE_API);
            
            // Show the item is not there (get)
            matchaDbTable.getData(matchaQueryGetFancyHat);
            if (((List)matchaDbTable.getData(matchaQueryGetFancyHat)).size() > 0) {
                Assert.fail();
            }

            // Add new item, fail if the method fails.
            if (!matchaDbTable.postData(matchaQueryInsertFancyHat)) {
                Assert.fail();
            }

            // Search (get) and see that it is where it is expected.
            List<HashMap<String,Object>> fancyHatQueryResults = 
                (List<HashMap<String, Object>>) matchaDbTable.getData(matchaQueryGetFancyHat);
            if (fancyHatQueryResults.size() == 1) {
                HashMap<String, Object> fancyHat = fancyHatQueryResults.get(0);
                if (fancyHat != null) {
                    // Check the contents to see that they are correct; Otherwise, fail.
                    if (
                        !"Trendy Hat".equals(fancyHat.get(ITEM_NAME)) ||
                        !"qwertu".equals(fancyHat.get(ITEM_BRAND)) ||
                        (double) fancyHat.get(ITEM_PRICE) != 9000000.95 ||
                        !"A hat with a feather for a feather."
                            .equals(fancyHat.get(ITEM_DESCRIPTION))
                    ) {
                        Assert.fail();
                    }
                } else {
                    Assert.fail();
                }
            } else {
                Assert.fail();
            }
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
        String newPrice = "18.91";
        String newBrand = "The Eighteen";

        MatchaGetQuery matchaQueryGet = new MatchaGetQuery(new String[]{ALL_TABLES}, 
            new String[][]{{ITEM_PRICE, GREATER_THAN, "15.00"}, {ITEM_PRICE, LESS_THAN, "20.00"}});
        
        MatchaUpdateQuery matchaQueryUpdate = new MatchaUpdateQuery(new String[]{ALL_TABLES}, 
            new String[][]{{ITEM_PRICE, GREATER_THAN, "15.00"}, {ITEM_PRICE, LESS_THAN, "20.00"}},
            new String[][]{{UPDATE, ITEM_PRICE, newPrice}, {UPDATE, ITEM_BRAND, newBrand}});

        matchaDbTable = new MatchaDbTable(EMPTY_DROPOFF_PATH);

        List<HashMap<String, Object>> expectedObjects = 
            MatchaDbGenerateData
                .getClothesWebsiteItemsViaQueryParams(null, null, null, 15.00, 20.00, null);

        try {
            matchaDbTable.loadData(
                    new FileReader(TEST_FILE_CLOTHES_WEBSITE_API_JSON_FILE), 
                    TEST_FILE_CLOTHES_WEBSITE_API
                );

            // Show the items are unmodified (get)
            List<HashMap<String, Object>> actualObjects = 
                (List<HashMap<String, Object>>) matchaDbTable.getData(matchaQueryGet);

            // Find the matching object from our mock data, and check to see that the contents 
            // match.
            expectedVersusActualClothingWebsiteAPICheck(expectedObjects, actualObjects);

            // Update items
            // Let's say for all items with a price greater than $16.00 but less than $20.00, 
            // update their brand to "The Eighteen", and change their price to $18.91
            if (!matchaDbTable.updateData(matchaQueryUpdate)) {
                // Just see that updates went okay
                Assert.fail();
            }

            // Search (get) and see that it is updated
            // This should be the Baseball Hat, Dad Hat, and Beanie
            actualObjects = (List<HashMap<String, Object>>) matchaDbTable.getData(matchaQueryGet);

            // For loop, see that some comparisons fail and that new expected comparisons pass
            for (HashMap expectedObject : expectedObjects) {
                boolean success = false;
                for (HashMap actualObject : actualObjects) {
                    if (expectedObject.get(ITEM_NAME).equals(actualObject.get(ITEM_NAME))) {
                        if (actualObject.get(ITEM_PRICE).equals(newPrice) 
                            && actualObject.get(ITEM_BRAND).equals(newBrand) 
                            && expectedObject.get(ITEM_DESCRIPTION)
                                .equals(actualObject.get(ITEM_DESCRIPTION))) {
                            success = true;
                            break;
                        } else {
                            // The object didn't have the right/new attributes for some reason
                            Assert.fail();
                        }
                    }
                }

                // We didn't find the expected object
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
     * Tests the system to delete data from a specific portion of 
     * memory.
     */
    @Test
    public void testDeleteData() {
        /**
         * We are going to test deleting all items of the brand ghjkl from the database.
         */
        MatchaGetQuery matchaQueryGetAll = new MatchaGetQuery(
            new String[] {ALL_TABLES},
            new String[][] {{ITEM_PRICE, GREATER_THAN, "1.00"}}
        );

        MatchaDeleteQuery matchaDeleteQuery = new MatchaDeleteQuery(
            new String[] {ALL_TABLES},
            new String[][] {{ITEM_BRAND, IS_OPERATION, "zxcvb"}} // Change this to equals operation
        );
        
        List<HashMap<String, Object>> allItems 
            = MatchaDbGenerateData
                .getClothesWebsiteItemsViaQueryParams(null, null, null, null, null, null);

        List<HashMap<String, Object>> allItemsFromzxcvb 
            = MatchaDbGenerateData
                .getClothesWebsiteItemsViaQueryParams(null, "zxcvb", null, null, null, null);        

        matchaDbTable = new MatchaDbTable(EMPTY_DROPOFF_PATH);

        try {   
            // Show all items are in the table
            matchaDbTable
                .loadData(
                    new FileReader(TEST_FILE_CLOTHES_WEBSITE_API_JSON_FILE), 
                    TEST_FILE_CLOTHES_WEBSITE_API
                );

            // Show the items are unmodified (get)
            List<HashMap<String, Object>> actualObjects = 
                (List<HashMap<String, Object>>) matchaDbTable.getData(matchaQueryGetAll);

            // Find the matching object from our mock data, and check to see that the contents 
            // match.  
            expectedVersusActualClothingWebsiteAPICheck(allItems, actualObjects);            

            // Delete all items of the brand "zxcvb"
            if (!matchaDbTable.deleteData(matchaDeleteQuery)) {
                // Something happened when attempting to use our delete query.
                Assert.fail();
            }

            // Seaarch and see that no more "zxcvb" brand items are in the table
            List<HashMap<String, Object>> actualObjectsNozxcvbBrand = 
                (List<HashMap<String, Object>>) matchaDbTable.getData(matchaQueryGetAll);            


            // For each object returned, was this an object we shouldn't have gotten?
            for (HashMap actualObject : actualObjectsNozxcvbBrand) {
                for (HashMap itemFromzxcvb : allItemsFromzxcvb) {
                    if (actualObject.get(ITEM_NAME).equals(itemFromzxcvb.get(ITEM_NAME)) &&
                        actualObject.get(ITEM_BRAND).equals(itemFromzxcvb.get(ITEM_BRAND)) &&
                        actualObject.get(ITEM_DESCRIPTION)
                            .equals(itemFromzxcvb.get(ITEM_DESCRIPTION)) &&
                        actualObject.get(ITEM_PRICE).equals(itemFromzxcvb.get(ITEM_PRICE))) {
                        Assert.fail();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
     * A method that checks between an expected and actual list of Clothing Website
     * API objects and fails if:
     *
     * A) An object in the expected list doesn't exist in the actual list
     * B) The object doesn't have the expected attributes
     *
     * @param expectedObjects The objects expected to be returned
     * @param actualObjects The actual objects returned
     */
    public void expectedVersusActualClothingWebsiteAPICheck(
        List<HashMap<String, Object>> expectedObjects, 
        List<HashMap<String, Object>> actualObjects) {
            for (HashMap expectedObject : expectedObjects) {
                boolean success = false;
                for (HashMap actualObject : actualObjects) {
                    if (expectedObject.get(ITEM_NAME).equals(actualObject.get(ITEM_NAME))) {
                        if (expectedObject.get(ITEM_PRICE).equals(actualObject.get(ITEM_PRICE)) 
                            && expectedObject.get(ITEM_BRAND).equals(actualObject.get(ITEM_BRAND))
                            && expectedObject.get(ITEM_DESCRIPTION)
                                .equals(actualObject.get(ITEM_DESCRIPTION))) {
                            success = true;
                            break;
                        } else {
                            // The object didn't have the right/original attributes for some reason
                            Assert.fail();
                        }
                    }
                }

                // We didn't find the expected object
                if (!success) {
                    Assert.fail();
                }
            }
    }
}