package com.outsera.ms_golden_raspberry_awards.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.outsera.ms_golden_raspberry_awards.dto.IntervalDTO;
import com.outsera.ms_golden_raspberry_awards.dto.MovieDTO;
import com.outsera.ms_golden_raspberry_awards.entity.Movie;
import com.outsera.ms_golden_raspberry_awards.repository.MovieRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MovieService {

    MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public IntervalDTO getIntervals(int limit) {
        log.info("[Service] - Inicio - getIntervals: {}", limit);
        List<Movie> listMovies = movieRepository.findByWinner("yes");

        List<MovieDTO> minInterval = getOrderedIntervalsByFilter(listMovies, "min", limit);
        List<MovieDTO> maxInterval = getOrderedIntervalsByFilter(listMovies, "max", limit);
        log.info("[Service] - Fim - getIntervals: {}", limit);
        return new IntervalDTO(minInterval, maxInterval);
    }

    public List<MovieDTO> getOrderedIntervalsByFilter(List<Movie> listMovies, String filter, int limit) {
        log.info("[Service] - Inicio - getOrderedIntervalsByFilter: {}", limit);
        Map<String, List<Integer>> listAwards = new HashMap<>();

        for (Movie movie : listMovies) {
            int year = movie.getYear();
            String producersStr = movie.getProducers();
            if (producersStr == null)
                continue;
            String[] producers = producersStr.split(",| and | e ");
            for (String producer : producers) {
                producer = producer.trim();
                if (producer.isEmpty())
                    continue;
                listAwards.computeIfAbsent(producer, Sk -> new ArrayList<>()).add(year);
            }
        }
        List<MovieDTO> intervals = new ArrayList<>();

        for (Map.Entry<String, List<Integer>> entry : listAwards.entrySet()) {
            List<Integer> years = entry.getValue();
            if (years.size() < 2)
                continue;
            Collections.sort(years);
            for (int j = 1; j < years.size(); j++) {
                int interval = years.get(j) - years.get(j - 1);
                intervals.add(new MovieDTO(entry.getKey(), interval, years.get(j - 1), years.get(j)));
            }
        }

        if ("min".equalsIgnoreCase(filter)) {
            intervals.sort(Comparator.comparingInt(MovieDTO::getInterval));
        } else if ("max".equalsIgnoreCase(filter)) {
            intervals.sort(Comparator.comparingInt(MovieDTO::getInterval).reversed());
        }

        log.info("[Service] - Fim - getOrderedIntervalsByFilter: {}", limit);
        return intervals.stream().limit(limit).toList();
    }
}
