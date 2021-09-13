package com.matchadb.models.request;

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

    private Object insertQuery;

    private String[][] updateQuery;

    public MatchaDbRequestObject(MatchaDbRequestType requestType, String[] fromQuery,
        String[][] selectQuery, Object insertQuery, String[][] updateQuery) {
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
    public Object getInsertQuery() {
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

    /**
     * Returns a string representation of the MatchaDbRequestObject.
     * 
     * @return a string representation of the MatchaDbRequestObject.
     */
    public String toString() {
        String fromQueryAsString = "";
        
        if (this.getFromQuery() != null) {
            for (String fromQueryAsStringPortion : this.getFromQuery()) {
                fromQueryAsString += "['" + fromQueryAsStringPortion + "']";
            }
        }

        String selectQueryAsString = "";

        if (this.getSelectQuery() != null) {
            for (String[] selectQueryAsStringPortion : this.getSelectQuery()) {
                String selectQueryAsStringPortionToAdd = "";

                for (String selectQueryAsStringPortionSection : selectQueryAsStringPortion) {
                    selectQueryAsStringPortionToAdd += " '" + selectQueryAsStringPortionSection + "' ";
                }

                selectQueryAsString += "["+ selectQueryAsStringPortionToAdd + "]";
            }
        }

        String insertQueryAsString = getInsertQuery() != null ? getInsertQuery().toString() : "";

        String updateQueryAsString = "";

        if (this.getUpdateQuery() != null) {
            for (String[] updateQueryAsStringPortion : this.getUpdateQuery()) {
                String updateQueryAsStringPortionToAdd = "";

                for (String updateQueryAsStringPortionSection : updateQueryAsStringPortion) {
                    updateQueryAsStringPortionToAdd += " '" + updateQueryAsStringPortionSection + "' ";
                }

                updateQueryAsString += "[" + updateQueryAsStringPortionToAdd + "]";
            }
        }

        return String.format(
            "{\"From\": [%s], \"Select\": [%s], \"Insert\": [%s], \"Update\": [%s]}",
            fromQueryAsString, selectQueryAsString, insertQueryAsString, updateQueryAsString
        );
    }
}