package com.matchadb.database;

import com.matchadb.instrumentation.MatchaDbInstrumentationTool;

import com.matchadb.models.query.MatchaGetQuery;
import com.matchadb.models.query.MatchaPostQuery;
import com.matchadb.models.query.MatchaUpdateQuery;
import com.matchadb.models.query.MatchaDeleteQuery;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.NumberFormatException;
import java.lang.System;
import java.lang.instrument.Instrumentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

/**
 * The database table, and the methods needed to run commands on the table.
 */
@Service
public class MatchaDbTable {

    private final static Logger logger = LoggerFactory.getLogger(MatchaDbTable.class);

    // The database's table name.
    private String databaseTableName;

    // The JSONParser object used to parse through provided JSON file data.
    private JSONParser parser;

    // A constant for the property name for where the database data will come from.
    private final String DATABASE_FILE = "databaseFile";

    // A constatnt for the property name for where the database name will come from.
    private final String DATABASE_NAME = "databaseName";

    // The path to where data is to be dropped off.
    private String dropoffPath;

    // The actual table which is to have operations run upon it.
    private Object table;

    // A Unix timestamp of the time the data was uploaded into the db.
    private long uploadTimeInMillis = 0l;

    // The time of the last update to the database in Unix Timestamp.
    private long lastUpdateTimeInMillis = 0l;

    // A boolean describing if the database was filled (ie if data was uploaded).
    private boolean databaseFilled = false;

    // A boolean describing if the database is corrupted somehow.
    private boolean databaseCorrupted = false;

    // The extension to a JSON file.
    private final String JSON_EXTENSION = ".json";

    // The position of the object that will be inserted into the table.
    private final int OBJECT_TO_INSERT_INDEX = 0;

    // The position of the Key that should be updated.
    private final int QUERY_UPDATED_KEY_POSITION = 0;

    // The postion of the Value to be updated.
    private final int QUERY_UPDATED_VALUE_POSITION = 2;

    // An asterik, a character that defines "select all" in the search for data.
    private final String SELECT_ALL = "*";

    // The value returned if the index does not exist.
    private final int INDEX_NONEXISTANT = -1;

    // The position of the key in the query subset.
    private final int QUERY_KEY_POSITION = 0;

    // The position in the select query defining what boolean operation should be performed against
    // the queried data.
    private final int QUERY_CHECK_TYPE_POSITION = 1;

    // The position of the value in the query subset.
    private final int QUERY_VALUE_POSITION = 2;

    // Provides inference that the given query expects the queried value should have the 
    // character string provided within the query.
    private final String HAS = "has";

    // Provides inference that the given query expects the queried value should be a 1-to-1
    // String equivalent to the provided string.
    private final String IS = "is";

    // Provides inference that the given query expects the queried value to be equal to
    // the given value.
    private final String EQUALS = "=";

    // Provides inference that the given query expects the queried value to be greater than 
    // the given value.
    private final String GREATER_THAN = ">";

    // Provides inference that the given query expects the queried value to be less than 
    // the given value.
    private final String LESS_THAN = "<";

    /**
     * Constructor for the DB Table.
     *
     * @param dropoffPath The dropoff path for the database data in JSON format when the saveData
     *                    method is called.
     */
    public MatchaDbTable (String dropoffPath) {
        logger.info("Loading MatchaDbTable...");

        this.dropoffPath = dropoffPath;
        if (this.dropoffPath == null) {
            logger.error("No dropoffPath supplied, will not save DB contents on shutdown.");
        }
        this.parser = new JSONParser();

        if (System.getProperty(DATABASE_FILE) != null) {
            String databaseFile = System.getProperty(DATABASE_FILE);
            String databaseName = System.getProperty(DATABASE_NAME);
            try {
                loadData(
                    new FileReader(databaseFile), 
                    databaseName != null ? databaseName : ""
                );
            } catch (FileNotFoundException fnfe) {
                logger.error(String.format("File %s was not found.", databaseFile), fnfe);
            }
        }

        logger.info("MatchaDbTable Loaded!");
    }

    /**
     * Loads data into the DB Table.
     *
     * @param file A FileReader object that has reference to the filedata.
     * @param databaseTableName The name of the database table.
     */
    public void loadData(FileReader file, String databaseTableName) {
        this.databaseTableName = databaseTableName;

        logger.info(String.format("Loading Data for %s", this.databaseTableName));

        try {
            // Parse the incoming FileReader for Data
            Object data = this.parser.parse(file);

            // Insert the data into the table
            tableBuilder(data);

            this.uploadTimeInMillis = System.currentTimeMillis();
            this.lastUpdateTimeInMillis = System.currentTimeMillis();

            this.databaseFilled = true;
            logger.info("MatchaDbTable is filled.");
        } catch (IOException ioe) {
            logger.error("An IOException occurred:\n ", ioe);
            this.databaseCorrupted = true;
        } catch (Exception e) {
            logger.error("An unidentified Exception occurred:\n", e);
            this.databaseCorrupted = true;
        }

        System.out.println(this.table);
    }    

