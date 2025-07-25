package com.outsera.ms_golden_raspberry_awards.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.outsera.ms_golden_raspberry_awards.dto.IntervalDTO;
import com.outsera.ms_golden_raspberry_awards.service.MovieService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/movie/v1")
public class MovieControllerImpl implements MovieController {

    MovieService movieService;

    public MovieControllerImpl(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/intervals/")
    public ResponseEntity<?> getIntevalsMovies() {
        log.info("[Controller] - getIntevalsMovies");
        try {
            IntervalDTO intervals = movieService.getIntervals();
            return ResponseEntity.ok(intervals);
        } catch (RuntimeException e) {
            log.error("[Controller] - Erro ao obter intervalos: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());

        }

    }
}
