package com.outsera.ms_golden_raspberry_awards.controller;

import org.springframework.web.bind.annotation.GetMapping;

import com.outsera.ms_golden_raspberry_awards.dto.IntervalDTO;

public interface MovieController {

    @GetMapping("/intervals/{filter}")
    IntervalDTO getIntevalsByFilter();
}
