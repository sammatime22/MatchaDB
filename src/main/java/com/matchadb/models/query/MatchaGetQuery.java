package com.matchadb.models.query;

/**
 * A class which holds the actual get query to be used on the system.
 */
public class MatchaGetQuery extends MatchaQuery {

    public MatchaGetQuery(String[] fromQuery, String[][] selectQuery) {
        super(fromQuery, selectQuery);
    }

}