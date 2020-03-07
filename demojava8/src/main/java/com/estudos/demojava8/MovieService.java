package com.estudos.demojava8;

import com.google.gson.Gson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private Settings settings;

    private MovieService(Settings settings) {

        this.settings = settings;

    }
    //TODO: extrair metodos redis para uma classe redis
    private <T> Optional<T> checkRedis(String key, Class<T> model) {
        //TODO: Abstrair auth para env var
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.auth("123");

        String value = jedis.get(key);
        jedis.close();
        System.out.println("############### VALUE: " + value);

        return Optional.ofNullable(new Gson().fromJson(value, model));
    }

    private boolean saveRedis(String key, String value) {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.auth("123");
        //TODO: Precisa usar o pool
        jedis.set(key, value);
        return true;
    }
    //TODO: extrair client para uma classe propria, tbm é necessario mudar o nome p/ indicar uso de cache
    private <T> Optional<T> httpClient(String endpoint, HttpMethod method, Class<T> model, String cacheKey) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Application");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        T response = client.exchange(
                settings.getApiURL() + endpoint,
                method,
                entity,
                model
        ).getBody();

        saveRedis(cacheKey, new Gson().toJson(response));

        return Optional.ofNullable(response);
    }

    public List<MovieModel> fetchMovies() {
        MovieRequest request = checkRedis("movies", MovieRequest.class).orElseGet(() -> {
            Optional<MovieRequest> optionalRequest = httpClient("films/?format=json", HttpMethod.GET, MovieRequest.class, "movies");

            return optionalRequest.orElseThrow(RuntimeException::new);
        });

        return request.getResults();
    }

    public List<CharactersModel> fetchMovieCharacters(String id) {
        //TODO: essa chamada nao precisa ser salva no Redis. Vc q lute!
        Optional<GetCharactersURL> optionalRequest = httpClient("films/" + id + "/?format=json", HttpMethod.GET, GetCharactersURL.class, "charactersurl");

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
        //TODO: nao usar string, usar list no redis
        Optional<CharactersModel> optionalCharactersModel = httpClient(s, HttpMethod.GET, CharactersModel.class, "characters");
        return optionalCharactersModel.orElseThrow(RuntimeException::new);
    }
}
