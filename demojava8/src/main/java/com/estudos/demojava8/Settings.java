package com.estudos.demojava8;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Settings {
    @Value("${APIURL:https://swapi.co/api/}")
    private String apiURL;
    @Value("${REDISHOST:localhost}")
    private String redisHost;
    @Value("${REDISPORT:6379}")
    private int redisPort;
    @Value("${REDISAUTH:123}")
    private String redisAuth;

    public String getRedisHost() {
        return redisHost;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public String getRedisAuth() {
        return redisAuth;
    }

    public String getApiURL() {
        return apiURL;
    }

}
