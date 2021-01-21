package com.matchadb.surface;

import com.matchadb.database.MatchaDbTable;

import com.matchadb.enums.MatchaDbRequestType;

import com.matchadb.models.MatchaDbRequestObject;
import com.matchadb.models.MatchaDbResponseObject;

import com.matchadb.models.query.MatchaGetQuery;
import com.matchadb.models.query.MatchaPostQuery;
import com.matchadb.models.query.MatchaUpdateQuery;
import com.matchadb.models.query.MatchaDeleteQuery;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;

import org.json.simple.parser.ParseException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * This class tests the MatchaDbRequestService.
 */
public class MatchaDbRequestServiceTest {

    @Tested MatchaDbRequestService matchaDbRequestService;

    @Mocked MatchaDbTable matchaDbTable;

    /**
     * Tests the usage of the conduct request method.
     *
     * This also tests successful runs of the four interpret methods.
     */
    @Test
    public void testConductRequest() throws ParseException {
        // General declarations for the test

        // Any expectations we need
        new Expectations() {{
            matchaDbTable.getData(null);
            result = null;

            matchaDbTable.postData(null);
            result = null;

            matchaDbTable.updateData(null);
            result = null;

            matchaDbTable.deleteData(null);
            result = null;
        }};


        // We run a get method, have three objects returned
        // We get what we need back from the interpret Get
        // We return the completed object

        // We run a post method, have one object inserted
        // We get stuff back from the interpret Post
        // We return the completed object

        // We run an update method, have two objects updated
        // We get stuff back from the interpret Update
        // We return the response object
        
        // We run a delete method, have half the db deleted
        // We get stuff back from the interpret Delete
        // We return the completed object
    }

    /**
     * TODO: Documentation
     */
    @Test
    public void testRunGetCommand() {
        // We provide back a response given that we had an unsuccessful get
    }

    /**
     * TODO: Documentation
     */
    @Test
    public void testRunPostCommand() {
        // We provide back a response given that an insert (post) had failed
    }

    /**
     * TODO: Documentation
     */
    @Test
    public void testRunUpdateCommand() {
        // We provide back a response when our update had failed
    }

    /**
     * TODO: Documentation
     */
    @Test
    public void testRunDeleteCommand() {
        // We give back a response on our delete request upon it failing
    }
}