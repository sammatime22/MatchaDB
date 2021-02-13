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

            System.out.println(requestContents.toString());
            if (rawRequest.getRequestType() == MatchaDbRequestType.GET) {
                // Attempt to parse GET request

                // Get From Portion
                JSONArray fromPortionAsJSONArray = (JSONArray) requestContents.get("From");
                System.out.println(fromPortionAsJSONArray.toString());

                List<String> fromPortionAsList = new ArrayList<>();
                for (Iterator fromPortionAsJSONArrayIterator = fromPortionAsJSONArray.iterator();
                    fromPortionAsJSONArrayIterator.hasNext();) {
                    fromPortionAsList.add((String) fromPortionAsJSONArrayIterator.next());
                }

                String[] fromPortionAsArray = new String[fromPortionAsList.size()];
                fromPortionAsArray = fromPortionAsList.toArray(fromPortionAsArray);

                for (String thing : fromPortionAsArray) {
                    System.out.println(thing);
                }

                // Get Select Portion
                JSONArray selectPortionAsJSONArray = (JSONArray) requestContents.get("Select");
                System.out.println(selectPortionAsJSONArray.toString());


                List<String[]> selectPortionAsList = new ArrayList<>();
                for (Iterator selectPortionAsJSONArrayIterator 
                    = selectPortionAsJSONArray.iterator(); 
                    selectPortionAsJSONArrayIterator.hasNext();) {
                    selectPortionAsList.add((String[]) selectPortionAsJSONArrayIterator.next());
                }

                System.out.println(selectPortionAsList.toString());

                String[][] selectPortionAsArray = new String[selectPortionAsList.size()][];
                selectPortionAsArray = selectPortionAsList.toArray(selectPortionAsArray);

                for (String[] ding : selectPortionAsArray) {
                    for (String aling : ding) {
                        System.out.println(aling);
                    }
                }

                // Develop Final Object
                requestObject = new MatchaDbRequestObject(
                    MatchaDbRequestType.GET,
                    fromPortionAsArray,
                    selectPortionAsArray,
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
                
                // Get From Portion

                // Get Select Portion

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // If we couldn't parse the request, just return null
        return requestObject;
    }
}