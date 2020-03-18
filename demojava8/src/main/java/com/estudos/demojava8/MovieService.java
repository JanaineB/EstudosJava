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

    public List<CharactersModel> fetchMovieCharacters(String id) {
        Optional<GetCharactersURL> optionalRequest = client.request(
                "films/" + id + "/?format=json", HttpMethod.GET, GetCharactersURL.class,
                "charactersurl", false
        );

        GetCharactersURL request = optionalRequest.orElseThrow(RuntimeException::new);

//        return request.getCharacters().forEach(s -> {
//            s.split("api/");
//        });

        List<String> endpoint = request.getCharacters()
                .stream()
                .map(s -> s.split("api/")[1])
                .collect(Collectors.toList());

        return fetchCharacterdata(endpoint);
    }

    public List<CharactersModel> fetchCharacterdata(List<String> endpointsList) {

        return endpointsList
                .stream()
                .map(this::getCharactersModel)
                .collect(Collectors.toList());
    }

    private CharactersModel getCharactersModel(String s) {
        // TODO: Faltando checar no redis antes se o dado ja existe.
        //TODO: nao usar string, usar list no redis
        Optional<CharactersModel> optionalCharactersModel = client.request(
                s, HttpMethod.GET, CharactersModel.class, "characters", true
        );
        return optionalCharactersModel.orElseThrow(RuntimeException::new);
    }
}
