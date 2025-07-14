package com.outsera.ms_golden_raspberry_awards.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.outsera.ms_golden_raspberry_awards.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByWinner(String winner);

}
