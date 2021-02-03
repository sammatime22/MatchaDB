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

        for (String[] selectQueryAsStringPortion : this.getSelectQuery()) {
            String selectQueryAsStringPortionToAdd = "";

            for (String selectQueryAsStringPortionSection : selectQueryAsStringPortion) {
                selectQueryAsStringPortionToAdd += selectQueryAsStringPortionSection;
            }

            selectQueryAsString += " '" + selectQueryAsStringPortionToAdd + "' ";
        }

        String updateQueryAsString = "";

        for (String[] updateQueryAsStringPortion : this.getUpdateQuery()) {
            String updateQueryAsStringPortionToAdd = "";

            for (String updateQueryAsStringPortionSection : updateQueryAsStringPortion) {
                updateQueryAsStringPortionToAdd += updateQueryAsStringPortionSection;
            }

            updateQueryAsString += " '" + updateQueryAsStringPortionToAdd + "' ";
        }        

        return String.format(
            "{ \"From\": [%s], \"Select\": [%s], \"Insert\": [%s]}", 
            fromQueryAsString, selectQueryAsString, updateQueryAsString
        );
    }
}