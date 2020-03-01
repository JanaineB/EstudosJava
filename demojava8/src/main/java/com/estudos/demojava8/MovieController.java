package com.estudos.demojava8;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {

    private  MovieService service;

    public  MovieController(MovieService service){
        this.service = service;
    }

    @GetMapping("/movies")
    public List<MovieModel> getMovies (){
        return service.fetchMovies();
    }

    @GetMapping("/movies/{id}/characters")
    public  List<CharactersModel> getCharacters (@PathVariable String id){
        return service.fetchMovieCharacters(id);
    }
}
