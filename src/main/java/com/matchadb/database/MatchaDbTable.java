package com.matchadb.database;

import com.matchadb.instrumentation.MatchaDbInstrumentationTool;

import com.matchadb.models.MatchaData;
import com.matchadb.models.MatchaDbRequestObject;
import com.matchadb.models.MatchaDbCommandResult;
import com.matchadb.models.MatchaQuery;

import java.io.FileReader;
import java.io.IOException;

import java.lang.instrument.Instrumentation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

    // The actual table which is to have operations run upon it.
    private HashMap<String, Object> table;

    // A Unix timestamp of the time the data was uploaded into the db.
    private long uploadTimeInMillis = 0l;

    // The time of the last update to the database in Unix Timestamp
    private long lastUpdateTimeInMillis = 0l;

    // A boolean describing if the database was filled (ie if data was uploaded)
    private boolean databaseFilled = false;

    // A boolean describing if the database is corrupted somehow
    private boolean databaseCorrupted = false;

    // A List defining the different tables that exist within the db
    private List<String> tables;    

    // An array of all of the titles of relevant metadata.
    private String[] metadataTitles;

    // An array of objects associated with the relevant metadata.
    private Object[] metadataObjects;

    /**
     * Constructor for the DB Table.
     */
    public MatchaDbTable () {

    }

    /**
     * Loads data into the DB Table.
     *
     * @param file A FileReader object that has reference to the filedata.
     */
    public void loadData(FileReader file) {
        JSONParser jsonParser = new JSONParser();

        table = new HashMap<String, Object>();

        try {
            // Parse the incoming FileReader for Data
            Object data = jsonParser.parse(file);

            // Insert the data into the table
            JSONObject tableData = (JSONObject) data;
            this.table = tableBuilder(tableData);

            this.uploadTimeInMillis = System.currentTimeMillis();
            this.lastUpdateTimeInMillis = System.currentTimeMillis();

            this.tables = table.keySet().stream().collect(Collectors.toList());
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
    private HashMap<String, Object> tableBuilder(Object tableData) {
        HashMap<String, Object> tableComponent = new HashMap<String, Object>();

        if (tableData instanceof JSONObject) {
            // if tableData was a JSONObject
            JSONObject jsonObject = (JSONObject) tableData;
            for (Iterator keyIterator = jsonObject.keySet().iterator(); 
                    keyIterator.hasNext();) {
                String key = (String) keyIterator.next();
                // See if it has any children objects
                if (jsonObject.get(key) instanceof JSONObject ||
                    jsonObject.get(key) instanceof JSONArray) {
                    // If so, recursively call the method
                    tableComponent.put(key, tableBuilder(jsonObject.get(key)));
                } else {
                    // Simply add values to the hashmap as expected
                    tableComponent.put(key, jsonObject.get(key));
                }
            }
        } else if (tableData instanceof JSONArray) {
            // if the tableData was a JSONArray
            int position = 0; // For now, maybe we just give numeric indecies
            JSONArray jsonArray = (JSONArray) tableData;
            for (Iterator jsonArrayIterator = jsonArray.iterator(); 
                    jsonArrayIterator.hasNext();) {
                // Get each object and add it to the table
                tableComponent.put(String.valueOf(position++), 
                    tableBuilder(jsonArrayIterator.next()));
            }
        }

        return tableComponent;
    }

    /**
     * Converts the object back to a JSON file and saves it on the system. the 
     * file gets saved onto the filesystem.
     */
    public void saveData() {

    }

    /**
     * Runs the command as prompted by the Request Service.
     *
     * @param requestObject The request made from the Request Service
     *
     * @return A CommandResult object that contains details on the execution.
     */
    public MatchaDbCommandResult runCommand(MatchaDbRequestObject requestObject) {
        return null;
    }

    /**
     * Places the DB Pointer to positions that accurately reflect
     * the data to be manipulated, or the appropriate place for data
     * to be placed.
     *
     * @param query The search conditions
     */
    public void searchForData(MatchaQuery query) {

    }

    /**
     * Returns data depending on where the pointer is.
     *
     * @return The data encapsulated in a MatchaData object.
     */
    public MatchaData getData() {
        return null;
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
     * META-COMMAND
     * 
     * Returns the metadata associated with the database.
     *
     * @return The metadata associated with the database.
     */
    public MatchaDbCommandResult retrieveDbMetadata() {
        MatchaDbCommandResult metadata = new MatchaDbCommandResult();

        metadataTitles = new String[]{"Upload Time", "Last Update Time",
                                      "Filled", "Corrupted", "Tables"};
        metadataObjects = new Object[]{uploadTimeInMillis, 
                                       lastUpdateTimeInMillis, databaseFilled,
                                       databaseCorrupted, tables};


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
        return 0L;//MatchaDbInstrumentationTool.getObjectSize(this.table);
    }
}