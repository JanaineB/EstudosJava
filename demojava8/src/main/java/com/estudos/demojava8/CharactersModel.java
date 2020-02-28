package com.estudos.demojava8;

public class CharactersModel {
    private String name;

    public CharactersModel() {
    }

    public CharactersModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CharactersModel{" +
                "name='" + name + '\'' +
                '}';
    }
}
