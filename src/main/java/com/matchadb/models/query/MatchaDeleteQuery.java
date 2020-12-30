package com.matchadb.models.query;

/**
 * A class which holds the actual delete query to be used on the system.
 */
public class MatchaDeleteQuery extends MatchaQuery {

    public MatchaDeleteQuery(String[] fromQuery, String[][] selectQuery) {
        super(fromQuery, selectQuery);
    }

}