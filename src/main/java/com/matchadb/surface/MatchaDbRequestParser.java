package com.matchadb.surface;

import com.matchadb.enums.MatchaDbRequestType;

import com.matchadb.models.MatchaDbRawRequestObject;
import com.matchadb.models.MatchaDbRequestObject;
import com.matchadb.models.MatchaDbResponseObject;

import org.springframework.stereotype.Service;

/**
 * This service is an intermediary service which determines the 
 * request type and data associated with the request.
 */
@Service
public class MatchaDbRequestParser {

    // Constructor
    public MatchaDbRequestParser() {

    }

    /**
     * This method is the major driver of the service, ingesting the message
     * and sending it off to the following service in the application.
     *
     * @param rawRequest A raw request object, as received at the Interface.
     *
     * @return The response from the database.
     */
    public MatchaDbResponseObject ingestRequest(MatchaDbRawRequestObject rawRequest) {
        return null;
    }

    /**
     * This method runs the other conversion steps.
     *
     * @param rawRequest The raw request to be parsed.
     *
     * @return A fully developed request object, or null if the request is invalid.
     */
    public MatchaDbRequestObject convertRawRequest(MatchaDbRawRequestObject rawRequest) {
        return null;
    }

    /**
     * Gets and returns the enum associated with the request type.
     *
     * @param rawRequest The raw request to be parsed.
     *
     * @return An enum assicated with the request object.
     */
    public MatchaDbRequestType getRequestType(MatchaDbRawRequestObject rawRequest) {
        return null;
    }

    /**
     * Gets the contents of the request, and turns them into an acceptable command
     * for the system.
     *
     * @param rawRequest The raw request to be parsed.
     *
     * @return A string with the command as made up via content from the request.
     */
    public String getRequestContents(MatchaDbRawRequestObject rawRequest) {
        return null;
    }

    /**
     * Calls the appropriate method on the Request Service, and returns the 
     * response upon completion.
     *
     * @param request The request to be used on the db.
     *
     * @return A response object.
     */
    public MatchaDbResponseObject makeRequest(MatchaDbRequestObject request) {
        return null;
    }
}