    /**
     * This method calls on itself recursively to help build the database table.
     *
     * @param tableData The table data provided to the DB table.
     *
     * @return A HashMap with the data within the table/subtables.
     */
    private void tableBuilder(Object tableData) {
        if (tableData instanceof JSONObject tableDataAsJSONObject) {
            this.table = interpretJSONObject(tableDataAsJSONObject);
        } else if (tableData instanceof JSONArray tableDataAsJSONArray) {
            this.table = interpretJSONArray(tableDataAsJSONArray);
        }
    }

    /**
     * A recursive helper method that properly constructs HashMaps to repersent JSON Arrays.
     * 
     * @param jsonArray The JSON object to interpret.
     *
     * @return A list representing the interpreted JSON array.
     */
    private List<Object> interpretJSONArray(JSONArray jsonArray) {
        List<Object> jsonArrayTableComponent = new ArrayList<Object>();

        for (Iterator jsonArrayIterator = jsonArray.iterator(); jsonArrayIterator.hasNext();) {
            Object nextObject = jsonArrayIterator.next();

            if (nextObject instanceof JSONObject nextObjectAsJsonObject) {
                jsonArrayTableComponent.add(interpretJSONObject(nextObjectAsJsonObject));
            } else if (nextObject instanceof JSONArray nextObjectAsJsonArray) {
                jsonArrayTableComponent.add(interpretJSONArray(nextObjectAsJsonArray));
            } else {
                // If the data wasn't of type JSONObject or JSONArray, let's interpret
                // it in the following branching statements
                if (nextObject instanceof Boolean nextObjectAsBoolean) { 
                    jsonArrayTableComponent.add(nextObjectAsBoolean);
                } else if (nextObject instanceof Long nextObjectAsLong) {
                    jsonArrayTableComponent.add(nextObjectAsLong);
                } else if (nextObject instanceof Double nextObjectAsDouble) {
                    jsonArrayTableComponent.add(nextObjectAsDouble);
                } else if (nextObject instanceof String nextObjectAsString) {
                    jsonArrayTableComponent.add(nextObjectAsString);
                } else {
                    // If we couldn't figure out the type, we will just turn the object into
                    // a string.
                    jsonArrayTableComponent.add(nextObject.toString());
                }
            }
        }

        return jsonArrayTableComponent;
    }

    /**
     * A recursive helper method that properly constructs HashMaps to repersent JSON Objects.
     * 
     * @param jsonObject The JSON object to interpret.
     *
     * @return A hashmap representing the interpreted JSON object.
     */
    private HashMap<String, Object> interpretJSONObject(JSONObject jsonObject) {
        HashMap<String, Object> jsonObjectTableComponent = new HashMap<String, Object>();

        for (Iterator keyIterator = jsonObject.keySet().iterator(); 
                keyIterator.hasNext();) {
            String key = (String) keyIterator.next();

            if (key.equals("Buy X Items Get One Free Number")) {
                System.out.println("uh " + jsonObject.get(key).toString() + " vlass " + jsonObject.get(key).getClass());
            }
            if (jsonObject.get(key) instanceof JSONObject jsonObjectsValueAsJsonObject) {
                jsonObjectTableComponent.put(key, 
                    interpretJSONObject(jsonObjectsValueAsJsonObject));
            } else if (jsonObject.get(key) instanceof JSONArray jsonObjectsValueAsJsonArray) {
                jsonObjectTableComponent.put(key, interpretJSONArray(jsonObjectsValueAsJsonArray));
            } else {
                // If the data wasn't of type JSONObject or JSONArray, let's interpret
                // it in the following branching statements
                if (jsonObject.get(key) instanceof Boolean jsonObjectsValueAsBoolean) { 
                    jsonObjectTableComponent.put(key, jsonObjectsValueAsBoolean);
                } else if (jsonObject.get(key) instanceof Long jsonObjectsValueAsLong) {
                    jsonObjectTableComponent.put(key, jsonObjectsValueAsLong);
                } else if (jsonObject.get(key) instanceof Double jsonObjectsValueAsDouble) {
                    jsonObjectTableComponent.put(key, jsonObjectsValueAsDouble);
                } else if (jsonObject.get(key) instanceof String jsonObjectsValueAsString) {
                    jsonObjectTableComponent.put(key, jsonObjectsValueAsString);
                } else {
                    // If we couldn't figure out the type, we will just turn the object into
                    // a string.
                    jsonObjectTableComponent.put(key, jsonObject.get(key).toString());
                }
            }
        }

        return jsonObjectTableComponent;
    }

