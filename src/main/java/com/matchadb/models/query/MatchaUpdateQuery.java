package com.matchadb.models.query;

/**
 * A class which holds the actual update query to be used on the system.
 */
public class MatchaUpdateQuery extends MatchaQuery {

    /**
     * Describes the updates that will be applied to the database.
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