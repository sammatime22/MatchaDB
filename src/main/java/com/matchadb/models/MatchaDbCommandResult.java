package com.matchadb.models;

import java.util.HashMap;

/**
 * A class which describes the result of running a command on the db table.
 */
public class MatchaDbCommandResult {

    private HashMap<String, Object> contents;

    public MatchaDbCommandResult() {
        contents = new HashMap<String, Object>();
    }

    /**
     * Puts a single piece of content into the result.
     *
     * @param content The content to be added.
     */
    public void putResults(Object content) {
        contents.put("Results", content);
    }

    /** 
     * Allows for a more customized version of results 
     * to be added.
     * 
     * @param key The key to the content to be added.
     * @param content The content to be added.
     */
    public void put(String key, Object content) {
        contents.put(key, content);
    }

    /**
     * Allows for the contents to be retrieved.
     *
     * @return The contents to be retrieved.
     */
    public HashMap<String, Object> getContents() {
        return contents;
    }
}