package com.matchadb.models.query;

/**
 * A class which holds the actual query to be used on the system.
 */
public class MatchaQuery {

    /**
     * A query that defines exactly where the expected data should come from 
     * within the database.
     */
    private String[] fromQuery;

    /**
     * Describes the conditions on which a set of data should be selected.
     */
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

    /**
     * Returns a string of the MatchaQuery object.
     *
     * @return a string of the MatchaQuery object.
     */
    public String toString() {
        String fromQueryAsString = "";

        for (String fromQueryAsStringPortion : this.getFromQuery()) {
            fromQueryAsString += " '" + fromQueryAsStringPortion + "' ";
        }

        String selectQueryAsString = "";

        for (String[] selectQueryAsStringPortion : this.getSelectQuery()) {
            String selectQueryAsStringPortionToAdd = "";

            for (String selectQueryAsStringPortionSection : selectQueryAsStringPortion) {
                selectQueryAsStringPortionToAdd += " '" + selectQueryAsStringPortionSection + "' ";
            }

            selectQueryAsString += "[" + selectQueryAsStringPortionToAdd + "]";
        }

        return String.format(
            "{ \"From\": [%s], \"Select\": [%s]}", 
            fromQueryAsString, selectQueryAsString
        );
    }
}