    /**
     * Converts the object back to a JSON file and saves it on the system. 
     */
    public void saveData() {
        Object tableInJSONForm = null;

        if (this.table instanceof List tableAsList) {
            tableInJSONForm = gatherJSONArrayFromTable(tableAsList);
        } else if (this.table instanceof HashMap tableAsHashMap) {
            tableInJSONForm = gatherJSONObjectFromTable(tableAsHashMap);
        }

        if (this.dropoffPath != null) {
            // Still need to determine the right path.
            try (FileWriter fileWriter = new FileWriter(getSaveDataFilename())) {
                if (tableInJSONForm != null) {
                    fileWriter.write(tableInJSONForm.toString());
                    logger.info("MatchaDbTable contents have been written to disk.");
                } else {
                    fileWriter.write("");
                    logger.info("Empty contents have been written to disk.");
                }
            } catch (IOException ioe) {
                logger.error("An IO Exception has occurred:\n ", ioe);
            }
        }
    }

    /**
     * Returns a filename that can be used by the saveData method.
     *
     * @return A filename that can be used by the saveData method.
     */
    private String getSaveDataFilename() {
        return this.dropoffPath + String.valueOf(System.currentTimeMillis()) 
            + this.databaseTableName + JSON_EXTENSION;
    }

    /**
     * This method is a helper method that is used to revert the current table into a JSON Array,
     * particularly to save the database back to a json file.
     *
     * @param listObject The table to be transformed into a JSON Object.
     *
     * @return A JSONObject object, representing the HashMap in object form.
     */
    private JSONArray gatherJSONArrayFromTable(List<Object> tableObject) {
        JSONArray jsonArray = new JSONArray();

        for (Object object : tableObject) {
            if (object instanceof List objectAsList) {
                jsonArray.add(gatherJSONArrayFromTable(objectAsList));
            } else if (object instanceof HashMap objectAsHashMap) {
                jsonArray.add(gatherJSONObjectFromTable(objectAsHashMap));
            } else {
                // Here we will put anything that would be generic data
                if (object instanceof Boolean objectAsBoolean) {
                    jsonArray.add(objectAsBoolean);
                } else if (object instanceof Long objectAsLong) {
                    jsonArray.add(objectAsLong);
                } else if (object instanceof Double objectAsDouble) {
                    jsonArray.add(objectAsDouble);
                } else if (object instanceof String objectAsString) {
                    jsonArray.add(objectAsString);
                } else {
                    // If we couldn't figure out the type, we will just turn the object into
                    // a string.
                    jsonArray.add(object.toString());
                }
            }
        }
        
        return jsonArray;
    }

    /**
     * This method is a helper method that is used to revert the current table into a JSON Array,
     * particularly to save the database back to a json file.
     *
     * @param listObject The table to be transformed into a JSON Object.
     *
     * @return A JSONObject object, representing the HashMap in object form.
     */
    private JSONObject gatherJSONObjectFromTable(HashMap tableObject) {
        JSONObject jsonObject = new JSONObject();

        for (Iterator objectKeyIterator = tableObject.keySet().iterator(); 
            objectKeyIterator.hasNext();) {
            String objectKey = (String) objectKeyIterator.next();
            Object object = tableObject.get(objectKey);
            if (objectKey.equals("Buy X Items Get One Free Number")) {
                System.out.println("uh " + object.toString() + " vlass " + object.getClass());
            }
            if (object instanceof List objectAsList) {
                jsonObject.put(objectKey, gatherJSONArrayFromTable(objectAsList));
            } else if (object instanceof HashMap objectAsHashMap) {
                jsonObject.put(objectKey, gatherJSONObjectFromTable(objectAsHashMap));
            } else {
                // Here we will put anything that would be generic data
                if (object instanceof Boolean objectAsBoolean) {
                    jsonObject.put(objectKey, objectAsBoolean);
                } else if (object instanceof Long objectAsLong) {
                    jsonObject.put(objectKey, objectAsLong);
                } else if (object instanceof Double objectAsDouble) {
                    jsonObject.put(objectKey, objectAsDouble);
                } else if (object instanceof String objectAsString) {
                    jsonObject.put(objectKey, objectAsString);
                } else {
                    // If we couldn't figure out the type, we will just turn the object into
                    // a string.
                    jsonObject.put(objectKey, object.toString());
                }
            }
        }
        
        return jsonObject;
    }

