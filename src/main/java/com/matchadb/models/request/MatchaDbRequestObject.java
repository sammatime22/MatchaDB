package com.matchadb.models;

import com.matchadb.enums.MatchaDbRequestType;

/**
 * The Request Object of the MatchaDb system, which is the actual "request" to
 * to be used on the database.
 */
public class MatchaDbRequestObject {

    // The Request Type of choices GET/POST/UPDATE/DELETE
    private MatchaDbRequestType requestType;

    private String[] fromQuery;

    private String[][] selectQuery;

    private String[][] insertQuery;

    private String[][] updateQuery;

    public MatchaDbRequestObject(MatchaDbRequestType requestType, String[] fromQuery,
        String[][] selectQuery, String[][] insertQuery, String[][] updateQuery) {
        this.requestType = requestType;
        this.fromQuery = fromQuery;
        this.selectQuery = selectQuery;
        this.insertQuery = insertQuery;
        this.updateQuery = updateQuery;
    }

    /**
     * Gets the query provided to the system.
     *
     * @return the query provided to the system.
     */
    public MatchaDbRequestType getRequestType() {
        return this.requestType;
    }

    /**
     * Returns a from query that existed on the request.
     *
     * @return a from query that existed on the request.
     */
    public String[] getFromQuery() {
        return this.fromQuery;
    }

    /**
     * Returns a select query that existed on the request.
     *
     * @return a select query that existed on the request.
     */
    public String[][] getSelectQuery() {
        return this.selectQuery;
    }

    /**
     * Returns an insert query that existed on the request.
     *
     * @return an insert query that existed on the request.
     */
    public String[][] getInsertQuery() {
        return this.insertQuery;
    }

    /**
     * Returns an update query that existed on the request.
     *
     * @return an update query that existed on the request.
     */
    public String[][] getUpdateQuery() {
        return this.updateQuery;
    }
}