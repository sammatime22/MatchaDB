package com.matchadb.surface;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.matchadb.database.MatchaDbTable;

import com.matchadb.enums.MatchaDbRequestType;

import com.matchadb.models.request.MatchaDbRequestObject;
import com.matchadb.models.response.MatchaDbResponseObject;

import com.matchadb.models.query.MatchaGetQuery;
import com.matchadb.models.query.MatchaPostQuery;
import com.matchadb.models.query.MatchaUpdateQuery;
import com.matchadb.models.query.MatchaDeleteQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * This class tests the MatchaDbRequestService.
 */
@ExtendWith(MockitoExtension.class)
public class MatchaDbRequestServiceTest {

    MatchaDbTable matchaDbTable = mock(MatchaDbTable.class);

    MatchaDbRequestService matchaDbRequestService = new MatchaDbRequestService(matchaDbTable);

    // Request Objects throughout the tests
    MatchaDbRequestObject getRequestObject = new MatchaDbRequestObject(MatchaDbRequestType.GET, 
        new String[] {"Eggs"}, new String[][] {{"Shell Color", "is", "Green"}}, null, null);
    MatchaDbRequestObject postRequestObject = new MatchaDbRequestObject(MatchaDbRequestType.POST, 
        new String[] {"Customer 4"}, null, 
        new String[][] {{"Order", "Earl Grey"}, {"Order", "Matcha"}}, null);
    MatchaDbRequestObject updateRequestObject = new MatchaDbRequestObject(
        MatchaDbRequestType.UPDATE, new String[] {"Employee"}, 
        new String[][] {{"Name", "is", "Prince"}}, null,
        new String[][] {{"Update", "Salary", "$20/hr"}});
    MatchaDbRequestObject deleteRequestObject = new MatchaDbRequestObject(
        MatchaDbRequestType.DELETE, new String[] {"Desks"}, 
        new String[][] {{"Characteristic", "is", "mean"}}, null, null);

    // Queries used throughout the tests
    MatchaGetQuery getGreenEggsQuery = new MatchaGetQuery(new String[] {"Eggs"},
        new String[][] {{"Shell Color", "is", "Green"}});
    MatchaPostQuery postTwoCupsOfTeaQuery = new MatchaPostQuery(new String[] {"Customer 4"},
        null, new String[][] {{"Order", "Earl Grey"}, {"Order", "Matcha"}});
    MatchaUpdateQuery updateEmployeeSalaryQuery = new MatchaUpdateQuery(new String[] {"Employee"},
        new String[][] {{"Name", "is", "Prince"}}, 
        new String[][] {{"Update", "Salary", "$20/hr"}});
    MatchaDeleteQuery deleteDeskQuery = new MatchaDeleteQuery(new String[] {"Desks"},
        new String[][] {{"Characteristic", "is", "mean"}});

    // Captors to retrieve the arguments handed to the mocked MatchaDbTable methods
    @Captor ArgumentCaptor<MatchaGetQuery> matchaGetQueryCaptor;
    @Captor ArgumentCaptor<MatchaPostQuery> matchaPostQueryCaptor;
    @Captor ArgumentCaptor<MatchaUpdateQuery> matchaUpdateQueryCaptor;
    @Captor ArgumentCaptor<MatchaDeleteQuery> matchaDeleteQueryCaptor;    

    /**
     * Tests the usage of the conduct request method.
     *
     * This also tests unsuccessful runs of the four interpret methods.
     *   - The get call attempting to get an object that doesn't exist
     *   - The post call attempting to get an object that doesn't exist
     *   - The update call attempting to get an object that doesn't exist
     *   - The delete call attempting to get an object that doesn't exist
     */
    @Test
    public void testConductRequest() {
        // Get Test
        MatchaDbResponseObject getFailedResponseObject 
            = new MatchaDbResponseObject("Retrieval Failed", "");
        when(matchaDbTable.getData(any(MatchaGetQuery.class))).thenReturn(null);
        compareResponseObjects(getFailedResponseObject, 
            matchaDbRequestService.conductRequest(getRequestObject));

        // Post Test
        MatchaDbResponseObject postFailedResponseObject 
            = new MatchaDbResponseObject("Insert Failed", false);
        when(matchaDbTable.postData(any(MatchaPostQuery.class))).thenReturn(false);
        compareResponseObjects(postFailedResponseObject, 
            matchaDbRequestService.conductRequest(postRequestObject));

        // Update Test
        MatchaDbResponseObject updateFailedResponseObject 
            = new MatchaDbResponseObject("Update Failed", false);
        when(matchaDbTable.updateData(any(MatchaUpdateQuery.class))).thenReturn(false);
        compareResponseObjects(updateFailedResponseObject, 
            matchaDbRequestService.conductRequest(updateRequestObject));

        // Delete Test
        MatchaDbResponseObject deleteFailedResponseObject 
            = new MatchaDbResponseObject("Removal Failed", false);
        when(matchaDbTable.deleteData(any(MatchaDeleteQuery.class))).thenReturn(false);
        compareResponseObjects(deleteFailedResponseObject, 
            matchaDbRequestService.conductRequest(deleteRequestObject));
    }

