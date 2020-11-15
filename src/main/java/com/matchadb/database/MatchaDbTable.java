package com.matchadb.database;

import com.matchadb.instrumentation.MatchaDbInstrumentationTool;

import com.matchadb.models.MatchaData;
import com.matchadb.models.MatchaDbRequestObject;
import com.matchadb.models.MatchaDbCommandResult;
import com.matchadb.models.MatchaQuery;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.instrument.Instrumentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
    private List<Object> table;

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

    /**
     * Constructor for the DB Table.
     */
    public MatchaDbTable (String dropoffPath) {
        this.dropoffPath = dropoffPath;
    }

    /**
     * Loads data into the DB Table.
     *
     * @param file A FileReader object that has reference to the filedata.
     * @param databaseTableName The name of the database table.
     */
    public void loadData(FileReader file, String databaseTableName) {
        JSONParser jsonParser = new JSONParser();
        this.databaseTableName = databaseTableName;

        table = new ArrayList<Object>();

        try {
            // Parse the incoming FileReader for Data
            Object data = jsonParser.parse(file);

            // Insert the data into the table
            this.table = tableBuilder(data);

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
    private List<Object> tableBuilder(Object tableData) {
        List<Object> tableComponent = new ArrayList<Object>();

        if (tableData instanceof JSONObject) {
            tableComponent.add(interpretJSONObject((JSONObject) tableData));
        } else if (tableData instanceof JSONArray) {
            tableComponent = interpretJSONArray((JSONArray) tableData);
        }

        return tableComponent;
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
            if (jsonObject.get(key) instanceof JSONObject) {
                jsonObjectTableComponent.put(key, interpretJSONObject((JSONObject) jsonObject.get(key)));
            } else if (jsonObject.get(key) instanceof JSONArray) {
                jsonObjectTableComponent.put(key, interpretJSONArray((JSONArray) jsonObject.get(key)));
            } else {
                // If the data wasn't of type JSONObject or JSONArray, let's interpret
                // it in the following branching statements
                if (jsonObject.get(key) instanceof Boolean) { 
                    jsonObjectTableComponent.put(key, (boolean) jsonObject.get(key));
                } else if (jsonObject.get(key) instanceof Integer) {
                    jsonObjectTableComponent.put(key, (int) jsonObject.get(key));
                } else if (jsonObject.get(key) instanceof Double) {
                    jsonObjectTableComponent.put(key, (double) jsonObject.get(key));
                } else if (jsonObject.get(key) instanceof String) {
                    jsonObjectTableComponent.put(key, (String) jsonObject.get(key));
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

            if (nextObject instanceof JSONObject) {
                jsonArrayTableComponent.add(interpretJSONObject((JSONObject) nextObject));
            } else if (nextObject instanceof JSONArray) {
                jsonArrayTableComponent.add(interpretJSONArray((JSONArray) nextObject));
            } else {
                // If the data wasn't of type JSONObject or JSONArray, let's interpret
                // it in the following branching statements
                if (nextObject instanceof Boolean) { 
                    jsonArrayTableComponent.add((boolean) nextObject);
                } else if (nextObject instanceof Integer) {
                    jsonArrayTableComponent.add((int) nextObject);
                } else if (nextObject instanceof Double) {
                    jsonArrayTableComponent.add((double) nextObject);
                } else if (nextObject instanceof String) {
                    jsonArrayTableComponent.add((String) nextObject);
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
        JSONArray tableInJSONArrayForm = gatherJSONArrayFromTable(this.table);

        // Still need to determine the right path.
        try (FileWriter fileWriter = new FileWriter(getSaveDataFilename())) {
            fileWriter.write(tableInJSONArrayForm.toString());
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
            if (object instanceof List) {
                jsonArray.add(gatherJSONArrayFromTable((List) object));
            } else if (object instanceof Map) {
                jsonArray.add(gatherJSONObjectFromTable((HashMap) object));
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
            if (object instanceof List) {
                jsonObject.put(objectKey, gatherJSONArrayFromTable((ArrayList<Object>) object));
            } else if (object instanceof Map) {
                jsonObject.put(objectKey, gatherJSONObjectFromTable((HashMap<String, Object>) object));
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
     * @return The data encapsulated in a MatchaData object.
     */
    public List<Object> getData(MatchaQuery query) {
        Object selection = this.table; // Not sure if we really want to set this like this

        // First, perform the dive portion of our query
        for (String diveSelection : query.getDiveSections()) {
            if (selection instanceof List listSelection) {
                // Do the operations to dive down as if this were a list
            } else if (selection instanceof HashMap hashmapSelection) {
                // Do the operations to dive down as if this were a hashmap
            } else {
                // If we couldn't move further down the table, our query is faulty and 
                // we should return an empty list to describe that.
                return new ArrayList<Object>();
            }
        }


        // The values to return should also be more generic, reflective of what
        // we are actually seeing coming in.
        List<Object> valuesToReturn = new ArrayList<Object>();

        // Next, perform the subset query
        // for (Object value : selection.values()) { // Need to find other means to go about this
        //     if (canExist(value, query.getSubsetQuery())) {
        //         // do action
        //     }
        // }

        return valuesToReturn;
    }

    /**
     * Inserts the data into the appropriate position of the table.
     *
     * @return A boolean describing a successful insert.
     */
    public boolean postData() {
        return false;
    }

    /**
     * Updates the data into the appropriate position of the table.
     *
     * @return A boolean describing a successful update.
     */
    public boolean updateData() {
        return false;
    }

    /**
     * Deletes the data into the appropriate position of the table.
     *
     * @return A boolean describing a successful insert.
     */
    public boolean deleteData() {
        return false;
    }

    /**
     * Allows for the subset of a query to take action on a potential candidate
     * for action, whether this candidate matches some regex pattern, has a value
     * within some pattern, or likewise.
     *
     * @param value The value or Object to be identified or have queries enacted 
     *        upon it.
     * @param querySubset The query subset that was passed along into the system.
     */
    public boolean canExist(Object value, String[] querySubset) {
        // We require the following helper methods
            // Regex check
            // Size check
            // Numerical check
        return false;
    }

    /**
     * META-COMMAND
     * 
     * Returns the metadata associated with the database.
     *
     * @return The metadata associated with the database.
     */
    public MatchaDbCommandResult retrieveDbMetadata() {
        MatchaDbCommandResult metadata = new MatchaDbCommandResult();

        metadataTitles = new String[]{"Upload Time", "Last Update Time",
                                      "Filled", "Corrupted", "Table Name"};
        metadataObjects = new Object[]{uploadTimeInMillis, 
                                       lastUpdateTimeInMillis, databaseFilled,
                                       databaseCorrupted, databaseTableName};


        for (int i = 0; i < metadataTitles.length; i++) {
            metadata.put(metadataTitles[i], metadataObjects[i]);
        }

        return metadata;
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