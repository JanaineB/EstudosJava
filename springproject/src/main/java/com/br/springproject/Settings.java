package com.br.springproject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Settings {
    @Value("${APIURL:https://swapi.co/api/films/?format=json}")
    private String apiURL;

    public String getApiURL() {
        return apiURL;
    }

    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }
}