    /**
     * This test tests the successful run of the runGetCommand method, retrieving three objects,
     * and then returning said objects back to the end user.
     *
     * This is also testing the construction of the required MatchaGetQuery object.
     */
    @Test
    public void testRunGetCommand() {
        List<HashMap> threeGreenEggs = new ArrayList<>() {{
            add(new HashMap<>() {{ put("Name", "Green Egg A"); }});
            add(new HashMap<>() {{ put("Name", "Green Egg B"); }});
            add(new HashMap<>() {{ put("Name", "Green Egg C"); }});
        }}; 
        MatchaDbResponseObject getSuccessfulResponseObject
            = new MatchaDbResponseObject("Retrieval Successful", threeGreenEggs);
        when(matchaDbTable.getData(any(MatchaGetQuery.class))).thenReturn(threeGreenEggs);

        // See overall results
        compareResponseObjects(getSuccessfulResponseObject, 
            matchaDbRequestService.runGetCommand(getRequestObject));

        // Run captor verifications
        verify(matchaDbTable).getData(matchaGetQueryCaptor.capture());
        MatchaGetQuery capturedMatchaGetQuery = matchaGetQueryCaptor.getValue();
        Assert.assertTrue(capturedMatchaGetQuery.toString().equals(getGreenEggsQuery.toString()));
    }

    /**
     * This test tests the successful run of the runPostCommand method, inserting two objects,
     * and then returning a successfully complete back to the user.
     *
     * This is also testing the construction of the required MatchaPostQuery object.
     */
    @Test
    public void testRunPostCommand() {
        MatchaDbResponseObject postSuccessfulResponseObject
            = new MatchaDbResponseObject("Insert Successful", true);
        when(matchaDbTable.postData(any(MatchaPostQuery.class))).thenReturn(true);

        // See overall results
        compareResponseObjects(postSuccessfulResponseObject,
            matchaDbRequestService.runPostCommand(postRequestObject));

        // Run captor verifications
        verify(matchaDbTable).postData(matchaPostQueryCaptor.capture());
        MatchaPostQuery capturedMatchaPostQuery = matchaPostQueryCaptor.getValue();
        Assert.assertTrue(capturedMatchaPostQuery.toString()
            .equals(postTwoCupsOfTeaQuery.toString()));
    }

    /**
     * This test tests the successful run of the runUpdateCommand method, updating objects,
     * and then returning a successful response back to the user.
     *
     * This is also testing the construction of the required MatchaUpdateQuery object.
     */
    @Test
    public void testRunUpdateCommand() {
        MatchaDbResponseObject updateSuccessfulResponseObject
            = new MatchaDbResponseObject("Update Successful", true);
        when(matchaDbTable.updateData(any(MatchaUpdateQuery.class))).thenReturn(true);

        // See overall results
        compareResponseObjects(updateSuccessfulResponseObject,
            matchaDbRequestService.runUpdateCommand(updateRequestObject));

        // Run captor verifications
        verify(matchaDbTable).updateData(matchaUpdateQueryCaptor.capture());
        MatchaUpdateQuery capturedMatchaUpdateQuery = matchaUpdateQueryCaptor.getValue();
        Assert.assertTrue(capturedMatchaUpdateQuery.toString()
            .equals(updateEmployeeSalaryQuery.toString()));
    }

    /**
     * This test tests the successful run of the runDeleteCommand method, calling to delete data
     * from the DB, and then returning a success on said delete.
     *
     * This is also testing the construction of the required MatchaDeleteQuery object.
     */
    @Test
    public void testRunDeleteCommand() {
        MatchaDbResponseObject deleteSuccessfulResponseObject
            = new MatchaDbResponseObject("Removal Successful", true);
        when(matchaDbTable.deleteData(any(MatchaDeleteQuery.class))).thenReturn(true);

        // See overall results
        compareResponseObjects(deleteSuccessfulResponseObject,
            matchaDbRequestService.runDeleteCommand(deleteRequestObject));

        // Run captor verifications
        verify(matchaDbTable).deleteData(matchaDeleteQueryCaptor.capture());
        MatchaDeleteQuery capturedMatchaDeleteQuery = matchaDeleteQueryCaptor.getValue();
        Assert.assertTrue(capturedMatchaDeleteQuery.toString().equals(deleteDeskQuery.toString()));
    }


    /**
     * Compares two MatchaDbResponseObjects, and fails if the two do not have comparably equal 
     * fields.
     *
     * @param expected The expected parms for nthe Resueslt Object.
     * @param actual The actual prmeters for the Result Object.
     */
    private void compareResponseObjects(MatchaDbResponseObject expected, 
                                        MatchaDbResponseObject actual) {
        try {
            Assert.assertTrue(expected.toString().equals(actual.toString()));
        } catch (NullPointerException npe) {
            Assert.fail("Null expected or actual response objects");
        }
    }
}