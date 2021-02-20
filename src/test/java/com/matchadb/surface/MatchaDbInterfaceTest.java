package com.matchadb.surface;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
/**
 * This class tests any and all endpoint types for the MatchaDbInterface
 * class.
 */
@ExtendWith(MockitoExtension.class)
public class MatchaDbInterfaceTest {

    // Copy-Pastad this over here from the Request Service, but it should be in a constants class
    // or something.
    // The info values for successful commands on the DB Table.
    private final String SUCCESSFUL_GET_INFO = "Retrieval Successful";
    private final String SUCCESSFUL_POST_INFO = "Insert Successful";
    private final String SUCCESSFUL_UPDATE_INFO = "Update Successful";
    private final String SUCCESSFUL_DELETE_INFO = "Removal Successful";

    // The info values for unsuccessful commands on the DB Table.
    private final String UNSUCCESSFUL_GET_INFO = "Retrieval Failed";
    private final String UNSUCCESSFUL_POST_INFO = "Insert Failed";
    private final String UNSUCCESSFUL_UPDATE_INFO = "Update Failed";
    private final String UNSUCCESSFUL_DELETE_INFO = "Removal Failed";    

    MatchaDbRequestParser matchaDbRequestParser = mock(MatchaDbRequestParser.class);

    MatchaDbInterface matchaDbInterface = new MatchaDbInterface(matchaDbRequestParser);

    @Test
    public void testGetRequestSuccessful() {

    }

    @Test
    public void testGetRequestUnsuccessful() {

    }

    @Test
    public void testPostRequestSuccessful() {

    }

    @Test
    public void testPostRequestUnsuccessful() {

    }

    @Test 
    public void testUpdateRequestSuccessful() {

    }

    @Test 
    public void testUpdateRequestUnsuccessful() {

    }

    @Test
    public void testDeleteRequestSuccessful() {

    }

    @Test
    public void testDeleteRequestUnsuccessful() {

    }
}