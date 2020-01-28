package com.br.springproject;

import com.google.gson.Gson;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.vavr.control.Try;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
public class MovieService {
    private Settings settings;

    public MovieService(Settings settings) {
        this.settings = settings;
    }

    //TODO: handle exceptions
    public CompletableFuture<HttpResponse<String>> decorateAPIcall(){
        //TODO: Check REDIS
//        Supplier<CompletionStage<String>> completionStageSupplier =
//                () -> CompletableFuture.supplyAsync(helloWorldService::returnHelloWorld);
//
//        Supplier<CompletionStage<String>> decoratedCompletionStage = Decorators.ofCompletionStage(completionStageSupplier)
//                .withCircuitBreaker(circuitBreaker)
//                .withRetry(Retry.ofDefaults("id"), Executors.newSingleThreadScheduledExecutor())
//                .decorate();

        Retry retry = Retry.ofDefaults("requestStartWarsAPI");

        Supplier<CompletableFuture<HttpResponse<String>>> supplier = () -> fetchMovies();

        Supplier<CompletableFuture<HttpResponse<String>>> decoratedSupplier = Decorators.ofSupplier(supplier)
                .withRetry(retry)
                .decorate();

        String result = Try.ofSupplier(decoratedSupplier);

        System.out.println("\n_______________\n RESULT: "+result+"\n_________________\n");

        return null;
    }

    public CompletableFuture<HttpResponse<String>> fetchMovies(){
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(settings.getApiURL()))
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        responseFuture
                .thenApply(response -> {
                    Gson gson = new Gson();
                    return gson.fromJson(response.body(), MovieRequest.class);
                })
                .exceptionally(error -> new MovieRequest(error.getMessage()));

        return responseFuture;
    }
}
