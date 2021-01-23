package com.matchadb.surface;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.matchadb.database.MatchaDbTable;

import com.matchadb.enums.MatchaDbRequestType;

import com.matchadb.models.MatchaDbRequestObject;
import com.matchadb.models.MatchaDbResponseObject;

import com.matchadb.models.query.MatchaGetQuery;
import com.matchadb.models.query.MatchaPostQuery;
import com.matchadb.models.query.MatchaUpdateQuery;
import com.matchadb.models.query.MatchaDeleteQuery;

import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * This class tests the MatchaDbRequestService.
 */
public class MatchaDbRequestServiceTest {

    static MatchaDbRequestService matchaDbRequestService;

    static MatchaDbTable matchaDbTable;

    // Request Objects throughout the tests
    MatchaDbRequestObject getRequestObject = new MatchaDbRequestObject(MatchaDbRequestType.GET, 
        "{\"From\": \"Eggs\", \"Select\":[\"'Shell Color' is 'Green'\"]}");
    MatchaDbRequestObject postRequestObject = new MatchaDbRequestObject(MatchaDbRequestType.POST, 
        "{\"From\": \"Customer 4\", \"Insert\":[\"'Shell Color' is 'Green'\"]}");
    MatchaDbRequestObject updateRequestObject = new MatchaDbRequestObject(MatchaDbRequestType.UPDATE,
        "{\"From\": \"Eggs\", \"Select\":[\"'Shell Color' is 'Green'\"]," + 
        "\"Update\": \"\"}");
    MatchaDbRequestObject deleteRequestObject = new MatchaDbRequestObject(MatchaDbRequestType.DELETE,
        "{\"From\": \"Eggs\", \"Select\":[\"'Shell Color' is 'Green'\"]}");

    // Queries used throughout the tests
    MatchaGetQuery getGreenEggsQuery = new MatchaGetQuery(new String[] {"Eggs"},
        new String[][] {{"Shell Color", "is", "Green"}});
    MatchaPostQuery postTwoCupsOfTea = new MatchaPostQuery(new String[] {"Customer 4"},
        null, new String[][] {{"Order", "Earl Grey"}, {"Order", "Matcha"}});
    MatchaUpdateQuery updateEmployeeSalary = new MatchaUpdateQuery(new String[] {"Employee"},
        new String[][] {{"Name", "is", "Prince"}}, 
        new String[][] {{"Update", "Salary", "$20/hr"}});
    MatchaDeleteQuery deleteDesk = new MatchaDeleteQuery(new String[] {"Desks"},
        new String[][] {{"Characteristic", "is", "mean"}});


    /**
     * Instantiates our Request Service and sets up the mock Matcha DB Table.
     */
    @BeforeAll
    public static void setup() {
        matchaDbRequestService = new MatchaDbRequestService();
        matchaDbTable = mock(MatchaDbTable.class);
    }

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
    public void testConductRequest() throws ParseException {
        // Get Test
        MatchaDbResponseObject getFailedResponseObject 
            = new MatchaDbResponseObject("Retrieval Failed", "");
        when(matchaDbTable.getData(getGreenEggsQuery)).thenReturn(null);
        Assert.assertEquals(matchaDbTable.getData(getGreenEggsQuery), null);
        compareResponseObjects(getFailedResponseObject, 
            matchaDbRequestService.conductRequest(getRequestObject));

        // Post Test
        MatchaDbResponseObject postFailedResponseObject 
            = new MatchaDbResponseObject("Insert Failed", "");
        when(matchaDbTable.postData(postTwoCupsOfTea)).thenReturn(false);
        Assert.assertFalse(matchaDbTable.postData(postTwoCupsOfTea));
        compareResponseObjects(postFailedResponseObject, 
            matchaDbRequestService.conductRequest(postRequestObject));

        // Update Test
        MatchaDbResponseObject updateFailedResponseObject 
            = new MatchaDbResponseObject("Update Failed", "");
        when(matchaDbTable.updateData(updateEmployeeSalary)).thenReturn(false);
        Assert.assertFalse(matchaDbTable.updateData(updateEmployeeSalary));
        compareResponseObjects(updateFailedResponseObject, 
            matchaDbRequestService.conductRequest(updateRequestObject));

        // Delete Test
        MatchaDbResponseObject deleteFailedResponseObject 
            = new MatchaDbResponseObject("Removal Failed", "");
        when(matchaDbTable.deleteData(deleteDesk)).thenReturn(false);
        Assert.assertFalse(matchaDbTable.deleteData(deleteDesk));
        compareResponseObjects(deleteFailedResponseObject, 
            matchaDbRequestService.conductRequest(deleteRequestObject));
    }

    /**
     * This test tests the successful run of the runGetCommand method, retrieving three objects,
     * and then returning said objects back to the end user.
     */
    @Test
    public void testRunGetCommand() {
        // MatchaDbResponseObject getFailedResponseObject 
        //     = new MatchaDbResponseObject("Retrieval Failed", "");
        // when(matchaDbTable.getData(getGreenEggsQuery)).thenReturn(null);
        // Assert.assertEquals(matchaDbTable.getData(getGreenEggsQuery), null);
        // compareResponseObjects(getFailedResponseObject, 
        //     matchaDbRequestService.conductRequest(getRequestObject));
    }

    /**
     * This test tests the successful run of the runPostCommand method, inserting one object,
     * and then returning a successfully complete back to the user.
     */
    @Test
    public void testRunPostCommand() throws ParseException {
        // MatchaDbResponseObject postFailedResponseObject 
        //     = new MatchaDbResponseObject("Insert Failed", "");
        // when(matchaDbTable.postData(postTwoCupsOfTea)).thenReturn(false);
        // Assert.assertFalse(matchaDbTable.postData(postTwoCupsOfTea));
        // compareResponseObjects(postFailedResponseObject, 
        //     matchaDbRequestService.conductRequest(postRequestObject));
    }

    /**
     * This test tests the successful run of the runUpdateCommand method, updating two objects,
     * and then returning a successful response back to the user.
     */
    @Test
    public void testRunUpdateCommand() {
        // MatchaDbResponseObject updateFailedResponseObject 
        //     = new MatchaDbResponseObject("Update Failed", "");
        // when(matchaDbTable.updateData(updateEmployeeSalary)).thenReturn(false);
        // Assert.assertFalse(matchaDbTable.updateData(updateEmployeeSalary));
        // compareResponseObjects(updateFailedResponseObject, 
        //     matchaDbRequestService.conductRequest(updateRequestObject));
    }

    /**
     * This test tests the successful run of the runDeleteCommand method, calling to delete half
     * of the DB, and then returning a success on said delete.
     */
    @Test
    public void testRunDeleteCommand() {
        // MatchaDbResponseObject deleteFailedResponseObject 
        //     = new MatchaDbResponseObject("Removal Failed", "");
        // when(matchaDbTable.deleteData(deleteDesk)).thenReturn(false);
        // Assert.assertFalse(matchaDbTable.deleteData(deleteDesk));
        // compareResponseObjects(deleteFailedResponseObject, 
        //     matchaDbRequestService.conductRequest(deleteRequestObject));
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