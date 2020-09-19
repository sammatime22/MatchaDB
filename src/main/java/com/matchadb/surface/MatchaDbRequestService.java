package com.matchadb.surface;

import com.matchadb.models.MatchaDbCommandResult;
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
     * Conducts the request, and returns the response appropriate to the 
     * contents of the request.
     * 
     * @param request The request to be conducted.
     *
     * @return A Response object.
     */
    public MatchaDbResponseObject conductRequest(MatchaDbRequestObject request) {
        return null;
    }

    /**
     * Interprets the result of a Get command, and returns the appropriate
     * result string upstream.
     *
     * @param result The result of running the command.
     *
     * @return A string defining the result of the command.
     */
    public String interpretGetResult(MatchaDbCommandResult result) {
        return null;
    }

    /**
     * Interprets the result of a Post command, and returns the appropriate
     * result string upstream.
     *
     * @param result The result of running the command.
     *
     * @return A string defining the result of the command.
     */
    public String interpretPostResult(MatchaDbCommandResult result) {
        return null;
    }

    /**
     * Interprets the result of a Update command, and returns the appropriate
     * result string upstream.
     *
     * @param result The result of running the command.
     *
     * @return A string defining the result of the command.
     */
    public String interpretUpdateResult(MatchaDbCommandResult result) {
        return null;
    }

    /**
     * Interprets the result of a Delete command, and returns the appropriate
     * result string upstream.
     *
     * @param result The result of running the command.
     *
     * @return A string defining the result of the command.
     */
    public String interpretDeleteResult(MatchaDbCommandResult result) {
        return null;
    }
}