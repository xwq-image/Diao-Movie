package com.cinema.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cinema.entity.Movie;
import com.cinema.mapper.MovieMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    
    private final MovieMapper movieMapper;
    
    public List<Movie> getMovieList(Integer status) {
        LambdaQueryWrapper<Movie> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Movie::getStatus, status);
        }
        wrapper.orderByDesc(Movie::getCreateTime);
        return movieMapper.selectList(wrapper);
    }
    
    public Movie getMovieDetail(Long id) {
        Movie movie = movieMapper.selectById(id);
        if (movie == null) {
            throw new RuntimeException("影片不存在");
        }
        return movie;
    }
    
    public void createMovie(Movie movie) {
        movieMapper.insert(movie);
    }
    
    public void updateMovie(Movie movie) {
        movieMapper.updateById(movie);
    }
    
    public void deleteMovie(Long id) {
        movieMapper.deleteById(id);
    }
}
