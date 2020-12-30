package com.matchadb.models.query;

/**
 * A class which holds the actual post query to be used on the system.
 */
public class MatchaPostQuery extends MatchaQuery {

    /**
     * Describes the new datasets to be inserted.
     */
    private String[][] insertQuery; 

    public MatchaPostQuery(String[] fromQuery, String[][] selectQuery, String[][] insertQuery) {
        super(fromQuery, selectQuery);
        this.insertQuery = insertQuery;
    }

    /**
     * Gets the insert query.
     *
     * @return the insert query.
     */
    public String[][] getInsertQuery() {
        return this.insertQuery;
    }
}