    /**
     * Returns data depending on where the pointer is.
     *
     * @param MatchaQuery The query provided to gather the data.
     *
     * @return The data encapsulated in a MatchaData object.
     */
    public Object getData(MatchaGetQuery query) {
        logger.info(String.format("getData called with contents: %s", query.toString()));

        Object valuesToReturn = null;

        try {
            Object selection = searchForData(query.getFromQuery(), this.table);
            logger.info("Selection Used: " + selection);

            // Next, perform the subset query
            if (selection instanceof List finalListselection) {
                valuesToReturn = new ArrayList<>();
                for (Object value : finalListselection.toArray()) { 
                    if (meetsQueryRequirement(value, query.getSelectQuery())) {
                        ((ArrayList) valuesToReturn).add(value);
                    }
                }

                // Given that we get no values, we will set valuesToReturn back to null so that the
                // service which called the getData method can react appropriately.
                if (((List) valuesToReturn).isEmpty()) {
                    valuesToReturn = null;
                }     
            } else if (selection instanceof HashMap finalHashmapSelection) {
                valuesToReturn = new HashMap<>();

                // If our first select query entry has no length, then we know that the select 
                // query is empty, and we can return the selection gathered.
                if (query.getSelectQuery()[0].length > 0) {
                    System.out.println(query.getSelectQuery().length);
                    for (Iterator finalHashmapSelectionIterator 
                            = finalHashmapSelection.keySet().iterator(); 
                        finalHashmapSelectionIterator.hasNext();) {
                        String key = (String) finalHashmapSelectionIterator.next();
                        Object value = finalHashmapSelection.get(key);
                        if (meetsQueryRequirement(value, query.getSelectQuery())) {
                            ((HashMap) valuesToReturn).put(key, value);
                        }
                    }
                } else {
                    // If we don't have a select query, we will just return the HashMap as is.
                    valuesToReturn = selection;
                }

                // Given that we get no values, we will set valuesToReturn back to null so that the
                // service which called the getData method can react appropriately.
                if (((HashMap) valuesToReturn).isEmpty()) {
                    valuesToReturn = null;
                }
            } else {
                if (meetsQueryRequirement(selection, query.getSelectQuery())) {
                    valuesToReturn = selection;
                }
            }   
        } catch (Exception e) {
            logger.error("An unidentified Exception has occured:\n", e);
            return null;
        }

        logger.info("getData ran successfully.");
        System.out.println(this.table);
        return valuesToReturn;
    }

    /**
     * Inserts the data into the appropriate position of the table.
     *
     * @param query The query to insert data.
     *
     * @return A boolean describing a successful insert.
     */
    public boolean postData(MatchaPostQuery query) {
        logger.info(String.format("postData called with contents: %s", query.toString()));

        try {
            Object selectionToInsertUpon = searchForData(query.getFromQuery(), this.table);

            // If our selection is empty, we will make a new table for said selection
            if (selectionToInsertUpon == null || ((List) selectionToInsertUpon).isEmpty()) {
                selectionToInsertUpon = developNewTable(query.getFromQuery(), this.table);

                if (selectionToInsertUpon == null) {
                    logger.error("Could not develop new subtable");
                    return false;
                }
            }

            for (String[] insertQuery : query.getInsertQuery()) {
                // Build the object
                Object newItem = null;
                for (String entry : insertQuery) {
                    String[] components = entry.split("="); // Split on an equals sign

                    // Hashmap
                    if (components.length >= 2) {
                        if (newItem == null) {
                            newItem = new HashMap<String, Object>();
                        }

                        ((HashMap<String, Object>) newItem)
                            .put(components[0], determineDataType(components[1]));
                    }

                    // List
                    else if (insertQuery.length > 1) {
                        if (newItem == null) {
                            newItem = new ArrayList<Object>();
                        }
                        ((List) newItem).add(determineDataType(components[0]));
                    }

                    // Other
                    else {
                        // If we only have one single item (not a list or hashmap), we can 
                        // conclude that the selection is simply a single "key-value" pair.
                        String[] fromQuerySubquery 
                            = Arrays.copyOfRange(
                                query.getFromQuery(), 0, query.getFromQuery().length - 1);
                        String newKey = query.getFromQuery()[query.getFromQuery().length - 1];
                        selectionToInsertUpon = developNewTable(fromQuerySubquery, this.table);
                        newItem = determineDataType(components[0]);
                        ((HashMap) selectionToInsertUpon)
                            .put(newKey, newItem);
                        break;
                    }
                }

                if (selectionToInsertUpon instanceof HashMap selectionAsHashMap) {
                    selectionAsHashMap.put(
                        query.getFromQuery()[query.getFromQuery().length - 1], newItem
                    ); 
                } else if (selectionToInsertUpon instanceof List selectionAsList) {
                    selectionAsList.add(newItem);
                }
            }

            logger.info("Selection After Insert: " + selectionToInsertUpon);
        } catch (Exception e) {
            logger.error("An unidentified Exception has occurred:\n", e);
            return false;
        }

        this.lastUpdateTimeInMillis = System.currentTimeMillis();
        logger.info("postData ran successfully.");

        System.out.println(this.table + " the table");
        return true;
    }

