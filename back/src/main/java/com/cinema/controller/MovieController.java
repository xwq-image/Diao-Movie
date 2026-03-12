package com.cinema.controller;

import com.cinema.common.Result;
import com.cinema.entity.Movie;
import com.cinema.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/list")
    public Result<List<Movie>> getMovieList(@RequestParam(required = false) Integer status) {
        List<Movie> movies = movieService.getMovieList(status);
        return Result.success(movies);
    }

    @GetMapping("/detail/{id}")
    public Result<Movie> getMovieDetail(@PathVariable Long id) {
        Movie movie = movieService.getMovieDetail(id);
        return Result.success(movie);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> createMovie(@RequestBody Movie movie) {
        movieService.createMovie(movie);
        return Result.success();
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateMovie(@RequestBody Movie movie) {
        movieService.updateMovie(movie);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return Result.success();
    }
}
