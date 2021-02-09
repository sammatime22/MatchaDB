package com.matchadb.surface;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.matchadb.enums.MatchaDbRequestType;
import com.matchadb.models.request.MatchaDbRawRequestObject;
import com.matchadb.models.request.MatchaDbRequestObject;
import com.matchadb.models.response.MatchaDbResponseObject;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * This class tests the MatchaDb Request Parser.
 */
@ExtendWith(MockitoExtension.class)
public class MatchaDbRequestParserTest {

    MatchaDbRequestService matchaDbRequestService = mock(MatchaDbRequestService.class);

    MatchaDbRequestParser matchaDbRequestParser = new MatchaDbRequestParser(matchaDbRequestService);

    @Captor ArgumentCaptor<MatchaDbRequestObject> matchaDbRequestObjectCaptor;

    /**
     * Tests that given an unparsable request that the Parser will return a "request could not be
     * parsed" message.
     */
    @Test
    public void couldNotBeParsedMessageCanBeReturned() {
        // {"Form": "Tree", ....}
        MatchaDbRawRequestObject malformedRawRequest 
            = new MatchaDbRawRequestObject(MatchaDbRequestType.GET, "{\"Form\": \"Tree\", ....}");

        MatchaDbResponseObject response 
            = matchaDbRequestParser.ingestAndConductRequest(malformedRawRequest);

        Assert.assertTrue(
            response.toString().equals(
                String.format(
                    "{\"info\": %s, \"responseValue\": %s}", 
                    "\"Command could not be parsed...\"", "\"\""
                )
            )
        );
    }

    /**
     * Tests both the ingestion, conversion, and request conduction of a GET request.
     */
    @Test
    public void ingestAndConductGetRequest() {
        // {"From":"Fruit", "Select": [["Color", "is", "red"], ["Price", "<", "12.00"]}
        String[] fruitToReturn = new String[]{"Strawberries", "Apples"};
        MatchaDbResponseObject response 
            = new MatchaDbResponseObject("Retrieval Successful", Arrays.asList(fruitToReturn));

        when(
            matchaDbRequestService.conductRequest(any(MatchaDbRequestObject.class))     
        ).thenReturn(response);


        // Run Captor Verifications
        verify(matchaDbRequestService).conductRequest(matchaDbRequestObjectCaptor.capture());
        MatchaDbRequestObject capturedRequestObject = matchaDbRequestObjectCaptor.getValue();
        Assert.assertTrue(MatchaDbRequestType.GET == capturedRequestObject.getRequestType());
        Assert.assertTrue(
            capturedRequestObject.toString().equals(
                String.format(
                    "{\"From\": [%s], \"Select\": [%s], \"Insert\": [%s], \"Update\": [%s]}",
                    "Fruit", "[\"Color\", \"is\", \"red\"], [\"Price\", \"<\", \"12.00\"]", "", ""
                )
            )
        );
    }

    /**
     * Tests both the ingestion, conversion, and request conduction of a POST request.
     */
    @Test
    public void ingestAndConductPostRequest() {
        // {"From":"Cars", "Insert": [["Name", "F150"]]}

        // Run Captor Verifications
    }

    /**
     * Tests both the ingestion, conversion, and request conduction of a UPDATE request.
     */
    @Test
    public void ingestAndConductUpdateRequest() {
        // {"From": "Laptops", "Select":[["Brand", "has", "G"]], "Update": ["Price", "to", "1300"]}

        // Run Captor Verifications
    }

    /**
     * Tests both the ingestion, conversion, and request conduction of a DELETE request.
     */
    @Test
    public void ingestAndConductDeleteRequest() {
        // {"From": "Books", "Select":[["Author", "has", "i"]]}

        // Run Captor Verifications
    }
}