package com.br.springproject;

import java.util.List;

public class MovieRequest {
    private List <MovieModel> results;
    private String error;

    public MovieRequest() {
    }

    public MovieRequest(String error) {
        this.error = error;
    }

    public List<MovieModel> getResults() {
        return results;
    }

    public void setResults(List<MovieModel> results) {
        this.results = results;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "MovieRequest{" +
                "results=" + results +
                ", error='" + error + '\'' +
                '}';
    }
}
