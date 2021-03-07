package com.matchadb.surface;

import com.matchadb.common.MatchaDbConstants;
import com.matchadb.enums.MatchaDbRequestType;
import com.matchadb.models.request.MatchaDbRawRequestObject;
import com.matchadb.models.response.MatchaDbResponseObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.context.annotation.ComponentScan;


/**
 * This class acts as the REST interface for the MatchaDB application.
 */
@RestController
public class MatchaDbInterface {

    @Autowired
    private MatchaDbRequestParser matchaDbRequestParser;

    /**
     * Takes in a string, which is expected to be in the form of a JSON object, to run commands
     * against the DB and returns data.
     *
     * @param request The request as a string.
     *
     * @return A response object as a string wrapped in an entity that defines the success/failure 
     *         of the operation.
     */
    @GetMapping(path = "/")
    public ResponseEntity<String> get(@RequestBody String request) {
        // Send the request to the Parser
        MatchaDbResponseObject response 
            = matchaDbRequestParser.ingestAndConductRequest(
                new MatchaDbRawRequestObject(MatchaDbRequestType.GET, request)
            );
        
        if (MatchaDbConstants.SUCCESSFUL_GET_INFO.equals(response.getInfo())) {
            // Given we have a result to return, return said result
            return ResponseEntity.ok()
                .header("Custom-Header", MatchaDbConstants.SUCCESSFUL_GET_INFO)
                .body(response.getResponseValue().toString());
        } else if (MatchaDbConstants.UNSUCCESSFUL_GET_INFO.equals(response.getInfo())) {
            // If the request was unsuccessful, return a 404
            return ResponseEntity.notFound().build();
        } else {
            // If the request was malformed, return a 400
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Takes in a string, which is expected to be in the form of a JSON object, to run commands
     * against the DB to insert data.
     *
     * @param request The request as a string.
     *
     * @return A response object as a string wrapped in an entity that defines the success/failure 
     *         of the operation.
     */
    @PostMapping(path = "/")
    public ResponseEntity<String> post(String request) {
        return null;
    }

    /**
     * Takes in a string, which is expected to be in the form of a JSON object, to run commands
     * against the DB to update data.
     *
     * @param request The request as a string.
     *
     * @return A response object as a string wrapped in an entity that defines the success/failure 
     *         of the operation.
     */
    @PutMapping(path = "/")
    public ResponseEntity<String> update(String request) {
        return null;
    }

    /**
     * Takes in a string, which is expected to be in the form of a JSON object, to run commands
     * against the DB to delete data.
     *
     * @param request The request as a string.
     *
     * @return A response object as a string wrapped in an entity that defines the success/failure 
     *         of the operation.
     */
    @DeleteMapping(path = "/")
    public ResponseEntity<String> delete(String request) {
        return null;
    }
}