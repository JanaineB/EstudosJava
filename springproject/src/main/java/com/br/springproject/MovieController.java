package com.br.springproject;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class MovieController {

  @GetMapping("/hello")
  public String helloWorld(){
    return "Hello World";
  }

  @GetMapping("/movie")
  public List<MovieModel> getMovies(){
    return List.of(new MovieModel("star wars","O 3 O"));
  }
}
