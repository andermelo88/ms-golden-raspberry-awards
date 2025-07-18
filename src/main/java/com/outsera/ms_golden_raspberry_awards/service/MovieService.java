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

    public IntervalDTO getIntervals() {
        log.info("[Service] - Inicio - getIntervals");
        List<Movie> listMovies = movieRepository.findByWinner("yes");

        List<MovieDTO> minInterval = getOrderedIntervalsByFilter(listMovies, "min");
        List<MovieDTO> maxInterval = getOrderedIntervalsByFilter(listMovies, "max");
        log.info("[Service] - Fim - getIntervals: {}");
        return new IntervalDTO(minInterval, maxInterval);
    }

    public List<MovieDTO> getOrderedIntervalsByFilter(List<Movie> listMovies, String filter) {
        log.info("[Service] - Inicio - getOrderedIntervalsByFilter");
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

        if (intervals.isEmpty()) {
            return Collections.emptyList();
        }

        List<MovieDTO> result = new ArrayList<>();
        if ("min".equalsIgnoreCase(filter)) {
            int min = intervals.stream().mapToInt(MovieDTO::getInterval).min().orElse(Integer.MAX_VALUE);
            result = intervals.stream()
                    .filter(dto -> dto.getInterval() == min)
                    .sorted(Comparator.comparingInt(MovieDTO::getPreviousWin))
                    .toList();
        } else if ("max".equalsIgnoreCase(filter)) {
            int max = intervals.stream().mapToInt(MovieDTO::getInterval).max().orElse(Integer.MIN_VALUE);
            result = intervals.stream()
                    .filter(dto -> dto.getInterval() == max)
                    .sorted(Comparator.comparingInt(MovieDTO::getPreviousWin))
                    .toList();
        }

        log.info("[Service] - Fim - getOrderedIntervalsByFilter");
        return result;

    }
}
