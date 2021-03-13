package com.matchadb.common;

/**
 * A class containing many of the constants used throughout the software.
 */
public final class MatchaDbConstants {

    // The info values for successful commands on the DB Table.
    public static final String SUCCESSFUL_GET_INFO = "Retrieval Successful";
    public static final String SUCCESSFUL_POST_INFO = "Insert Successful";
    public static final String SUCCESSFUL_UPDATE_INFO = "Update Successful";
    public static final String SUCCESSFUL_DELETE_INFO = "Removal Successful";

    // The info values for unsuccessful commands on the DB Table.
    public static final String UNSUCCESSFUL_GET_INFO = "Retrieval Failed";
    public static final String UNSUCCESSFUL_POST_INFO = "Insert Failed";
    public static final String UNSUCCESSFUL_UPDATE_INFO = "Update Failed";
    public static final String UNSUCCESSFUL_DELETE_INFO = "Removal Failed";  

    // Used if the command could not be identified.
    public static final String COMMAND_UNIDENTIFIABLE = "Command could not be identified...";
}
