package com.estudos.demojava8;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private RedisConnection redis;
    private CachedHttpClient client;

    private MovieService(RedisConnection redis, CachedHttpClient client) {

        this.client = client;
        this.redis = redis;

    }
    //TODO: E as excessao? vai pa onde?
    //TODO: E a resiliencia?
    public List<MovieModel> fetchMovies() {
        MovieRequest request = redis.checkRedis("movies", MovieRequest.class).orElseGet(() -> {
            Optional<MovieRequest> optionalRequest = client.request(
                    "films/?format=json", HttpMethod.GET, MovieRequest.class, "movies", true
            );

            return optionalRequest.orElseThrow(RuntimeException::new);
        });

        return request.getResults();
    }

    public List<CharactersModel> fetchMovieCharacters(String filmid) {
        List<CharactersModel> request = redis.checkRedis(filmid, List.class).orElseGet(() -> {
            Optional<GetCharactersURL> optionalRequest = client.request(
                    "films/" + filmid + "/?format=json", HttpMethod.GET, GetCharactersURL.class,
                    "charactersurl", false
            );

            GetCharactersURL response = optionalRequest.orElseThrow(RuntimeException::new);

            List<String> endpoint = response.getCharacters()
                    .stream()
                    .map(s -> s.split("api/")[1])// no slipt tudo q vem antes do api/ é [0] e [1] depois
                    .collect(Collectors.toList());

            return fetchCharacterdata(endpoint, filmid);
        });
        return request;
    }

    public List<CharactersModel> fetchCharacterdata(List<String> endpointsList, String filmid) {

        return endpointsList
                .stream()
                .map(s->getCharactersModel(s, filmid))
                .collect(Collectors.toList());
    }

    private CharactersModel getCharactersModel(String endpoint, String filmid) {
        // TODO: Faltando checar no redis antes se o dado ja existe.
        //TODO: nao usar string, usar list no redis

        Optional<CharactersModel> optionalCharactersModel = client.requestList(
                endpoint, HttpMethod.GET, CharactersModel.class, filmid);
        return optionalCharactersModel.orElseThrow(RuntimeException::new);
    }
}
