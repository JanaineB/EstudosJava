package com.br.springproject;

import java.util.List;

public class MovieRequest {
    private List <MovieModel> results;

    public List<MovieModel> getResults() {
        return results;
    }

    public void setResults(List<MovieModel> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "MovieRequest{" +
                "results=" + results +
                '}';
    }
}
