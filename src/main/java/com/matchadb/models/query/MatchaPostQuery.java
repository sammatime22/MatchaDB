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

        String insertQueryAsString = "";

        for (String[] insertQueryAsStringPortion : this.getInsertQuery()) {
            String insertQueryAsStringPortionToAdd = "";

            for (String insertQueryAsStringPortionSection : insertQueryAsStringPortion) {
                insertQueryAsStringPortionToAdd += " '" + insertQueryAsStringPortionSection + "' ";
            }

            insertQueryAsString += "[" + insertQueryAsStringPortionToAdd + "]";
        }        

        return String.format(
            "{ \"From\": [%s], \"Select\": [%s], \"Insert\": [%s]}", 
            fromQueryAsString, selectQueryAsString, insertQueryAsString
        );
    }
}