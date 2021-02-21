package com.matchadb.surface;

import static com.matchadb.common.MatchaDbConstants.SUCCESSFUL_GET_INFO;
import static com.matchadb.common.MatchaDbConstants.SUCCESSFUL_POST_INFO;
import static com.matchadb.common.MatchaDbConstants.SUCCESSFUL_UPDATE_INFO;
import static com.matchadb.common.MatchaDbConstants.SUCCESSFUL_DELETE_INFO;
import static com.matchadb.common.MatchaDbConstants.UNSUCCESSFUL_GET_INFO;
import static com.matchadb.common.MatchaDbConstants.UNSUCCESSFUL_POST_INFO;
import static com.matchadb.common.MatchaDbConstants.UNSUCCESSFUL_UPDATE_INFO;
import static com.matchadb.common.MatchaDbConstants.UNSUCCESSFUL_DELETE_INFO;

import static org.mockito.Mockito.mock;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.matchadb.enums.MatchaDbRequestType;
import com.matchadb.models.request.MatchaDbRawRequestObject;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import org.mockito.junit.jupiter.MockitoExtension;
/**
 * This class tests any and all endpoint types for the MatchaDbInterface
 * class.
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest
public class MatchaDbInterfaceTest {  

    MatchaDbRequestParser matchaDbRequestParser = mock(MatchaDbRequestParser.class);

    MatchaDbInterface matchaDbInterface = new MatchaDbInterface(matchaDbRequestParser);

    @Autowired
    MockMvc mockMvc; // = new MockMvc();

    /**
     * Tests that on a successful GET request that the interface provides values accordingly.
     */
    @Test
    public void testGetRequestSuccessful() throws Exception {
        // Mock the GET request's MatchaDbRawRequestObject
        // {"From": "Presents", "Select": [["Present Owner", "is", "Me"]]}
        MatchaDbRawRequestObject rawGetRequest 
            = new MatchaDbRawRequestObject(
                MatchaDbRequestType.GET,
                "{\"From\": \"Presents\", \"Select\": [[\"Present Owner\", \"is\", \"Me\"]]}"
            );

        // Test the response
        mockMvc.perform(get("/"));
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