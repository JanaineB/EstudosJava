package com.estudos.demojava8;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Settings {
    @Value("${APIURL:https://swapi.co/api/}")
    private String apiURL;

    public String getApiURL() {
        return apiURL;
    }

}
