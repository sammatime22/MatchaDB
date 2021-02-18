package com.matchadb.common;

import com.matchadb.models.response.MatchaDbResponseObject;

import org.junit.Assert;

/**
 * A test utility class within the MatchaDb codebase, containing helper methods and common object
 * comparison tests.
 */
public class MatchaDbTestUtils {

    /**
     * Compares two MatchaDbResponseObjects, and fails if the two do not have comparably equal 
     * fields.
     *
     * @param expected The expected parms for nthe Resueslt Object.
     * @param actual The actual prmeters for the Result Object.
     */
    public static void compareResponseObjects(MatchaDbResponseObject expected, 
                                        MatchaDbResponseObject actual) {
        try {
            //System.out.println(expected.toString());
            //System.out.println(actual.toString());
            Assert.assertTrue(expected.toString().equals(actual.toString()));
        } catch (NullPointerException npe) {
            Assert.fail("Null expected or actual response objects");
        }
    }
}