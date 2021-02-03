package com.matchadb.surface;

import com.matchadb.enums.MatchaDbRequestType;

import com.matchadb.database.MatchaDbTable;

import com.matchadb.models.MatchaDbRequestObject;
import com.matchadb.models.MatchaDbResponseObject;
import com.matchadb.models.query.MatchaGetQuery;
import com.matchadb.models.query.MatchaPostQuery;
import com.matchadb.models.query.MatchaUpdateQuery;
import com.matchadb.models.query.MatchaDeleteQuery;

import java.util.List;

import org.json.simple.parser.ParseException;

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

    private String UNSUCCESSFUL_COMMAND_UNIDENTIFIABLE = "Command could not be identified...";

    /**
     * Constructor used for autowiring.
     *
     * @param matchaDbTable an instance of the MatchaDbTable class.
     */
    public MatchaDbRequestService(MatchaDbTable matchaDbTable) {
        this.matchaDbTable = matchaDbTable;
    }

    /**
     * Conducts the request by calling on an internal method, and then returns the
     * request result back to the Parser.
     * 
     * @param request The request to be conducted.
     *
     * @return A Response object.
     */
    public MatchaDbResponseObject conductRequest(MatchaDbRequestObject request) throws ParseException {
        if (request.getRequestType() == MatchaDbRequestType.GET) {
            return runGetCommand(request);
        } else if (request.getRequestType() == MatchaDbRequestType.POST) {
            return runPostCommand(request);
        } else if (request.getRequestType() == MatchaDbRequestType.UPDATE) {
            return runUpdateCommand(request);
        } else if (request.getRequestType() == MatchaDbRequestType.DELETE) {
            return runDeleteCommand(request);
        } else {
            // This should never happen, but just in case.
            return new MatchaDbResponseObject(UNSUCCESSFUL_COMMAND_UNIDENTIFIABLE, "");
        }
    }

    /**
     * Takes in the request object and runs the "getData" command on the MatchaDbTable.
     * 
     * @param request The request used for the command.
     *
     * @return A MatchaDbResponseObject explaining the results of the command.
     */
    public MatchaDbResponseObject runGetCommand(MatchaDbRequestObject request) {
        MatchaGetQuery matchaGetQuery 
            = new MatchaGetQuery(request.getFromQuery(), request.getSelectQuery());

        Object matchaGetQueryResponseObject = matchaDbTable.getData(matchaGetQuery);

        if (matchaGetQueryResponseObject != null) {
            return new MatchaDbResponseObject(SUCCESSFUL_GET_INFO, matchaGetQueryResponseObject);
        } else {
            return new MatchaDbResponseObject(UNSUCCESSFUL_GET_INFO, "");
        }
    }

    /**
     * Takes in the request object and runs the "postData" command on the MatchaDbTable.
     * 
     * @param request The request used for the command.
     *
     * @return A MatchaDbResponseObject explaining the results of the command.
     */
    public MatchaDbResponseObject runPostCommand(MatchaDbRequestObject request) throws ParseException {
        MatchaPostQuery matchaPostQuery = new MatchaPostQuery(
            request.getFromQuery(), request.getSelectQuery(), request.getInsertQuery()
        );

        boolean matchaPostQueryResponse = matchaDbTable.postData(matchaPostQuery);

        if (matchaPostQueryResponse) {
            return new MatchaDbResponseObject(SUCCESSFUL_POST_INFO, matchaPostQueryResponse);
        } else {
            return new MatchaDbResponseObject(UNSUCCESSFUL_POST_INFO, matchaPostQueryResponse);
        }
    }

    /**
     * Takes in the request object and runs the "updateData" command on the MatchaDbTable.
     * 
     * @param request The request used for the command.
     *
     * @return A MatchaDbResponseObject explaining the results of the command.
     */
    public MatchaDbResponseObject runUpdateCommand(MatchaDbRequestObject request) {
        MatchaUpdateQuery matchaUpdateQuery = new MatchaUpdateQuery(
            request.getFromQuery(), request.getSelectQuery(), request.getUpdateQuery()
        );

        boolean matchaUpdateQueryResponse = matchaDbTable.updateData(matchaUpdateQuery);

        if (matchaUpdateQueryResponse) {
            return new MatchaDbResponseObject(SUCCESSFUL_UPDATE_INFO, matchaUpdateQueryResponse);
        } else {
            return new MatchaDbResponseObject(UNSUCCESSFUL_UPDATE_INFO, matchaUpdateQueryResponse);
        }
    }

    /**
     * Takes in the request object and runs the "deleteData" command on the MatchaDbTable.
     * 
     * @param request The request used for the command.
     *
     * @return A MatchaDbResponseObject explaining the results of the command.
     */
    public MatchaDbResponseObject runDeleteCommand(MatchaDbRequestObject request) {
        MatchaDeleteQuery matchaDeleteQuery 
            = new MatchaDeleteQuery(request.getFromQuery(), request.getSelectQuery());

        boolean matchaDeleteQueryResponse = matchaDbTable.deleteData(matchaDeleteQuery);

        if (matchaDeleteQueryResponse) {
            return new MatchaDbResponseObject(SUCCESSFUL_DELETE_INFO, matchaDeleteQueryResponse);
        } else {
            return new MatchaDbResponseObject(UNSUCCESSFUL_DELETE_INFO, matchaDeleteQueryResponse);
        }
    }
}