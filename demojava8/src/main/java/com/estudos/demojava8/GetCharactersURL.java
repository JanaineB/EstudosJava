package com.estudos.demojava8;

import java.util.List;

public class GetCharactersURL {
    private List<String> characters;

    public GetCharactersURL() {
    }

    public GetCharactersURL(List<String> characters) {
        this.characters = characters;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public void setCharacters(List<String> characters) {
        this.characters = characters;
    }

    @Override
    public String toString() {
        return "GetCharactersURL{" +
                "characters=" + characters +
                '}';
    }
}
