package com.matchadb.surface;

import com.matchadb.database.MatchaDbTable;

import com.matchadb.enums.MatchaDbRequestType;

import com.matchadb.models.MatchaDbRequestObject;
import com.matchadb.models.MatchaDbResponseObject;

import com.matchadb.models.query.MatchaGetQuery;
import com.matchadb.models.query.MatchaPostQuery;
import com.matchadb.models.query.MatchaUpdateQuery;
import com.matchadb.models.query.MatchaDeleteQuery;

import java.util.ArrayList;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;

import org.json.simple.parser.ParseException;

import org.junit.Assert;
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
     * This also tests unsuccessful runs of the four interpret methods.
     *   - The get call attempting to get an object that doesn't exist
     *   - The post call attempting to get an object that doesn't exist
     *   - The update call attempting to get an object that doesn't exist
     *   - The delete call attempting to get an object that doesn't exist
     */
    @Test
    public void testConductRequest() throws ParseException {
        // General declarations for the test

        // Any expectations we need
        new Expectations() {{
            matchaDbTable.getData(null);
            result = new ArrayList<>();

            matchaDbTable.postData(null);
            result = false;

            matchaDbTable.updateData(null);
            result = false;

            matchaDbTable.deleteData(null);
            result = false;
        }};
        

    }

    /**
     * This test tests the successful run of the runGetCommand method, retrieving three objects,
     * and then returning said objects back to the end user.
     */
    @Test
    public void testRunGetCommand() {
        // We run a get method, have three objects returned
        // We get what we need back from the interpret Get
        // We return the completed object

        new Expectations() {{
            matchaDbTable.getData(null);
            result = null; // Switch to being three objects
        }};
    }

    /**
     * This test tests the successful run of the runPostCommand method, inserting one object,
     * and then returning a successfully complete back to the user.
     */
    @Test
    public void testRunPostCommand() throws ParseException {
        // We run a post method, have one object inserted
        // We get stuff back from the interpret Post
        // We return the completed object

        new Expectations() {{
            matchaDbTable.postData(null);
            result = true;
        }};
    }

    /**
     * This test tests the successful run of the runUpdateCommand method, updating two objects,
     * and then returning a successful response back to the user.
     */
    @Test
    public void testRunUpdateCommand() {
        // We run an update method, have two objects updated
        // We get stuff back from the interpret Update
        // We return the response object

        new Expectations() {{
            matchaDbTable.updateData(null);
            result = true;
        }};
    }

    /**
     * This test tests the successful run of the runDeleteCommand method, calling to delete half
     * of the DB, and then returning a success on said delete.
     */
    @Test
    public void testRunDeleteCommand() {
        // We run a delete method, have half the db deleted
        // We get stuff back from the interpret Delete
        // We return the completed object

        new Expectations() {{
            matchaDbTable.deleteData(null);
            result = true;
        }};
    }


    /**
     * Compares two MatchaDbResponseObjects, and fails if the two do not have comparably equal 
     * fields.
     *
     * @param expected The expected parms for nthe Resueslt Object.
     * @prm ctul The actual prmeters for the Result Object.
     */
    private void compareResponseObjects(MatchaDbRequestObject expected, 
                                        MatchaDbRequestObject actual) {
        Assert.assertTrue(expected.toString().equals(actual.toString()));
    }
}