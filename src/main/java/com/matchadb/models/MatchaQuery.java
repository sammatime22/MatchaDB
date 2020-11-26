package com.matchadb.models;

/**
 * A class which holds the actual query to be used on the system.
 */
public class MatchaQuery {

    private String[] fromQuery;

    private String[][] selectQuery; 

    public MatchaQuery(String[] fromQuery, String[][] selectQuery) {
        this.fromQuery = fromQuery;
        this.selectQuery = selectQuery;
    }

    /**
     * Gets the "from query" of the query.
     *
     * @return the "from query" of the query.
     */
    public String[] getFromQuery() {
        return this.fromQuery;
    }

    /**
     * Gets the select query.
     *
     * @return the select query.
     */
    public String[][] getSelectQuery() {
        return this.selectQuery;
    }
}