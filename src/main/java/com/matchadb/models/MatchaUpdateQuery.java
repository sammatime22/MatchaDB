package com.matchadb.models;

/**
 * A class which holds the actual update query to be used on the system.
 */
public class MatchaUpdateQuery extends MatchaQuery {

    /**
     * Describes the conditions on which a set of data should be selected.
     */
    private String[][] updateQuery; 

    public MatchaUpdateQuery(String[] fromQuery, String[][] selectQuery, String[][] updateQuery) {
        super(fromQuery, selectQuery);
        this.updateQuery = updateQuery;
    }

    /**
     * Gets the update query.
     *
     * @return the update query.
     */
    public String[][] getUpdateQuery() {
        return this.updateQuery;
    }
}