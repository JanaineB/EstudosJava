package com.estudos.demojava8;

import com.google.gson.Gson;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Configuration
public class CachedHttpClient {
    private Settings settings;
    private RedisConnection redis;

    public CachedHttpClient(Settings settings, RedisConnection redis) {
        this.redis = redis;
        this.settings = settings;

    }
    public <T> Optional<T> request (String endpoint, HttpMethod method, Class<T> model, String cacheKey, Boolean redisFlag) {
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

        if(redisFlag) {
            redis.saveRedis(cacheKey, new Gson().toJson(response));
        }

        return Optional.ofNullable(response);
    }
    public <T> Optional <T> requestList(String endpoint, HttpMethod method, Class<T> model, String cacheKey){
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

        redis.saveListRedis(cacheKey, new Gson().toJson(response));

        return Optional.ofNullable(response);
    }

}
