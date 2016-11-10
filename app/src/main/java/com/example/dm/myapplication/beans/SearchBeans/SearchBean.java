package com.example.dm.myapplication.beans.SearchBeans;

import java.io.Serializable;
import java.util.List;

/**
 * SearchBean
 * Created by dm on 16-11-10.
 */

public class SearchBean implements Serializable {
    private int count;
    private boolean error;
    private List<SearchResultsBean> results;

    public SearchBean() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<SearchResultsBean> getResults() {
        return results;
    }

    public void setResults(List<SearchResultsBean> results) {
        this.results = results;
    }
}
