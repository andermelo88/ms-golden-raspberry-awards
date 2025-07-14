package com.outsera.ms_golden_raspberry_awards.dto;

import java.util.List;

public class IntervalDTO {

    private List<MovieDTO> min;
    private List<MovieDTO> max;

    public IntervalDTO() {
        // Default constructor
    }

    public IntervalDTO(List<MovieDTO> min, List<MovieDTO> max) {
        this.min = min;
        this.max = max;
    }

    public List<MovieDTO> getMin() {
        return min;
    }

    public void setMin(List<MovieDTO> min) {
        this.min = min;
    }

    public List<MovieDTO> getMax() {
        return max;
    }

    public void setMax(List<MovieDTO> max) {
        this.max = max;
    }
}