    /**
     * This method can be used to develop a table, given that it did not exist on the table yet.
     *
     * @param fromQuery The From Query, defining where in the table the data is coming from.
     * @param selection The selection of the table to be searched upon.
     * 
     * @return The new subtable, as a List, which points to a reference on the parent table.
     */
    public Object developNewTable(String[] fromQuery, Object selection) {
        Object returnedSelection = selection;

        for (String fromQueryPortion : fromQuery) {
            if (returnedSelection instanceof List listSelection) {
                int indexOfInterest;

                if (canBeInterpretedAsInteger(fromQueryPortion)) {
                    indexOfInterest = Integer.parseInt(fromQueryPortion);
                } else {
                    indexOfInterest = listSelection.indexOf(fromQueryPortion);
                }

                if (indexOfInterest != INDEX_NONEXISTANT) {
                    returnedSelection = listSelection.get(indexOfInterest);
                } else {
                    // If this entry didn't exist, we'll add it to the table
                    returnedSelection = new HashMap<>(){{
                        put(fromQueryPortion, new ArrayList<>());
                    }};
                    listSelection.add(returnedSelection);
                }
            } else if (returnedSelection instanceof HashMap hashmapSelection) {
                if (hashmapSelection.containsKey(fromQueryPortion)) {
                    returnedSelection = hashmapSelection.get(fromQueryPortion);
                } else {
                    returnedSelection = new ArrayList<>();
                    hashmapSelection.put(fromQueryPortion, returnedSelection);
                }
            } else {
                // If our table is empty, it is possible it is not instantiated.
                if (selection == null) {
                    selection = new HashMap<String, Object>();
                    returnedSelection = selection;
                } else {
                    returnedSelection = null;
                }
            }
        }

        return returnedSelection;
    }

