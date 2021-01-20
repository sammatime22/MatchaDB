package com.matchadb.surface;

import com.matchadb.enums.MatchaDbRequestType;

import com.matchadb.models.MatchaDbRequestObject;
import com.matchadb.models.MatchaDbResponseObject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import mockit.Tested;

/**
 * This class tests the MatchaDbRequestService.
 */
public class MatchaDbRequestServiceTest {

    @Tested MatchaDbRequestService matchaDbRequestService;

    /**
     * Tests the usage of the conduct request method.
     *
     * This also tests successful runs of the four interpret methods.
     */
    @Test
    public void testConductRequest() {
        // Determine how to declare this above properly with the @Tested tags
        matchaDbRequestService = new MatchaDbRequestService();

        MatchaDbResponseObject result = matchaDbRequestService.conductRequest(new MatchaDbRequestObject(MatchaDbRequestType.GET, "ok"));

        // General declarations for the test

        // Any expectations we need

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
     * Tests interpreting GET request results.
     */
    @Test
    public void testInterpretGetFailedResult() {
        // We provide back a response given that we had an unsuccessful get
    }

    /**
     * Tests interpreting POST request results.
     */
    @Test
    public void testInterpretPostFailedResult() {
        // We provide back a response given that an insert (post) had failed
    }

    /**
     * Tests interpreting UPDATE request results.
     */
    @Test
    public void testInterpretUpdateFailedRequest() {
        // We provide back a response when our update had failed
    }

    /**
     * Tests interpreting DELETE request results.
     */
    @Test
    public void testInterpretDeleteRequest() {
        // We give back a response on our delete request upon it failing
    }
}