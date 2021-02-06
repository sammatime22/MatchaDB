package com.matchadb.surface;

import org.junit.jupiter.api.Test;

/**
 * This class tests the MatchaDb Request Parser.
 */
public class MatchaDbRequestParserTest {

    MatchaDbRequestParser matchaDbRequestParser;

    /**
     * Tests that given an unparsable request that the Parser will return a "request could not be
     * parsed" message.
     */
    @Test
    public void couldNotBeParsedMessageCanBeReturned() {
        // {"Form": "Tree", ....}
    }

    /**
     * Tests both the ingestion, conversion, and request conduction of a GET request.
     */
    @Test
    public void ingestAndConductGetRequest() {
        // {"From":"Fruit", "Select": [["Color", "is", "red"], ["Price", "<", "12.00"]}

        // Run Captor Verifications
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