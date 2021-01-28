package com.matchadb.surface;

import com.matchadb.database.MatchaDbTable;

import com.matchadb.models.MatchaDbRequestObject;
import com.matchadb.models.MatchaDbResponseObject;
import com.matchadb.models.query.MatchaGetQuery;
import com.matchadb.models.query.MatchaPostQuery;
import com.matchadb.models.query.MatchaUpdateQuery;
import com.matchadb.models.query.MatchaDeleteQuery;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class takes the request received from the Request Service one step 
 * further and tries to execute said request.
 */
@Service
public class MatchaDbRequestService {
    
    @Autowired MatchaDbTable matchaDbTable;

    // The info values for successful commands on the DB Table.
    private String SUCCESSFUL_GET_INFO = "Retrieval Successful";
    private String SUCCESSFUL_POST_INFO = "Insert Successful";
    private String SUCCESSFUL_UPDATE_INFO = "Update Successful";
    private String SUCCESSFUL_DELETE_INFO = "Removal Successful";

    // The info values for unsuccessful commands on the DB Table.
    private String UNSUCCESSFUL_GET_INFO = "Retrieval Failed";
    private String UNSUCCESSFUL_POST_INFO = "Insert Failed";
    private String UNSUCCESSFUL_UPDATE_INFO = "Update Failed";
    private String UNSUCCESSFUL_DELETE_INFO = "Removal Failed";    

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
     * Takes in the request query string, and runs the "getData" command on the MatchaDbTable.
     * 
     * @param request The request used for the command.
     *
     * @return A MatchaDbResponseObject explaining the results of the command.
     */
    public MatchaDbResponseObject runGetCommand(MatchaDbRequestObject request) {
        // Rewrite requestQuery input param
        MatchaGetQuery matchaGetQuery 
            = new MatchaGetQuery(request.getFromQuery(), request.getSelectQuery());

        for (String thing : matchaGetQuery.getFromQuery()) {
            System.out.println(thing);
        }

        for (String[] things : matchaGetQuery.getSelectQuery()) {
            for (String moreThings : things) {
                System.out.println(moreThings);
            }
        }

        Object matchaGetQueryResponseObject = matchaDbTable.getData(matchaGetQuery);

        // if (matchaGetQueryResponseObject != null 
        //     || ((List) matchaGetQueryResponseObject).isEmpty()) {
        //     return new MatchaDbResponseObject(SUCCESSFUL_GET_INFO, matchaGetQueryResponseObject);
        // } else {
        //     return new MatchaDbResponseObject(UNSUCCESSFUL_GET_INFO, "");
        // }
        return null;
    }

    /**
     * TODO: Documentation
     */
    public MatchaDbResponseObject runPostCommand(MatchaDbRequestObject request) {
        return null;
    }

    /**
     * TODO: Documentation
     */
    public MatchaDbResponseObject runUpdateCommand(MatchaDbRequestObject request) {
        return null;
    }

    /**
     * TODO: Documentation
     */
    public MatchaDbResponseObject runDeleteCommand(MatchaDbRequestObject request) {
        return null;
    }
}