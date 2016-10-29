package com.example.dm.myapplication.beans.VideoBeans;

import java.io.Serializable;
import java.util.List;

/**
 * VideosBean
 * Created by dm on 16-10-29.
 */
public class VideosBean implements Serializable {
    private boolean error;
    List<VideoResultsBean> results;

    public VideosBean() {
    }

    public VideosBean(boolean error, List<VideoResultsBean> results) {
        this.error = error;
        this.results = results;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<VideoResultsBean> getResults() {
        return results;
    }

    public void setResults(List<VideoResultsBean> results) {
        this.results = results;
    }
}
