package com.outsera.ms_golden_raspberry_awards.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


public interface MovieController {

    @GetMapping("/intervals/{filter}")
    ResponseEntity<?> getIntevalsMovies();
}
