package com.matchadb.models;

import com.matchadb.enums.MatchaDbRequestType;

/**
 * The Request Object of the MatchaDb system, which is the actual "request" to
 * to be used on the database.
 */
public class MatchaDbRequestObject {

    // The Request Type of choices GET/POST/UPDATE/DELETE
    private MatchaDbRequestType requestType;

    // The actual query provided to the system
    private String queryString;

    public MatchaDbRequestObject(MatchaDbRequestType requestType, String queryString) {
        this.requestType = requestType;
        this.queryString = queryString;
    }

    /**
     * Gets the query provided to the system.
     *
     * @return the query provided to the system.
     */
    public MatchaDbRequestType getRequestType() {
        return this.requestType;
    }

    /**
     * Gets the query string.
     *
     * @return the query string.
     */
    public String getQueryString() {
        return this.queryString;
    }
}