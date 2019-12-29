package com.br.springproject;

import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class MovieController {

  private MovieService movieService;

  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @GetMapping("/hello")
  public String helloWorld(){
    return "Hello World";
  }

  @GetMapping("/movie")
  public List<MovieModel> getMovies() throws IOException, InterruptedException {
    return movieService.fetchMovies();
  }
}
