package com.matchadb.models;

/**
 * This object carries the data associated with a response from the database, 
 * along with application metadata, if specified by the settings.
 */
public class MatchaDbResponseObject {
    
    // Provides the user with information on the result of their command.
    private String info;

    // Provides the value associated with the response.
    private Object responseValue;

    public MatchaDbResponseObject(String info, Object responseValue) {
        this.info = info;
        this.responseValue = responseValue;
    }
}