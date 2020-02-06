package com.estudos.demojava8;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private Settings settings;

    private MovieService(Settings settings){

        this.settings = settings;

    }

    public List<MovieModel> fetchMovies(){

        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Application");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        Optional<MovieRequest> optionalRequest = Optional
                .ofNullable(client.exchange(settings.getApiURL(), HttpMethod.GET, entity, MovieRequest.class).getBody());

        MovieRequest request = optionalRequest.orElseThrow(RuntimeException::new);

        return request.getResults();
    }
}
