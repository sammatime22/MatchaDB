package com.matchadb.surface;

import com.matchadb.models.request.MatchaDbRawRequestObject;
import com.matchadb.models.response.MatchaDbResponseObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
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

    // Contstructor 
    
    // public MatchaDbInterface(MatchaDbRequestParser matchaDbRequestParser) {
    //     this.matchaDbRequestParser = matchaDbRequestParser;
    // }

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
    public ResponseEntity<String> get(String request) {
        System.out.println("Got here!");
        // Send the request to the Parser

        // Interpret the info - see if the request passed or failed

        // Apply the info to the header
        
        // Apply the object to the body of the entity as a string
        
        // Return the result
        return ResponseEntity.ok().header("Custom-Header", "grapefruit").body("sour");
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