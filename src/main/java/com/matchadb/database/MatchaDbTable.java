package com.matchadb.database;

import com.matchadb.instrumentation.MatchaDbInstrumentationTool;

import com.matchadb.models.MatchaData;
import com.matchadb.models.MatchaDbRequestObject;
import com.matchadb.models.query.MatchaGetQuery;
import com.matchadb.models.query.MatchaPostQuery;
import com.matchadb.models.query.MatchaUpdateQuery;
import com.matchadb.models.query.MatchaDeleteQuery;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.NumberFormatException;
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

import org.springframework.stereotype.Service;

/**
 * The database table, and the methods needed to run commands on the table.
 */
@Service
public class MatchaDbTable {

    // The name of the database.
    private String databaseName;

    // The path to where data is to be dropped off.
    private String dropoffPath;

    // The actual table which is to have operations run upon it.
    private Object table;

    private JSONParser parser;

    // A Unix timestamp of the time the data was uploaded into the db.
    private long uploadTimeInMillis = 0l;

    // The time of the last update to the database in Unix Timestamp.
    private long lastUpdateTimeInMillis = 0l;

    // A boolean describing if the database was filled (ie if data was uploaded).
    private boolean databaseFilled = false;

    // A boolean describing if the database is corrupted somehow.
    private boolean databaseCorrupted = false;

    // The databases table name.
    private String databaseTableName;

    // An array of all of the titles of relevant metadata.
    private String[] metadataTitles;

    // An array of objects associated with the relevant metadata.
    private Object[] metadataObjects;

    // The extension to a JSON file.
    private final String JSON_EXTENSION = ".json";

    // A single quotation mark.
    private final String SINGLE_QUOTE = "'";

    // A space.
    private final String SPACE = " ";

    // An asterik, a character that defines "select all" in the search for data.
    private final String SELECT_ALL = "*";

    // The position of the key in the query subset.
    private final int QUERY_KEY_POSITION = 0;

    private final int QUERY_CHECK_TYPE_POSITION = 1;

    // The position of the value in the query subset.
    private final int QUERY_VALUE_POSITION = 2;

    private final int QUERY_UPDATED_KEY_POSITION = 1;

    private final int QUERY_UPDATED_VALUE_POSITION = 2;

    private final int OBJECT_TO_INSERT_INDEX = 0;

    // The value returned if the index does not exist.
    private final int INDEX_NONEXISTANT = -1;

    // The value of the index of an element within a single encapsulated list.
    private final int SINGLE_ENCAPSULATED_LIST_INDEX = 0;

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
     */
    public MatchaDbTable (String dropoffPath) {
        this.dropoffPath = dropoffPath;
        this.parser = new JSONParser();
    }

