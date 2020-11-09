package com.matchadb.models;

import com.matchadb.MatchaDbRequestType;

/**
 * The Request Object of the MatchaDb system, which is the actual "request" to
 * to be used on the database.
 */
public class MatchaDbRequestObject {

    private MatchaDbRequestType requestType;

    private String queryString;

    public MatchaDbRequestObject(MatchaDbRequestType requestType, String queryString) {
        this.requestType = requestType;
        this.queryString = queryString;
    }

    public MatchaDbRequestType getRequestType() {
        return this.requestType;
    }

    public String getQueryString() {
        return this.queryString;
    }
}