package com.br.springproject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
  //TODO: handle exceptions
  public List<MovieModel> fetchMovies() throws IOException, InterruptedException {
    //TODO: Check REDIS
    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create("https://swapi.co/api/films/?format=json"))//TODO: handle multiple envs
        .build();

    //TODO: Change to async
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    // print response body
    System.out.println(response.body());

    return null;
  }
}