    /**
     * Loads data into the DB Table.
     *
     * @param file A FileReader object that has reference to the filedata.
     * @param databaseTableName The name of the database table.
     */
    public void loadData(FileReader file, String databaseTableName) {
        this.databaseTableName = databaseTableName;

        table = new ArrayList<Object>();

        try {
            // Parse the incoming FileReader for Data
            Object data = this.parser.parse(file);

            // Insert the data into the table
            tableBuilder(data);

            this.uploadTimeInMillis = System.currentTimeMillis();
            this.lastUpdateTimeInMillis = System.currentTimeMillis();

            this.databaseFilled = true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            this.databaseCorrupted = true;
        } catch (Exception e) {
            e.printStackTrace();
            this.databaseCorrupted = true;
        }
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
            if (jsonObject.get(key) instanceof JSONObject jsonObjectsValueAsJsonObject) {
                jsonObjectTableComponent.put(key, interpretJSONObject(jsonObjectsValueAsJsonObject));
            } else if (jsonObject.get(key) instanceof JSONArray jsonObjectsValueAsJsonArray) {
                jsonObjectTableComponent.put(key, interpretJSONArray(jsonObjectsValueAsJsonArray));
            } else {
                // If the data wasn't of type JSONObject or JSONArray, let's interpret
                // it in the following branching statements
                if (jsonObject.get(key) instanceof Boolean jsonObjectsValueAsBoolean) { 
                    jsonObjectTableComponent.put(key, jsonObjectsValueAsBoolean);
                } else if (jsonObject.get(key) instanceof Integer jsonObjectsValueAsInteger) {
                    jsonObjectTableComponent.put(key, jsonObjectsValueAsInteger);
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
                } else if (nextObject instanceof Integer nextObjectAsInteger) {
                    jsonArrayTableComponent.add(nextObjectAsInteger);
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
     * Converts the object back to a JSON file and saves it on the system. 
     */
    public void saveData() {
        Object tableInJSONForm = null;

        if (this.table instanceof List tableAsList) {
            tableInJSONForm = gatherJSONArrayFromTable(tableAsList);
        } else if (this.table instanceof HashMap tableAsHashMap) {
            tableInJSONForm = gatherJSONObjectFromTable(tableAsHashMap);
        }

        // Still need to determine the right path.
        try (FileWriter fileWriter = new FileWriter(getSaveDataFilename())) {
            if (tableInJSONForm != null) {
                fileWriter.write(tableInJSONForm.toString());
            } else {
                fileWriter.write("");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Returns a filename that can be used by the saveData method.
     *
     * @return A filename that can be used by the saveData method.
     */
    private String getSaveDataFilename() {
        return this.dropoffPath + String.valueOf(System.currentTimeMillis()) 
            + this.databaseName + JSON_EXTENSION;
    }

    /**
     * This method is a helper method that is used to revert the current table
     * into a JSON Array, particularly to save the database back to a json file.
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
                jsonArray.add(object);
            }
        }
        
        return jsonArray;
    }

    /**
     * This method is a helper method that is used to revert the current table
     * into a JSON Array, particularly to save the database back to a json file.
     *
     * @param listObject The table to be transformed into a JSON Object.
     *
     * @return A JSONObject object, representing the HashMap in object form.
     */
    private JSONObject gatherJSONObjectFromTable(HashMap tableObject) {
        JSONObject jsonObject = new JSONObject();

        for (Iterator objectKeyIterator = tableObject.keySet().iterator(); objectKeyIterator.hasNext();) {
            String objectKey = (String) objectKeyIterator.next();
            Object object = tableObject.get(objectKey);
            if (object instanceof List objectAsList) {
                jsonObject.put(objectKey, gatherJSONArrayFromTable(objectAsList));
            } else if (object instanceof HashMap objectAsHashMap) {
                jsonObject.put(objectKey, gatherJSONObjectFromTable(objectAsHashMap));
            } else {
                // Here we will put anything that would be generic data
                jsonObject.put(objectKey, object);
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
        Object selection = searchForData(query.getFromQuery(), this.table);


        // We should make sure the values to return are in a generic object.
        // We don't know what the User had asked for, and we want to be as
        // dynamic as possible.
        Object valuesToReturn = null;

        // Next, perform the subset query
        if (selection instanceof List finalListselection) {
            valuesToReturn = new ArrayList<>();
            for (Object value : finalListselection.toArray()) { 
                if (meetsQueryRequirement(value, query.getSelectQuery())) {
                    ((ArrayList) valuesToReturn).add(value);
                }
            }     
        } else if (selection instanceof HashMap finalHashmapSelection) {
            valuesToReturn = new HashMap<>();
            for (Iterator finalHashmapSelectionIterator = finalHashmapSelection.keySet().iterator(); 
                finalHashmapSelectionIterator.hasNext();) {
                String key = (String) finalHashmapSelectionIterator.next();
                Object value = finalHashmapSelection.get(key);
                if (meetsQueryRequirement(value, query.getSelectQuery())) {
                    ((HashMap) valuesToReturn).put(key, value);
                }
            }
        } else {
            if (meetsQueryRequirement(selection, query.getSelectQuery())) {
                valuesToReturn = selection;
            }
        }   

        return valuesToReturn;
    }

    /**
     * Inserts the data into the appropriate position of the table.
     *
     * @param query The query to insert data.
     *
     * @return A boolean describing a successful insert.
     */
    public boolean postData(MatchaPostQuery query) throws ParseException {

        try {
            Object selectionToInsertUpon = searchForData(query.getFromQuery(), this.table);

            for (String[] insertQuery : query.getInsertQuery()) {
                HashMap<String, Object> newItem = 
                    interpretJSONObject((JSONObject) this.parser.parse(insertQuery[OBJECT_TO_INSERT_INDEX]));
                if (selectionToInsertUpon instanceof HashMap selectionAsHashMap) {
                    selectionAsHashMap.put(query.getFromQuery()[query.getFromQuery().length - 1], newItem); 
                } else if (selectionToInsertUpon instanceof List selectionAsList) {
                    selectionAsList.add(newItem);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Updates the data into the appropriate position of the table.
     *
     * @param query The query that will be used to update the DB table.
     *
     * @return A boolean describing a successful update.
     */
    public boolean updateData(MatchaUpdateQuery query) {
        Object selection = searchForData(query.getFromQuery(), this.table);

        // Next, perform the subset query
        try {
            if (selection instanceof List finalListselection) {
                for (Object value : finalListselection.toArray()) { 
                    if (meetsQueryRequirement(value, query.getSelectQuery())) {
                        for (String[] update : query.getUpdateQuery()) {
                            if (value instanceof HashMap valueAsHashMap) {
                                valueAsHashMap.put(update[QUERY_UPDATED_KEY_POSITION], update[QUERY_UPDATED_VALUE_POSITION]);
                            } else if (value instanceof ArrayList valueAsArrayList) {
                                if (canBeInterpretedAsInteger(update[QUERY_UPDATED_KEY_POSITION])) {
                                    valueAsArrayList.set(Integer.valueOf(update[QUERY_UPDATED_KEY_POSITION]), update[QUERY_UPDATED_VALUE_POSITION]);
                                } else {
                                    valueAsArrayList.set(
                                        valueAsArrayList.indexOf(update[QUERY_UPDATED_KEY_POSITION]), 
                                        update[QUERY_UPDATED_VALUE_POSITION]
                                    );
                                }
                            }
                            else {
                                // For all other instances, I think we are just literally seting "value" to 
                                // a new value that's coming in the 2nd slot of the updateQuery.
                                value = update[QUERY_UPDATED_VALUE_POSITION];
                            }
                        }
                    }
                }     
            } else if (selection instanceof HashMap finalHashmapSelection) {
                for (Iterator finalHashmapSelectionIterator = finalHashmapSelection.keySet().iterator(); 
                    finalHashmapSelectionIterator.hasNext();) {
                    String key = (String) finalHashmapSelectionIterator.next();
                    Object value = finalHashmapSelection.get(key);
                    if (meetsQueryRequirement(value, query.getSelectQuery())) {
                        for (String[] update : query.getUpdateQuery()) {
                            if (value instanceof HashMap valueAsHashMap) {
                                valueAsHashMap.put(update[QUERY_UPDATED_KEY_POSITION], update[QUERY_UPDATED_VALUE_POSITION]);
                            } else if (value instanceof ArrayList valueAsArrayList) {
                                if (canBeInterpretedAsInteger(update[QUERY_UPDATED_KEY_POSITION])) {
                                    valueAsArrayList.set(Integer.valueOf(update[QUERY_UPDATED_KEY_POSITION]), update[QUERY_UPDATED_VALUE_POSITION]);
                                } else {
                                    valueAsArrayList.set(
                                        valueAsArrayList.indexOf(update[QUERY_UPDATED_KEY_POSITION]), 
                                        update[QUERY_UPDATED_VALUE_POSITION]
                                    );
                                }
                            }
                            else {
                                // For all other instances, I think we are just literally seting "value" to 
                                // a new value that's coming in the 2nd slot of the updateQuery.
                                value = update[QUERY_UPDATED_VALUE_POSITION];
                            }
                        }
                    }
                }
            } else {
                if (meetsQueryRequirement(selection, query.getSelectQuery())) {
                    for (String[] update : query.getUpdateQuery()) {
                        if (selection instanceof HashMap selectionAsHashMap) {
                            selectionAsHashMap.put(update[QUERY_UPDATED_KEY_POSITION], update[QUERY_UPDATED_VALUE_POSITION]);
                        } else if (selection instanceof ArrayList selectionAsArrayList) {
                            if (canBeInterpretedAsInteger(update[QUERY_UPDATED_KEY_POSITION])) {
                                selectionAsArrayList.set(Integer.valueOf(update[QUERY_UPDATED_KEY_POSITION]), update[QUERY_UPDATED_VALUE_POSITION]);
                            } else {
                                selectionAsArrayList.set(
                                    selectionAsArrayList.indexOf(update[QUERY_UPDATED_KEY_POSITION]), 
                                    update[QUERY_UPDATED_VALUE_POSITION]
                                );
                            }
                        }
                        else {
                            // For all other instances, I think we are just literally seting "value" to 
                            // a new value that's coming in the 2nd slot of the updateQuery.
                            selection = update[QUERY_UPDATED_VALUE_POSITION];
                        }
                    }
                }
            }   
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
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
        return false;
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
                                Arrays.copyOfRange(fromQuery, 1, fromQuery.length), selectionAsListPortion));
                    }
                    // We queried the remaining of the "From Query" on the selection portions.
                    // All data has been recursively collected and we no longer need to do any queries.
                    break;
                } // else, don't search further down
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

        if (returnedSelection != null) {
            return returnedSelection;
        } else {
            return null;
        }
    }

    /**
     * Allows for the select query contents to take action on a potential candidate
     * for action, whether this candidate matches some regex pattern, has a value
     * within some pattern, or likewise.
     *
     * Current Query Implementations:
     *     has - Does the object "have" the specific character in their field?
     *     equals - Does the value in the query equal the value in the queried field?
     *              (This relating to numerical based queries only)
     *     less than - Is the value less than what was expected?
     *     greater than - Is the value greater than what was expected?
     * To Implement:
     *     is - Is the object a 1-to-1 string match?
     *
     *
     * @param value The value or Object to be identified or have queries enacted 
     *        upon it.
     * @param selectQueryContents The select query passed into the system.
     *
     * @return A boolean describing if our value meets our query requirements.
     */
    private boolean meetsQueryRequirement(Object value, String[][] selectQueryContents) {
        HashMap<String, Object> valueMap = ((HashMap) value);
        List<Boolean> queryResults = new ArrayList<>();

        // Run each subquery, and if all match, finish the method by returning true.
        // Otherwise, return false promptly.
        for (String[] selectQuery : selectQueryContents) {
            // has query
            if (HAS.equals(selectQuery[QUERY_CHECK_TYPE_POSITION])) {
                if (!((String) (valueMap.get(selectQuery[QUERY_KEY_POSITION]))).contains(
                    selectQuery[QUERY_VALUE_POSITION].substring(1, selectQuery[QUERY_VALUE_POSITION].length() - 1)
                )) { 
                    queryResults.add(false);
                } else {
                    queryResults.add(true);
                }
            } 
            // equals query
            else if (EQUALS.equals(selectQuery[QUERY_CHECK_TYPE_POSITION])) {
                // Run a numerical check on the params
                if (valueMap.get(selectQuery[QUERY_KEY_POSITION]) 
                        != Integer.valueOf(selectQuery[QUERY_VALUE_POSITION])) {
                    queryResults.add(false);
                } else {
                    queryResults.add(true);
                }
            } 
            // Greater than query
            else if (GREATER_THAN.equals(selectQuery[QUERY_CHECK_TYPE_POSITION]) 
                && canBeInterpretedAsDouble(selectQuery[QUERY_VALUE_POSITION])
                && canBeInterpretedAsDouble(valueMap.get(selectQuery[QUERY_KEY_POSITION]).toString())) {
                if (Double.valueOf(valueMap.get(selectQuery[QUERY_KEY_POSITION]).toString()) > 
                    Double.valueOf(selectQuery[QUERY_VALUE_POSITION])) {
                    queryResults.add(true);
                } else {
                    queryResults.add(false);
                }
            }
            // Less than
            else if (LESS_THAN.equals(selectQuery[QUERY_CHECK_TYPE_POSITION]) 
                && canBeInterpretedAsDouble(selectQuery[QUERY_VALUE_POSITION])
                && canBeInterpretedAsDouble(valueMap.get(selectQuery[QUERY_KEY_POSITION]).toString())) {
                if (Double.valueOf(valueMap.get(selectQuery[QUERY_KEY_POSITION]).toString()) < 
                    Double.valueOf(selectQuery[QUERY_VALUE_POSITION])) {
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

        return true;
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
     * @param stringToInterpret The string that will be interpreted if it is an 
     *                          integer/can be turned into an integer.
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
     * A small helper method to determine if a String is in fact convertable to 
     * a double value.
     *
     * TODO: Move to helper functions class
     *
     * @param stringToInterpret The string that will be interpreted if it is an 
     *                          integer/can be turned into an integer.
     *
     * @return A boolean describing if the String could be an integer.
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