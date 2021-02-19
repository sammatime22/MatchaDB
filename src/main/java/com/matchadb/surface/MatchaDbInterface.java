package com.matchadb.surface;

import com.matchadb.models.request.MatchaDbRawRequestObject;
import com.matchadb.models.response.MatchaDbResponseObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * This class acts as the REST interface for the MatchaDB application.
 */
@Controller
public class MatchaDbInterface {

    @Autowired MatchaDbRequestParser requestParser;

    // Contstructor
    public MatchaDbInterface(MatchaDbRequestParser requestParser) {
        this.requestParser = requestParser;
    }

    /**
     * Takes in a string, which is expected to be in the form of a JSON object, to run commands
     * against the DB and returns data.
     *
     * @param request The request as a string.
     *
     * @return A response object that defines the success/failure of the operation, and if 
     *         successful returns data reflective of the command.
     */
    @GetMapping(path = "/")
    public MatchaDbResponseObject get(String request) {
        return null;
    }

    /**
     * Takes in a string, which is expected to be in the form of a JSON object, to run commands
     * against the DB to insert data.
     *
     * @param request The request as a string.
     *
     * @return A response object that defines the success/failure of the operation.
     */
    @PostMapping(path = "/")
    public MatchaDbResponseObject post(String request) {
        return null;
    }

    /**
     * Takes in a string, which is expected to be in the form of a JSON object, to run commands
     * against the DB to update data.
     *
     * @param request The request as a string.
     *
     * @return A response object that defines the success/failure of the operation.
     */
    @PutMapping(path = "/")
    public MatchaDbResponseObject update(String request) {
        return null;
    }

    /**
     * Takes in a string, which is expected to be in the form of a JSON object, to run commands
     * against the DB to delete data.
     *
     * @param request The request as a string.
     *
     * @return A response object that defines the success/failure of the operation.
     */
    @DeleteMapping(path = "/")
    public MatchaDbResponseObject delete(String request) {
        return null;
    }
}