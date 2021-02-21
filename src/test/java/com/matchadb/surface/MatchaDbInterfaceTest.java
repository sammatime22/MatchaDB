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

    MatchaDbRequestParser matchaDbRequestParser = mock(MatchaDbRequestParser.class);

    MatchaDbInterface matchaDbInterface = new MatchaDbInterface(matchaDbRequestParser);

    /**
     * Tests that on a successful GET request that the interface provides values accordingly.
     */
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