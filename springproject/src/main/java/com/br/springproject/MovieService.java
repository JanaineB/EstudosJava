package com.br.springproject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
    private Settings settings;

    public MovieService(Settings settings) {
        this.settings = settings;
    }

    //TODO: handle exceptions
    public List<MovieModel> fetchMovies() throws IOException, InterruptedException {
        //TODO: Check REDIS
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(settings.getApiURL()))
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        responseFuture.thenApply(response -> {
            System.out.println("________\nRESPONDEU\n_________");
            Gson gson = new Gson();
            MovieRequest movie = gson.fromJson(response.body(), MovieRequest.class);
            System.out.println("\n*********\n" + movie.toString());
            return movie;
        });

        return null;
    }
}
