package com.matchadb.surface;

import com.matchadb.enums.MatchaDbRequestType;

import com.matchadb.models.request.MatchaDbRawRequestObject;
import com.matchadb.models.request.MatchaDbRequestObject;
import com.matchadb.models.response.MatchaDbResponseObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service is an intermediary service which determines the request type and data associated 
 * with the request.
 */
@Service
public class MatchaDbRequestParser {

    @Autowired MatchaDbRequestService matchaDbRequestService;

    private JSONParser parser;

    private final String COMMAND_UNPARSABLE = "Command could not be parsed...";

    private final String FROM = "From";

    private final String SELECT = "Select";

    private final String INSERT = "Insert";

    private final String UPDATE = "Update";

    // Constructor
    public MatchaDbRequestParser(MatchaDbRequestService matchaDbRequestService) {
        this.matchaDbRequestService = matchaDbRequestService;
        this.parser = new JSONParser();
    }

    /**
     * This method is the major driver of the service, ingesting the message
     * and sending it off to the following service in the application.
     *
     * @param rawRequest A raw request object, as received at the Interface.
     *
     * @return The response from the database.
     */
    public MatchaDbResponseObject ingestAndConductRequest(MatchaDbRawRequestObject rawRequest) {
        MatchaDbRequestObject requestObject = convertRawRequest(rawRequest);
        if (requestObject != null) {
            return matchaDbRequestService.conductRequest(requestObject);
        } else {
            return new MatchaDbResponseObject(COMMAND_UNPARSABLE, "");
        }
    }

    /**
     * This method runs an interpretation on an incoming raw request, and returns a request object
     * that can be used by the ingestAndConductRequest method.
     *
     * @param rawRequest The raw request to be parsed.
     *
     * @return a request object that can be used on the DB.
     */
    public MatchaDbRequestObject convertRawRequest(MatchaDbRawRequestObject rawRequest) {
        MatchaDbRequestObject requestObject = null;

        try {
            JSONObject requestContents = (JSONObject) parser.parse(rawRequest.getRequestString());

            if (rawRequest.getRequestType() == MatchaDbRequestType.GET) {
                // Attempt to parse GET request
                requestObject = new MatchaDbRequestObject(
                    MatchaDbRequestType.GET,
                    gatherFromPortion((JSONArray) requestContents.get("From")),
                    gatherSelectPortion((JSONArray) requestContents.get("Select")),
                    null,
                    null
                );
            } else if (rawRequest.getRequestType() == MatchaDbRequestType.POST) {
                // Attempt to parse POST request
                
                // Get From Portion

                // Get Select Portion (if there)

                // Get Insert Portion

            } else if (rawRequest.getRequestType() == MatchaDbRequestType.UPDATE) {
                // Attempt to parse UPDATE request
                
                // Get From Portion

                // Get Select Portion

                // Get Update Portion

            } else if (rawRequest.getRequestType() == MatchaDbRequestType.DELETE) {
                // Attempt to parse DELETE request
                requestObject = new MatchaDbRequestObject(
                    MatchaDbRequestType.DELETE,
                    gatherFromPortion((JSONArray) requestContents.get("From")),
                    gatherSelectPortion((JSONArray) requestContents.get("Select")),
                    null,
                    null
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return requestObject;
    }

    /**
     * Gathers the "From" portion from a request and returns it as a String array.
     *
     * @param jsonArray the JSON Array containing the "From" portion.
     *
     * @return The from portion as an array of String.
     */
    private String[] gatherFromPortion(JSONArray fromPortionAsJSONArray) {

        List<String> fromPortionAsList = new ArrayList<>();
        for (Iterator fromPortionAsJSONArrayIterator = fromPortionAsJSONArray.iterator();
            fromPortionAsJSONArrayIterator.hasNext();) {
            fromPortionAsList.add((String) fromPortionAsJSONArrayIterator.next());
        }

        String[] fromPortionAsArray = new String[fromPortionAsList.size()];
        return fromPortionAsList.toArray(fromPortionAsArray);
    }

    /**
     * Gathers the "Select" portion from a request and returns it as a 2D String array.
     *
     * @param jsonArray the JSON Array containing the "From" portion.
     *
     * @return The from portion as a 2D array of String.
     */
    private String[][] gatherSelectPortion(JSONArray selectPortionAsJSONArray) {

        List<String[]> selectPortionAsList = new ArrayList<>();
        for (Iterator selectPortionAsJSONArrayIterator = selectPortionAsJSONArray.iterator();
            selectPortionAsJSONArrayIterator.hasNext();) {
            JSONArray selectPortionSubarray = (JSONArray) selectPortionAsJSONArrayIterator.next();

            selectPortionAsList.add(
                new String[] {
                    (String) selectPortionSubarray.get(0),
                    (String) selectPortionSubarray.get(1),
                    (String) selectPortionSubarray.get(2)
                }
            );
        }

        String[][] selectPortionAsArray = new String[selectPortionAsList.size()][];
        return selectPortionAsList.toArray(selectPortionAsArray);
    }
}