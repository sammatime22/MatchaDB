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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;


/**
 * This service is an intermediary service which determines the request type and data associated 
 * with the request.
 */
@Service
public class MatchaDbRequestParser {

    private static final Logger logger = LoggerFactory.getLogger(MatchaDbRequestParser.class);

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
        logger.info(String.format("Ingesting: %s", rawRequest.toString()));

        MatchaDbRequestObject requestObject = convertRawRequest(rawRequest);

        if (requestObject != null) {
            logger.info(String.format("Requesting with: %s", requestObject.toString()));
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
            logger.error("My request String: " + rawRequest.getRequestString());
            JSONObject requestContents = (JSONObject) parser.parse(rawRequest.getRequestString());

            if (rawRequest.getRequestType() == MatchaDbRequestType.GET) {
                // Attempt to parse GET request

                requestObject = new MatchaDbRequestObject(
                    MatchaDbRequestType.GET,
                    gather1DArrayPortion((JSONArray) requestContents.get("From")),
                    gather2DArrayPortion((JSONArray) requestContents.get("Select")),
                    null,
                    null
                );
            } else if (rawRequest.getRequestType() == MatchaDbRequestType.POST) {
                // Attempt to parse POST request

                // Optional to include a "Select" portion
                String[][] selectPortionForInsert = null;

                if (requestContents.get("Select") != null) {
                    selectPortionForInsert = gather2DArrayPortion((JSONArray) requestContents.get("Select"));
                }
                
                requestObject = new MatchaDbRequestObject(
                    MatchaDbRequestType.POST,
                    gather1DArrayPortion((JSONArray) requestContents.get("From")),
                    selectPortionForInsert,
                    gatherInsertJsonObject(requestContents.get("Insert")),
                    null
                );                
            } else if (rawRequest.getRequestType() == MatchaDbRequestType.UPDATE) {
                // Attempt to parse UPDATE request

                requestObject = new MatchaDbRequestObject(
                    MatchaDbRequestType.UPDATE,
                    gather1DArrayPortion((JSONArray) requestContents.get("From")),
                    gather2DArrayPortion((JSONArray) requestContents.get("Select")),
                    null,
                    gather2DArrayPortion((JSONArray) requestContents.get("Update"))
                );
            } else if (rawRequest.getRequestType() == MatchaDbRequestType.DELETE) {
                // Attempt to parse DELETE request

                requestObject = new MatchaDbRequestObject(
                    MatchaDbRequestType.DELETE,
                    gather1DArrayPortion((JSONArray) requestContents.get("From")),
                    gather2DArrayPortion((JSONArray) requestContents.get("Select")),
                    null,
                    null
                );
            }
        } catch (Exception e) {
            logger.error("An unidentified Exception has occurred:\n", e);
        }

        return requestObject;
    }

    /**
     * Gathers a 1D Array portion from a request and returns it as a 2D String array.
     *
     * @param jsonArray the 1D Json Array portion.
     *
     * @return a 1D array of String.
     */
    private String[] gather1DArrayPortion(JSONArray fromPortionAsJSONArray) {

        List<String> fromPortionAsList = new ArrayList<>();
        for (Iterator fromPortionAsJSONArrayIterator = fromPortionAsJSONArray.iterator();
            fromPortionAsJSONArrayIterator.hasNext();) {
            fromPortionAsList.add((String) fromPortionAsJSONArrayIterator.next());
        }

        String[] fromPortionAsArray = new String[fromPortionAsList.size()];
        return fromPortionAsList.toArray(fromPortionAsArray);
    }

    /**
     * Gathers a 2D Array portion from a request and returns it as a 2D String array.
     *
     * @param jsonArray the 2D Json Array portion.
     *
     * @return a 2D array of String.
     */
    private String[][] gather2DArrayPortion(JSONArray selectPortionAsJSONArray) {

        List<String[]> selectPortionAsList = new ArrayList<>();
        for (Iterator selectPortionAsJSONArrayIterator = selectPortionAsJSONArray.iterator();
            selectPortionAsJSONArrayIterator.hasNext();) {
            JSONArray selectPortionSubarray = (JSONArray) selectPortionAsJSONArrayIterator.next();
            
            String[] selectPortionZubarray = new String[selectPortionSubarray.size()];
            for (int i = 0; i < selectPortionSubarray.size(); i++) {
                selectPortionZubarray[i] = (String) selectPortionSubarray.get(i);
            }
            selectPortionAsList.add(selectPortionZubarray);
        }

        String[][] selectPortionAsArray = new String[selectPortionAsList.size()][];
        return selectPortionAsList.toArray(selectPortionAsArray);
    }

    /**
     * Gathers any object type from the "Insert" portion of a POST request and turns it into something
     * usable by the MatchaDB system.
     *
     * @param jsonObjectToInsert A JSON object to insert, in a JSON format, Array, Object, or otherwise.
     *
     * @return A 2D array of String.
     */
    private String[][] gatherInsertJsonObject(Object jsonObjectToInsert) {
        if (jsonObjectToInsert instanceof JSONArray jsonObjectToInsertAsJsonArray) {
            // ...dostuff...
        } else if (jsonObjectToInsert instanceof JSONObject jsonObjectToInsertAsJsonObject) {
            // ...dostuff...
        } else {
            //... dostuff and consider the object as one big string to insert
        }

        return null;
    }
}