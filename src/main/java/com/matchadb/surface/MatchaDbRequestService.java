package com.matchadb.surface;

import com.matchadb.models.MatchaDbRequestObject;
import com.matchadb.models.MatchaDbResponseObject;

import org.springframework.stereotype.Service;

/**
 * This class takes the request received from the Request Service one step 
 * further and tries to execute said request.
 */
@Service
public class MatchaDbRequestService {
    
    public MatchaDbRequestService() {

    }

    /**
     * Conducts the request by calling on an internal method, and then returns the
     * request result back to the Parser.
     * 
     * @param request The request to be conducted.
     *
     * @return A Response object.
     */
    public MatchaDbResponseObject conductRequest(MatchaDbRequestObject request) {
        return null;
    }

    /**
     * TODO: Documentation
     */
    public MatchaDbResponseObject runGetCommand(String requestQuery) {
        return null;
    }

    /**
     * TODO: Documentation
     */
    public MatchaDbResponseObject runPostCommand(String requestQuery) {
        return null;
    }

    /**
     * TODO: Documentation
     */
    public MatchaDbResponseObject runUpdateCommand(String requestQuery) {
        return null;
    }

    /**
     * TODO: Documentation
     */
    public MatchaDbResponseObject runDeleteCommand(String requestQuery) {
        return null;
    }
}