package com.matchadb.surface;

import static com.matchadb.common.MatchaDbConstants.SUCCESSFUL_GET_INFO;
import static com.matchadb.common.MatchaDbConstants.SUCCESSFUL_POST_INFO;
import static com.matchadb.common.MatchaDbConstants.SUCCESSFUL_UPDATE_INFO;
import static com.matchadb.common.MatchaDbConstants.SUCCESSFUL_DELETE_INFO;
import static com.matchadb.common.MatchaDbConstants.UNSUCCESSFUL_GET_INFO;
import static com.matchadb.common.MatchaDbConstants.UNSUCCESSFUL_POST_INFO;
import static com.matchadb.common.MatchaDbConstants.UNSUCCESSFUL_UPDATE_INFO;
import static com.matchadb.common.MatchaDbConstants.UNSUCCESSFUL_DELETE_INFO;
import static com.matchadb.common.MatchaDbConstants.COMMAND_UNIDENTIFIABLE;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.matchadb.enums.MatchaDbRequestType;
import com.matchadb.models.request.MatchaDbRawRequestObject;
import com.matchadb.models.response.MatchaDbResponseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.context.junit4.SpringRunner;

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
        String rawRequestString 
            = "{\"From\": \"Presents\", \"Select\": [[\"Present Owner\", \"is\", \"Me\"]]}";

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

        MvcResult result = mockMvc.perform(request).andReturn();
        
        Assert.assertTrue(rawRequestString.equals(result.getRequest().getContentAsString()));
        Assert.assertTrue(result.getRequest() != null);
        for (String expectedContent : expectedResponseBodyContents) {
            Assert.assertTrue(
                result.getResponse().getContentAsString().indexOf(expectedContent) != -1
            );
        }
        Assert.assertTrue(HttpStatus.OK.value() == result.getResponse().getStatus());
    }

    /**
     * Tests that on an unsuccessful GET request that the interface provides values accordingly.
     */
    @Test
    public void testGetRequestUnsuccessful() throws Exception {
        String rawRequestString = "{}";

        List<HashMap<String, Object>> responseObject = null;

        String expectedResponseBodyContent = "";

        when(
            matchaDbRequestParser.ingestAndConductRequest(any(MatchaDbRawRequestObject.class))
        ).thenReturn(new MatchaDbResponseObject(UNSUCCESSFUL_GET_INFO, null));

        RequestBuilder request 
            = MockMvcRequestBuilders
                .get("/")
                .content(rawRequestString)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();

        Assert.assertTrue(rawRequestString.equals(result.getRequest().getContentAsString()));
        Assert.assertTrue(
            expectedResponseBodyContent.equals(result.getResponse().getContentAsString()));
        Assert.assertTrue(HttpStatus.NOT_FOUND.value() == result.getResponse().getStatus());
    }

    /**
     * Tests that on an unparsable GET request that the interface provides values accordingly.
     */
    @Test
    public void testGetRequestUnparsable() throws Exception {
        String rawRequestString = "abc1234566";

        Object responseObject = null;

        String expectedResponseBodyContent = "";

        when(
            matchaDbRequestParser.ingestAndConductRequest(any(MatchaDbRawRequestObject.class))
        ).thenReturn(new MatchaDbResponseObject(COMMAND_UNIDENTIFIABLE, null));

        RequestBuilder request
            = MockMvcRequestBuilders
                .get("/")
                .content(rawRequestString)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();

        Assert.assertTrue(rawRequestString.equals(result.getRequest().getContentAsString()));
        Assert.assertTrue(
            expectedResponseBodyContent.equals(result.getResponse().getContentAsString()));
        Assert.assertTrue(HttpStatus.BAD_REQUEST.value() == result.getResponse().getStatus());
    }

    /**
     * Tests that on a successful POST request that the interface provides values accordingly.
     */
    @Test
    public void testPostRequestSuccessful() throws Exception {
        String rawRequestString 
            = "{\"From\": \"People\", \"Select\": [[\"Present Owner\", \"is\", \"Me\"]]," 
            + "\"Insert\": {\"Present\": \"New Drumkit\"}}";

        when(
            matchaDbRequestParser.ingestAndConductRequest(any(MatchaDbRawRequestObject.class))
        ).thenReturn(new MatchaDbResponseObject(SUCCESSFUL_POST_INFO, true));

        RequestBuilder request
            = MockMvcRequestBuilders
                .post("/")
                .content(rawRequestString)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();

        Assert.assertTrue(rawRequestString.equals(result.getRequest().getContentAsString()));
        Assert.assertTrue(HttpStatus.CREATED.value() == result.getResponse().getStatus());
    }

    /**
     * Tests that on an unsuccessful POST request that the interface provides values accordingly.
     */
    @Test
    public void testPostRequestUnsuccessful() throws Exception {
        String rawRequestString 
            = "{\"From\": \"Poeplu\", \"Select\": [[\"Present Owner\", \"is\", \"Me\"]]," 
            + "\"Insert\": {\"Present\": \"New Drumkit\"}}";

        when(
            matchaDbRequestParser.ingestAndConductRequest(any(MatchaDbRawRequestObject.class))
        ).thenReturn(new MatchaDbResponseObject(UNSUCCESSFUL_POST_INFO, false));

        RequestBuilder request
            = MockMvcRequestBuilders
                .post("/")
                .content(rawRequestString)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(request).andReturn();

        Assert.assertTrue(rawRequestString.equals(result.getRequest().getContentAsString()));
        Assert.assertTrue(HttpStatus.CONFLICT.value() == result.getResponse().getStatus());
    }

    /**
     * Tests that on an unparsable POST request that the interface provides values accordingly.
     */
    @Test
    public void testPostRequestUnparsable() throws Exception {
        String rawRequestString = "insert kit";

        when(
            matchaDbRequestParser.ingestAndConductRequest(any(MatchaDbRawRequestObject.class))
        ).thenReturn(new MatchaDbResponseObject(COMMAND_UNIDENTIFIABLE, false));

        RequestBuilder request
            = MockMvcRequestBuilders
                .post("/")
                .content(rawRequestString)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();

        Assert.assertTrue(rawRequestString.equals(result.getRequest().getContentAsString()));
        Assert.assertTrue(HttpStatus.BAD_REQUEST.value() == result.getResponse().getStatus());
    }

    /**
     * Tests that on a successful UPDATE request that the interface provides values accordingly.
     */
    @Test 
    public void testUpdateRequestSuccessful() throws Exception {
        String rawRequestString 
            = "{\"From\": \"People\", \"Select\": [[\"Present Owner\", \"is\", \"Me\"]]"
            + "\"Update\": {\"Present\": \"New Drumkit\"}}";

        when(
            matchaDbRequestParser.ingestAndConductRequest(any(MatchaDbRawRequestObject.class))
        ).thenReturn(new MatchaDbResponseObject(SUCCESSFUL_UPDATE_INFO, true));

        RequestBuilder request
            = MockMvcRequestBuilders
                .put("/")
                .content(rawRequestString)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();

        Assert.assertTrue(rawRequestString.equals(result.getRequest().getContentAsString()));
        Assert.assertTrue(HttpStatus.OK.value() == result.getResponse().getStatus());
    }

    /**
     * Tests that on an unsuccessful UPDATE request that the interface provides values accordingly.
     */
    @Test 
    public void testUpdateRequestUnsuccessful() throws Exception {
        String rawRequestString
            = "{\"From\": \"Peoplu\", \"Select\": [[\"Present Owner\", \"is\", \"Me\"]],"
            + "\"Update\": {\"Present\": \"New Drumkit\"}}";

        when(
            matchaDbRequestParser.ingestAndConductRequest(any(MatchaDbRawRequestObject.class))
        ).thenReturn(new MatchaDbResponseObject(UNSUCCESSFUL_UPDATE_INFO, false));

        RequestBuilder request
            = MockMvcRequestBuilders
                .put("/")
                .content(rawRequestString)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();

        Assert.assertTrue(rawRequestString.equals(result.getRequest().getContentAsString()));
        Assert.assertTrue(HttpStatus.CONFLICT.value() == result.getResponse().getStatus());
    }

    /**
     * Tests that on an unparsable UPDATE request that the interface provides values accordingly.
     */
    @Test
    public void testUpdateRequestUnparsable() throws Exception {
        String rawRequestString = "{I wanna update the present}";

        when(
            matchaDbRequestParser.ingestAndConductRequest(any(MatchaDbRawRequestObject.class))
        ).thenReturn(new MatchaDbResponseObject(COMMAND_UNIDENTIFIABLE, false));

        RequestBuilder request
            = MockMvcRequestBuilders
                .put("/")
                .content(rawRequestString)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();

        Assert.assertTrue(rawRequestString.equals(result.getRequest().getContentAsString()));
        Assert.assertTrue(HttpStatus.BAD_REQUEST.value() == result.getResponse().getStatus());
    }

    /**
     * Tests that on a successful DELETE request that the interface provides values accordingly.
     */
    @Test
    public void testDeleteRequestSuccessful() throws Exception {
        String rawRequestString
            = "{\"From\": \"Things\", \"Select\": [[\"Price\", \"is\", \"80000000.00\"]]";

        when(
            matchaDbRequestParser.ingestAndConductRequest(any(MatchaDbRawRequestObject.class))
        ).thenReturn(new MatchaDbResponseObject(SUCCESSFUL_DELETE_INFO, true));

        RequestBuilder request
            = MockMvcRequestBuilders
                .delete("/")
                .content(rawRequestString)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();

        Assert.assertTrue(rawRequestString.equals(result.getRequest().getContentAsString()));
        Assert.assertTrue(HttpStatus.OK.value() == result.getResponse().getStatus());
    }

    /**
     * Tests that on an unsuccessful DELETE request that the interface provides values accordingly.
     */
    @Test
    public void testDeleteRequestUnsuccessful() throws Exception {
        String rawRequestString 
            = "{\"From\": \"Thangs\", \"Select\": [[\"Price\", \"is\", \"0\"]]}";

        when(
            matchaDbRequestParser.ingestAndConductRequest(any(MatchaDbRawRequestObject.class))
        ).thenReturn(new MatchaDbResponseObject(UNSUCCESSFUL_DELETE_INFO, false));

        RequestBuilder request
            = MockMvcRequestBuilders
                .delete("/")
                .content(rawRequestString)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();

        Assert.assertTrue(rawRequestString.equals(result.getRequest().getContentAsString()));
        Assert.assertTrue(HttpStatus.CONFLICT.value() == result.getResponse().getStatus());
    }

    /**
     * Tests that on an unparsable DELETE request that the interface provides values accordingly.
     */
    @Test
    public void testDeleteRequestUnparsable() throws Exception {
        String rawRequestString
            = "Please get rid of that stuff what I need to do this right {} cool";

        when(
            matchaDbRequestParser.ingestAndConductRequest(any(MatchaDbRawRequestObject.class))
        ).thenReturn(new MatchaDbResponseObject(COMMAND_UNIDENTIFIABLE, false));

        RequestBuilder request
            = MockMvcRequestBuilders
                .delete("/")
                .content(rawRequestString)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();

        Assert.assertTrue(rawRequestString.equals(result.getRequest().getContentAsString()));
        Assert.assertTrue(HttpStatus.BAD_REQUEST.value() == result.getResponse().getStatus());
    }
}