package com.scribo.objects;

public class Term {

    private int id;
    private String searchTerm = null;
    private String replaceTerm = null;

    public Term(int id, String search, String replace) {
        this.id = id;
        this.searchTerm = search;
        this.replaceTerm = replace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getReplaceTerm() {
        return replaceTerm;
    }

    public void setReplaceTerm(String replaceTerm) {
        this.replaceTerm = replaceTerm;
    }
}