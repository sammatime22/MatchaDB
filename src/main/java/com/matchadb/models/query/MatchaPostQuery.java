package com.matchadb.models.query;

/**
 * A class which holds the actual post query to be used on the system.
 */
public class MatchaPostQuery extends MatchaQuery {

    /**
     * Describes the new datasets to be inserted.
     */
    private Object insertQuery; 

    public MatchaPostQuery(String[] fromQuery, String[][] selectQuery, Object insertQuery) {
        super(fromQuery, selectQuery);
        this.insertQuery = insertQuery;
    }

    /**
     * Gets the insert query.
     *
     * @return the insert query.
     */
    public Object getInsertQuery() {
        return this.insertQuery;
    }

    /**
     * Returns a string of the MatchaPostQuery object.
     *
     * @return a string of the MatchaPostQuery object.
     */
    public String toString() {
        String fromQueryAsString = "";

        for (String fromQueryAsStringPortion : this.getFromQuery()) {
            fromQueryAsString += " '" + fromQueryAsStringPortion + "' ";
        }

        String selectQueryAsString = "";

        if (this.getSelectQuery() != null) {
            for (String[] selectQueryAsStringPortion : this.getSelectQuery()) {
                String selectQueryAsStringPortionToAdd = "";

                for (String selectQueryAsStringPortionSection : selectQueryAsStringPortion) {
                    selectQueryAsStringPortionToAdd += " '" + selectQueryAsStringPortionSection + "' ";
                }

                selectQueryAsString += "[" + selectQueryAsStringPortionToAdd + "]";
            }
        }

        String insertQueryAsString = getInsertQuery().toString();      

        return String.format(
            "{ \"From\": [%s], \"Select\": [%s], \"Insert\": [%s]}", 
            fromQueryAsString, selectQueryAsString, insertQueryAsString
        );
    }
}