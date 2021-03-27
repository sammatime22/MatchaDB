package com.matchadb.surface;

import static com.matchadb.common.MatchaDbConstants.SUCCESSFUL_GET_INFO;
import static com.matchadb.common.MatchaDbConstants.SUCCESSFUL_POST_INFO;
import static com.matchadb.common.MatchaDbConstants.SUCCESSFUL_UPDATE_INFO;
import static com.matchadb.common.MatchaDbConstants.SUCCESSFUL_DELETE_INFO;
import static com.matchadb.common.MatchaDbConstants.UNSUCCESSFUL_GET_INFO;
import static com.matchadb.common.MatchaDbConstants.UNSUCCESSFUL_POST_INFO;
import static com.matchadb.common.MatchaDbConstants.UNSUCCESSFUL_UPDATE_INFO;
import static com.matchadb.common.MatchaDbConstants.UNSUCCESSFUL_DELETE_INFO;
import static com.matchadb.common.MatchaDbConstants.COMMAND_UNIDENTIFIABLE;

import com.matchadb.enums.MatchaDbRequestType;

import com.matchadb.database.MatchaDbTable;

import com.matchadb.models.request.MatchaDbRequestObject;
import com.matchadb.models.response.MatchaDbResponseObject;
import com.matchadb.models.query.MatchaGetQuery;
import com.matchadb.models.query.MatchaPostQuery;
import com.matchadb.models.query.MatchaUpdateQuery;
import com.matchadb.models.query.MatchaDeleteQuery;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class takes the request received from the Request Service one step 
 * further and tries to execute said request.
 */
@Service
public class MatchaDbRequestService {
    
    private static final Logger logger = LoggerFactory.getLogger(MatchaDbRequestService.class);

    @Autowired MatchaDbTable matchaDbTable;

    /**
     * Constructor.
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
    public MatchaDbResponseObject conductRequest(MatchaDbRequestObject request) {
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
            return new MatchaDbResponseObject(COMMAND_UNIDENTIFIABLE, "");
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
        logger.info(String.format("Running runGetCommand with: %s", request.toString()));

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
    public MatchaDbResponseObject runPostCommand(MatchaDbRequestObject request) {
        logger.info(String.format("Running runPostCommand with: %s", request.toString()));

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
        logger.info(String.format("Running runUpdateCommand with: %s", request.toString()));

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
        logger.info(String.format("Running runDeleteCommand with: %s", request.toString()));

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