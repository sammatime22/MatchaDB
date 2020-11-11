package com.matchadb.models;

/**
 * A class which holds the actual query to be used on the system.
 */
public class MatchaQuery {

    private String[] diveSections;

    private String[] subsetQuery;

    public MatchaQuery(String[] diveSections, String[] subsetQuery) {
        this.diveSections = diveSections;
        this.subsetQuery = subsetQuery;
    }

    /**
     * Gets the dive sections of the query.
     *
     * @return the dive sections of the query.
     */
    public String[] getDiveSections() {
        return this.diveSections;
    }

    /**
     * Gets the subset query.
     *
     * @return the subset query.
     */
    public String[] getSubsetQuery() {
        return this.subsetQuery;
    }
}