package com.matchadb.surface;

import com.matchadb.common.MatchaDbConstants;
import com.matchadb.enums.MatchaDbRequestType;
import com.matchadb.models.request.MatchaDbRawRequestObject;
import com.matchadb.models.response.MatchaDbResponseObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;


/**
 * This class acts as the REST interface for the MatchaDB application.
 */
@RestController
public class MatchaDbInterface {

    private static final Logger logger = LoggerFactory.getLogger(MatchaDbInterface.class);

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
        logger.info(String.format("\"get\" received request: %s", request));

        // Send the request to the Parser
        MatchaDbResponseObject response 
            = matchaDbRequestParser.ingestAndConductRequest(
                new MatchaDbRawRequestObject(MatchaDbRequestType.GET, request)
            );
        
        if (MatchaDbConstants.SUCCESSFUL_GET_INFO.equals(response.getInfo())) {
            // Given we have a result to return, return said result and a 200
            return new ResponseEntity(response.getResponseValue().toString(), HttpStatus.OK);
        } else if (MatchaDbConstants.UNSUCCESSFUL_GET_INFO.equals(response.getInfo())) {
            // If the request was unsuccessful, return a 404
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            // If the request was malformed, return a 400
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Takes in a string, which is expected to be in the form of a JSON object, to run commands
     * against the DB to insert data.
     *
     * @param request The request as a string.
     *
     * @return An HTTP response that defines the success/failure of the insert operation.
     */
    @PostMapping(path = "/")
    public ResponseEntity post(String request) {
        logger.info(String.format("\"post\" received request: %s", request));

        // Send the request to the parser
        MatchaDbResponseObject response 
            = matchaDbRequestParser.ingestAndConductRequest(
                new MatchaDbRawRequestObject(MatchaDbRequestType.POST, request)
            );

        if (MatchaDbConstants.SUCCESSFUL_POST_INFO.equals(response.getInfo())) {
            // Given that we could insert the request payload, return a 201
            return new ResponseEntity(HttpStatus.CREATED);
        } else if (MatchaDbConstants.UNSUCCESSFUL_POST_INFO.equals(response.getInfo())) {
            // If the request was unsuccessful, return a 409
            return new ResponseEntity(HttpStatus.CONFLICT);
        } else {
            // If the request was malformed, return a 400
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Takes in a string, which is expected to be in the form of a JSON object, to run commands
     * against the DB to update data.
     *
     * @param request The request as a string.
     *
     * @return An HTTP response that defines the success/failure of the update operation.
     */
    @PutMapping(path = "/")
    public ResponseEntity update(String request) {
        logger.info(String.format("\"update\" received request: %s", request));

        // Send the request to the parser
        MatchaDbResponseObject response 
            = matchaDbRequestParser.ingestAndConductRequest(
                new MatchaDbRawRequestObject(MatchaDbRequestType.UPDATE, request)
            );

        if (MatchaDbConstants.SUCCESSFUL_UPDATE_INFO.equals(response.getInfo())) {
            // Given that we could update the DB, return a 200
            return new ResponseEntity(HttpStatus.OK);
        } else if (MatchaDbConstants.UNSUCCESSFUL_UPDATE_INFO.equals(response.getInfo())) {
            // If the request was unsuccessful, return a 409
            return new ResponseEntity(HttpStatus.CONFLICT);
        } else {
            // If the request was malformed, return a 400
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Takes in a string, which is expected to be in the form of a JSON object, to run commands
     * against the DB to delete data.
     *
     * @param request The request as a string.
     *
     * @return An HTTP response that defines the success/failure of the delete operation.
     */
    @DeleteMapping(path = "/")
    public ResponseEntity delete(String request) {
        logger.info(String.format("\"delete\" received request: %s", request));

        // Send the request to the parser
        MatchaDbResponseObject response
            = matchaDbRequestParser.ingestAndConductRequest(
                new MatchaDbRawRequestObject(MatchaDbRequestType.DELETE, request)
            );
        
        if (MatchaDbConstants.SUCCESSFUL_DELETE_INFO.equals(response.getInfo())) {
            // Given that we could remove the item from the DB, return a 200
            return new ResponseEntity(HttpStatus.OK);
        } else if (MatchaDbConstants.UNSUCCESSFUL_DELETE_INFO.equals(response.getInfo())) {
            // If the request was unsuccessful, return a 409
            return new ResponseEntity(HttpStatus.CONFLICT);
        } else {
            // If the request was malformed, return a 400
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}