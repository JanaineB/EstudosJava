package com.estudos.demojava8;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private Settings settings;

    private MovieService(Settings settings) {

        this.settings = settings;

    }

    private <T> Optional<T> httpClient(String endpoint, HttpMethod method, Class<T> model) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Application");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return Optional
                .ofNullable(client.exchange(
                        settings.getApiURL() + endpoint,
                        method,
                        entity,
                        model
                ).getBody());
    }

    public List<MovieModel> fetchMovies() {

        Optional<MovieRequest> optionalRequest = httpClient("films/?format=json", HttpMethod.GET, MovieRequest.class);

        MovieRequest request = optionalRequest.orElseThrow(RuntimeException::new);

        return request.getResults();
    }

    public List<CharactersModel> fetchMovieCharacters(String id) {
        Optional<GetCharactersURL> optionalRequest = httpClient("films/" + id + "/?format=json", HttpMethod.GET, GetCharactersURL.class);

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
        Optional<CharactersModel> optionalCharactersModel = httpClient(s, HttpMethod.GET, CharactersModel.class);
        return optionalCharactersModel.orElseThrow(RuntimeException::new);
    }
}
