package com.matchadb.models.request;

import com.matchadb.enums.MatchaDbRequestType;

/**
 * This class holds the "raw" request from the Interface, such that the Request
 * Service can reinterperet it into the desired MatchaDbRequestObject.
 */
public class MatchaDbRawRequestObject {

    private MatchaDbRequestType requestType;

    private String requestString;

    /**
     * Constructor for the "Raw" request object.
     */
    public MatchaDbRawRequestObject(MatchaDbRequestType requestType, String requestString) {
        this.requestType = requestType;
        this.requestString = requestString;
    }

    /**
     * Returns the request type.
     */
    public MatchaDbRequestType getRequestType() {
        return this.requestType;
    }

    /**
     * Returns the request string provided from the Interface.
     */
    public String getRequestString() {
        return this.requestString;
    }

}