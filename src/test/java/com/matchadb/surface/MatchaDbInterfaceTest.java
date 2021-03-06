package com.matchadb.surface;

import static com.matchadb.common.MatchaDbConstants.SUCCESSFUL_GET_INFO;
import static com.matchadb.common.MatchaDbConstants.SUCCESSFUL_POST_INFO;
import static com.matchadb.common.MatchaDbConstants.SUCCESSFUL_UPDATE_INFO;
import static com.matchadb.common.MatchaDbConstants.SUCCESSFUL_DELETE_INFO;
import static com.matchadb.common.MatchaDbConstants.UNSUCCESSFUL_GET_INFO;
import static com.matchadb.common.MatchaDbConstants.UNSUCCESSFUL_POST_INFO;
import static com.matchadb.common.MatchaDbConstants.UNSUCCESSFUL_UPDATE_INFO;
import static com.matchadb.common.MatchaDbConstants.UNSUCCESSFUL_DELETE_INFO;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.matchadb.MatchaDbApplicationInitializer;

import com.matchadb.enums.MatchaDbRequestType;
import com.matchadb.models.request.MatchaDbRawRequestObject;
import com.matchadb.models.response.MatchaDbResponseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.mockito.junit.jupiter.MockitoExtension;
/**
 * This class tests any and all endpoint types for the MatchaDbInterface
 * class.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = MatchaDbInterface.class)
public class MatchaDbInterfaceTest {  

    @MockBean
    private MatchaDbRequestParser matchaDbRequestParser;

    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests that on a successful GET request that the interface provides values accordingly.
     */
    @Test
    public void testGetRequestSuccessful() throws Exception {
        // Mock the GET request's MatchaDbRawRequestObject
        // {"From": "Presents", "Select": [["Present Owner", "is", "Me"]]}
        
        String rawRequestString 
            = "{\"From\": \"Presents\", \"Select\": [[\"Present Owner\", \"is\", \"Me\"]]}";

        MatchaDbRawRequestObject rawGetRequest 
            = new MatchaDbRawRequestObject(MatchaDbRequestType.GET, rawRequestString);

        List<HashMap<String, Object>> responseObject = new ArrayList<HashMap<String, Object>>() {{
            add(
                new HashMap<String, Object>() {{
                    put("Name", "Drum Kit");
                    put("Number Of Parts", 8);
                    put("Including Cymbals?", true);
                }}
            );
        }};

        String[] expectedResponseBodyContents
            = new String[] {"Name=Drum Kit", "Number Of Parts=8", "Including Cymbals?=true"};

        when(
            matchaDbRequestParser.ingestAndConductRequest(any(MatchaDbRawRequestObject.class))
        ).thenReturn(new MatchaDbResponseObject(SUCCESSFUL_GET_INFO, responseObject));

        RequestBuilder request 
            = MockMvcRequestBuilders
                .get("/")
                .content(rawRequestString)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON);

        // Test the response
        MvcResult result = mockMvc.perform(request).andReturn();
        
        Assert.assertTrue(rawRequestString.equals(result.getRequest().getContentAsString()));
        Assert.assertTrue(result.getRequest() != null);
        for (String expectedContent : expectedResponseBodyContents) {
            Assert.assertTrue(
                result.getResponse().getContentAsString().indexOf(expectedContent) != -1
            );
        }
    }

    // @Test
    // public void testGetRequestUnsuccessful() {

    // }

    // @Test
    // public void testPostRequestSuccessful() {

    // }

    // @Test
    // public void testPostRequestUnsuccessful() {

    // }

    // @Test 
    // public void testUpdateRequestSuccessful() {

    // }

    // @Test 
    // public void testUpdateRequestUnsuccessful() {

    // }

    // @Test
    // public void testDeleteRequestSuccessful() {

    // }

    // @Test
    // public void testDeleteRequestUnsuccessful() {

    // }
}