    /**
     * Determines the data type of a value such that it can properly be inserted into an entry.
     *
     * @param value The value to interpret.
     *
     * @return A value in the data type of best fit. 
     */
    private Object determineDataType(String value) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            logger.error("Could not interpret as an integer.", e);
        }

        try {
            return Double.valueOf(value);
        } catch (Exception e) {
            logger.error("Could not interpret as a double.", e);
        }

        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            try { 
                return Boolean.valueOf(value);
            } catch (Exception e) {
                logger.error("Could not interpret as a boolean.", e);
            }
        }
        // Given that we couldn't get any other data type interpreted from the value, we'll just
        // return it back as a String.
        return value;
    }

    /**
     * Updates the data into the appropriate position of the table.
     *
     * @param query The query that will be used to update the DB table.
     *
     * @return A boolean describing a successful update.
     */
    public boolean updateData(MatchaUpdateQuery query) {
        logger.info(String.format("updateData called with contents: %s", query.toString()));

        try {
            Object selection = searchForData(query.getFromQuery(), this.table);

            // Next, perform the subset query
            if (selection instanceof List finalListselection) {
                for (Object value : finalListselection.toArray()) { 
                    if (meetsQueryRequirement(value, query.getSelectQuery())) {
                        for (String[] update : query.getUpdateQuery()) {
                            if (value instanceof HashMap valueAsHashMap) {
                                valueAsHashMap.put(
                                    update[QUERY_UPDATED_KEY_POSITION], 
                                    update[QUERY_UPDATED_VALUE_POSITION]
                                );
                            } else if (value instanceof ArrayList valueAsArrayList) {
                                if (canBeInterpretedAsInteger(update[QUERY_UPDATED_KEY_POSITION])) {
                                    valueAsArrayList.set(
                                        Integer.valueOf(update[QUERY_UPDATED_KEY_POSITION]), 
                                        update[QUERY_UPDATED_VALUE_POSITION]
                                    );
                                } else {
                                    valueAsArrayList.set(
                                        valueAsArrayList.indexOf(update[QUERY_UPDATED_KEY_POSITION]), 
                                        update[QUERY_UPDATED_VALUE_POSITION]
                                    );
                                }
                            }
                            else {
                                // For all other instances, I think we are just literally seting 
                                //"value" to  a new value that's coming in the 2nd slot of the 
                                // updateQuery.
                                value = update[QUERY_UPDATED_VALUE_POSITION];
                            }
                        }
                    }
                }     
            } else if (selection instanceof HashMap finalHashmapSelection) {
                for (Iterator finalHashmapSelectionIterator = 
                    finalHashmapSelection.keySet().iterator(); 
                    finalHashmapSelectionIterator.hasNext();) {

                    String key = (String) finalHashmapSelectionIterator.next();
                    Object value = finalHashmapSelection.get(key);
                    if (meetsQueryRequirement(value, query.getSelectQuery())) {
                        for (String[] update : query.getUpdateQuery()) {
                            if (value instanceof HashMap valueAsHashMap) {
                                valueAsHashMap.put(
                                    update[QUERY_UPDATED_KEY_POSITION], 
                                    update[QUERY_UPDATED_VALUE_POSITION]
                                );
                            } else if (value instanceof ArrayList valueAsArrayList) {
                                if (canBeInterpretedAsInteger(update[QUERY_UPDATED_KEY_POSITION])) {
                                    valueAsArrayList.set(
                                        Integer.valueOf(update[QUERY_UPDATED_KEY_POSITION]), 
                                        update[QUERY_UPDATED_VALUE_POSITION]
                                    );
                                } else {
                                    valueAsArrayList.set(
                                        valueAsArrayList.indexOf(update[QUERY_UPDATED_KEY_POSITION]), 
                                        update[QUERY_UPDATED_VALUE_POSITION]
                                    );
                                }
                            }
                            else {
                                // For all other instances, I think we are just literally seting 
                                // "value" to a new value that's coming in the 2nd slot of the 
                                // updateQuery.
                                value = update[QUERY_UPDATED_VALUE_POSITION];
                            }
                        }
                    }
                }
            } else {
                if (meetsQueryRequirement(selection, query.getSelectQuery())) {
                    for (String[] update : query.getUpdateQuery()) {
                        if (selection instanceof HashMap selectionAsHashMap) {
                            selectionAsHashMap.put(
                                update[QUERY_UPDATED_KEY_POSITION], 
                                update[QUERY_UPDATED_VALUE_POSITION]
                            );
                        } else if (selection instanceof ArrayList selectionAsArrayList) {
                            if (canBeInterpretedAsInteger(update[QUERY_UPDATED_KEY_POSITION])) {
                                selectionAsArrayList.set(
                                    Integer.valueOf(update[QUERY_UPDATED_KEY_POSITION]), 
                                    update[QUERY_UPDATED_VALUE_POSITION]
                                );
                            } else {
                                selectionAsArrayList.set(
                                    selectionAsArrayList.indexOf(
                                    update[QUERY_UPDATED_KEY_POSITION]), 
                                    update[QUERY_UPDATED_VALUE_POSITION]
                                );
                            }
                        }
                        else {
                            // For all other instances, I think we are just literally seting 
                            // "value"to a new value that's coming in the 2nd slot of the 
                            // updateQuery.
                            selection = update[QUERY_UPDATED_VALUE_POSITION];
                        }
                    }
                }
            }

            logger.info("Selection After Update: " + selection);   
        } catch (Exception e) {
            logger.error("An unidentified Exception has occurred:\n", e);
            return false;
        }


        this.lastUpdateTimeInMillis = System.currentTimeMillis();
        logger.info("updateData ran successfully.");
        return true;
    }

    /**
     * Deletes the data into the appropriate position of the table.
     *
     * @param query The delete query to be used on the system.
     *
     * @return A boolean describing a successful insert.
     */
    public boolean deleteData(MatchaDeleteQuery query) {
        logger.info(String.format("deleteData called with contents: %s", query.toString()));

        try {
            deleteDataFromDbTable(query.getFromQuery(), query.getSelectQuery(), this.table);
        } catch (Exception e) {
            logger.error("An unidentified Exception has occurred:\n", e);
            return false;
        }

        this.lastUpdateTimeInMillis = System.currentTimeMillis();
        return true;
    }

    /**
     * A helper method that deletes data from the class level "table" object by removing it's 
     * reference in the table object itself.
     *
     * @param fromQuery The from query used to gather the object.
     * @param selectQuery The select query that will be used to determine if the object is to be 
     *                    deleted.
     * @param tablePortion The table portion to which the data will be removed from.
     */
    private void deleteDataFromDbTable(String[] fromQuery, String[][] selectQuery, 
                                       Object tablePortion) {
        if (fromQuery.length > 0) {
            for (String fromQueryPortion : fromQuery) {
                // Given that we have a Select All query, move through the entire
                if (SELECT_ALL.equals(fromQueryPortion)) {
                    if (tablePortion instanceof List tablePortionAsList) {
                        for (Object tablePortionAsListPortion : tablePortionAsList) {
                            deleteDataFromDbTable(
                                Arrays.copyOfRange(fromQuery, 1, fromQuery.length), selectQuery, 
                                tablePortionAsListPortion
                            );
                        }
                    } else if (tablePortion instanceof HashMap tablePortionAsHashMap) {
                        for (Iterator keyIterator = tablePortionAsHashMap.keySet().iterator(); 
                            keyIterator.hasNext();) {
                            String key = (String) keyIterator.next();
                            deleteDataFromDbTable(
                                Arrays.copyOfRange(fromQuery, 1, fromQuery.length), selectQuery,
                                tablePortionAsHashMap.get(key)
                            );
                        }
                    }
                } else {
                    if (tablePortion instanceof List tablePortionAsList) {
                        deleteDataFromDbTable(
                            Arrays.copyOfRange(fromQuery, 1, fromQuery.length), selectQuery,
                            tablePortionAsList.get(tablePortionAsList.indexOf(fromQueryPortion))
                        );
                    } else if (tablePortion instanceof HashMap tablePortionAsHashMap) {
                        deleteDataFromDbTable(
                            Arrays.copyOfRange(fromQuery, 1, fromQuery.length), selectQuery,
                            tablePortionAsHashMap.get(fromQueryPortion)
                        );
                    }
                }
            }
        } else {
            if (tablePortion instanceof List tablePortionAsList) {
                for (Iterator tablePortionAsListIterator = tablePortionAsList.iterator(); 
                    tablePortionAsListIterator.hasNext();) {
                    Object object = tablePortionAsListIterator.next();
                    if (meetsQueryRequirement(object, selectQuery)) {
                        tablePortionAsListIterator.remove();
                    }
                }
            } else if (tablePortion instanceof HashMap tablePortionAsHashMap) {
                for (Iterator tablePortionAsHashMapIterator 
                    = tablePortionAsHashMap.keySet().iterator();
                    tablePortionAsHashMapIterator.hasNext();) {

                    String key = (String) tablePortionAsHashMapIterator.next();
                    if (meetsQueryRequirement(tablePortionAsHashMap.get(key), selectQuery)) {
                        tablePortionAsHashMapIterator.remove();
                    }
                }
            }
            
            logger.info("Table Portion After Delete: " + tablePortion);
        }
    }

    /**
     * A shared method among the Get, Post, Update, and Delete Data methods, that allows
     * us to search for the appropriate portion of the table to retrieve data from.
     *
     * @param fromQuery The From Query, defining where in the table the data is coming from.
     * @param selection The selection of the table to be searched upon.
     *
     * @return The portion of data to have the action conducted upon.
     */
    private Object searchForData(String[] fromQuery, Object selection) {
        Object returnedSelection = selection;

        for (String fromQueryPortion : fromQuery) {
            if (SELECT_ALL.equals(fromQueryPortion)) {
                if (returnedSelection instanceof List selectionAsList) {
                    returnedSelection = new ArrayList<>();
                    for (Object selectionAsListPortion : selectionAsList) {
                        ((List) returnedSelection).add(
                            searchForData(
                                Arrays.copyOfRange(fromQuery, 1, fromQuery.length), 
                                selectionAsListPortion)
                            );
                    }
                    // We queried the remaining of the "From Query" on the selection portions.
                    // All data has been recursively collected and we no longer need to do any 
                    // queries.
                } else if (returnedSelection instanceof HashMap selectionAsHashMap) {
                    returnedSelection = new ArrayList<>();

                    // For all of the keys in the Hashmap
                    for (Iterator keyIterator = selectionAsHashMap.keySet().iterator(); 
                        keyIterator.hasNext();) {
                        String key = (String) keyIterator.next(); 

                        // Interpolate objects
                        for (Object selectableObjects : Arrays.asList(
                            searchForData(
                                Arrays.copyOfRange(fromQuery, 1, fromQuery.length), 
                                selectionAsHashMap.get(key)))) {

                            // Add all of the objects, if it comes back as a List. Otherwise, just
                            // add the one single object to the returned selection.
                            if (selectableObjects instanceof List selectableObjectsAsList) {
                                for (Object selectableObject : selectableObjectsAsList) {
                                    ((List) returnedSelection).add(selectableObject);
                                }
                            } else {
                                ((List) returnedSelection).add(selectableObjects);
                            }
                        }
                    }
                }
                // else, just return the entire selection
                break;
            } else {
                if (returnedSelection instanceof List listSelection) {
                    int indexOfInterest;

                    if (canBeInterpretedAsInteger(fromQueryPortion)) {
                        indexOfInterest = Integer.parseInt(fromQueryPortion);
                    } else {
                        indexOfInterest = listSelection.indexOf(fromQueryPortion);
                    }

                    if (indexOfInterest != INDEX_NONEXISTANT) {
                        returnedSelection = listSelection.get(indexOfInterest);
                    } else {
                        // If this entry didn't exist, we'll just return an empty list
                        return new ArrayList<>();
                    }
                } else if (returnedSelection instanceof HashMap hashmapSelection) {
                    if (hashmapSelection.containsKey(fromQueryPortion)) {
                        returnedSelection = hashmapSelection.get(fromQueryPortion);
                    } else {
                        return new ArrayList<>();
                    }
                } else {
                    // If we searched down too far, then we can't interpret the query. Return
                    // an empty list as a result.
                    return new ArrayList<>();
                }
            }
        }

        return returnedSelection;
    }

    /**
     * Allows for the select query contents to take action on a potential candidate for action, 
     * whether this candidate matches some regex pattern, has a valuewithin some pattern, or 
     * likewise.
     *
     * Current Query Implementations:
     *     has - Does the object "have" the specific character in their field?
     *     is - Is the object a 1-to-1 string match?
     *     equals - Does the value in the query equal the value in the queried field?
     *              (This relating to numerical based queries only)
     *     less than - Is the value less than what was expected?
     *     greater than - Is the value greater than what was expected?
     *
     * @param value The value or Object to be identified or have queries enacted 
     *        upon it.
     * @param selectQueryContents The select query passed into the system.
     *
     * @return A boolean describing if our value meets our query requirements.
     */
    private boolean meetsQueryRequirement(Object value, String[][] selectQueryContents) {
        if (value instanceof HashMap valueMap) {
            List<Boolean> queryResults = new ArrayList<>();
            if (selectQueryContents[0].length > 0) {
                // Run each subquery, and if all match, finish the method by returning true.
                // Otherwise, return false promptly.
                try {
                    for (String[] selectQuery : selectQueryContents) {
                        // Has query is to be used
                        if (HAS.equals(selectQuery[QUERY_CHECK_TYPE_POSITION])) {

                            // Check to see if the queried value has the substring provided in the query
                            if (!((String) (valueMap.get(selectQuery[QUERY_KEY_POSITION])))
                                .contains(selectQuery[QUERY_VALUE_POSITION]
                                .substring(1, selectQuery[QUERY_VALUE_POSITION].length() - 1)
                            )) { 
                                queryResults.add(false);
                            } else {
                                queryResults.add(true);
                            }
                        } 

                        // An "Is" query is to be used
                        else if (IS.equals(selectQuery[QUERY_CHECK_TYPE_POSITION])) {

                            // Check to see if the provided string is a one-to-one match
                            if (!((String) valueMap.get(selectQuery[QUERY_KEY_POSITION]))
                                .equals(selectQuery[QUERY_VALUE_POSITION])) {
                                queryResults.add(false);
                            } else {
                                queryResults.add(true);
                            }
                        }

                        // Equals query is to be used
                        else if (EQUALS.equals(selectQuery[QUERY_CHECK_TYPE_POSITION])) {

                            // Check to see if the two values are equal
                            if (valueMap.get(selectQuery[QUERY_KEY_POSITION]) 
                                != Double.valueOf(selectQuery[QUERY_VALUE_POSITION])) {
                                queryResults.add(false);
                            } else {
                                queryResults.add(true);
                            }
                        } 

                        // Greater than query is to be used
                        else if (GREATER_THAN.equals(selectQuery[QUERY_CHECK_TYPE_POSITION]) 
                            && canBeInterpretedAsDouble(selectQuery[QUERY_VALUE_POSITION])
                            && canBeInterpretedAsDouble(
                                valueMap.get(selectQuery[QUERY_KEY_POSITION]).toString())) {

                            // Check to see if the table value is larger than the provided value
                            if (Double.valueOf(valueMap.get(selectQuery[QUERY_KEY_POSITION]).toString()) 
                                > Double.valueOf(selectQuery[QUERY_VALUE_POSITION])) {
                                queryResults.add(true);
                            } else {
                                queryResults.add(false);
                            }
                        }

                        // Less than query is to be used 
                        else if (LESS_THAN.equals(selectQuery[QUERY_CHECK_TYPE_POSITION]) 
                            && canBeInterpretedAsDouble(selectQuery[QUERY_VALUE_POSITION])
                            && canBeInterpretedAsDouble(
                                valueMap.get(selectQuery[QUERY_KEY_POSITION]).toString())) {

                            // Check to see if the table value is smaller than the provided value
                            if (Double.valueOf(valueMap.get(selectQuery[QUERY_KEY_POSITION]).toString()) 
                                < Double.valueOf(selectQuery[QUERY_VALUE_POSITION])) {
                                queryResults.add(true);
                            } else {
                                queryResults.add(false);
                            }
                        }

                        else {
                            // For whatever the reason, the query returned no expected results
                            queryResults.add(false);
                        }
                    }

                    for (Boolean queryResult : queryResults) {
                        if (!queryResult) {
                            return false;
                        }
                    }
                } catch (NullPointerException npe) {
                        logger.error("Got a Null Pointer Exception upon assessment.", npe);
                        return false;
                }
            }

            return true;
        } else {
            /**
             * Given that we have gotten a single value, if the select query contents are empty, we
             * can simply return true that we can add the item.
             */
            if (selectQueryContents == null || selectQueryContents[0].length == 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * A small helper method to determine if a String is in fact convertable to 
     * an integer value.
     *
     * (If more "helper" methods accrue that could be used within multiple 
     *  multiple services of the application, we will move this over to a Util
     *  class/service.)
     *
     * TODO: Move to helper functions class
     *
     * @param stringToInterpret The string that will be interpreted if it is an integer/can be 
     *                          turned into an integer.
     *
     * @return A boolean describing if the String could be an integer.
     */
    private boolean canBeInterpretedAsInteger(String stringToInterpret) {
        try {
            Integer.parseInt(stringToInterpret);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * A small helper method to determine if a String is in fact convertable to a double value.
     *
     * TODO: Move to helper functions class
     *
     * @param stringToInterpret The string that will be interpreted if it is an double/can be 
     *                          turned into an double.
     *
     * @return A boolean describing if the String could be an double.
     */
    private boolean canBeInterpretedAsDouble(String stringToInterpret) {
        try {
            Double.parseDouble(stringToInterpret);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * META-COMMAND
     * 
     * Returns the metadata associated with the database.
     *
     * @return The metadata associated with the database.
     */
    public HashMap retrieveDbMetadata() {
        return new HashMap<>(){{
            put("Upload Time", uploadTimeInMillis);
            put("Last Update Time", lastUpdateTimeInMillis);
            put("Filled", databaseFilled);
            put("Corrupted", databaseCorrupted);
            put("Table Name", databaseTableName);
        }};
    }

    /**
     * TODO
     * META-COMMAND
     *
     * Returns the size of the table in Bytes.
     *
     * @return The size of the table in Bytes;     
     */
    public long getTableSizeInBytes() {
        return 0L;
    }
}