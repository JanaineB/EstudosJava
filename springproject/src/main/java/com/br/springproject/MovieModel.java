package com.br.springproject;

public class MovieModel {
  private String name;
  private String thumbnail;

  public MovieModel(String name, String thumbnail) {
    this.name = name;
    this.thumbnail = thumbnail;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  @Override
  public String toString() {
    return "MovieModel{" +
        "name='" + name + '\'' +
        ", thumbnail='" + thumbnail + '\'' +
        '}';
  }
}
