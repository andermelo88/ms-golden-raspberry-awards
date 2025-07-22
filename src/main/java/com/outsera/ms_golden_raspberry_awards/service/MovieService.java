package com.outsera.ms_golden_raspberry_awards.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.outsera.ms_golden_raspberry_awards.dto.IntervalDTO;
import com.outsera.ms_golden_raspberry_awards.dto.MovieDTO;
import com.outsera.ms_golden_raspberry_awards.entity.Movie;
import com.outsera.ms_golden_raspberry_awards.repository.MovieRepository;
import com.outsera.ms_golden_raspberry_awards.util.CsvHashValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MovieService {

    MovieRepository movieRepository;

    private final CsvReader csvReader;
    private final CsvHashValidator csvHashValidator;

    public MovieService(MovieRepository movieRepository, CsvReader csvReader, CsvHashValidator csvHashValidator) {
        this.csvReader = csvReader;
        this.movieRepository = movieRepository;
        this.csvHashValidator = csvHashValidator;
    }

    public IntervalDTO getIntervals() {

        log.info("[Service] - Inicio - getIntervals");

        if (csvHashValidator.csvIsNotModified()) {
            log.info("[Service] - CSV file is valid");

            List<Movie> listMovies = csvReader.readCsv();

            Map<String, List<Integer>> listAwards = new HashMap<>();

            Iterator<Movie> iterador = listMovies.iterator();
            while (iterador.hasNext()) {
                Movie elemento = iterador.next();
                if (elemento.getWinner().isEmpty() || elemento.getWinner() == null) {
                    iterador.remove();
                }
            }
            for (Movie movie : listMovies) {
                int year = movie.getYear();
                if (year == 0)
                    continue;
                String producersStr = movie.getProducers();
                if (producersStr == null)
                    continue;
                String[] producers = producersStr.split(",| and | e ");
                for (String producer : producers) {
                    producer = producer.trim();
                    if (producer.isEmpty())
                        continue;
                    // verifica se o produtor já existe no mapa, se não existir, cria uma nova lista
                    listAwards.computeIfAbsent(producer, k -> new ArrayList<>()).add(year);
                }
            }
            List<MovieDTO> intervals = new ArrayList<>();
            // verifica se o produtor tem mais de um prêmio, se não tiver, não adiciona
            for (Map.Entry<String, List<Integer>> entry : listAwards.entrySet()) {
                List<Integer> years = entry.getValue();
                if (years.size() < 2)
                    continue;
                Collections.sort(years);
                for (int j = 1; j < years.size(); j++) {
                    // verifica o intervalo entre os prêmios
                    int interval = years.get(j) - years.get(j - 1);
                    intervals.add(new MovieDTO(entry.getKey(), interval, years.get(j - 1), years.get(j)));
                }
            }

            // Ordenar pelos menores intervalos
            List<MovieDTO> sortedMin = intervals.stream()
                    .sorted(Comparator.comparingInt(MovieDTO::getInterval))
                    .collect(Collectors.toList());

            int secondMin = sortedMin.size() > 1 ? sortedMin.get(1).getInterval() : sortedMin.get(0).getInterval();

            List<MovieDTO> minResults = sortedMin.stream()
                    .filter(dto -> dto.getInterval() <= secondMin)
                    .limit(2)
                    .collect(Collectors.toList());

            // Ordenar pelos maiores intervalos
            List<MovieDTO> sortedMax = intervals.stream()
                    .sorted(Comparator.comparingInt(MovieDTO::getInterval).reversed())
                    .collect(Collectors.toList());

            int secondMax = sortedMax.size() > 1 ? sortedMax.get(1).getInterval() : sortedMax.get(0).getInterval();

            List<MovieDTO> maxResults = sortedMax.stream()
                    .filter(dto -> dto.getInterval() >= secondMax)
                    .limit(2)
                    .collect(Collectors.toList());

            log.info("[Service] - Fim - getIntervals");
            
            return new IntervalDTO(minResults, maxResults);
        } else {
            log.error("[Service] - CSV file is invalid");
            throw new RuntimeException("CSV file is invalid or has been modified.");
        }
    }

}
