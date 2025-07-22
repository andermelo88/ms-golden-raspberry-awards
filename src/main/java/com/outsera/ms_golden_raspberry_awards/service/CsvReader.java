package com.outsera.ms_golden_raspberry_awards.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.outsera.ms_golden_raspberry_awards.entity.Movie;
import com.outsera.ms_golden_raspberry_awards.repository.MovieRepository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CsvReader {

    @Value("${csv.file.path}")
    private String csvFilePath;
    public static final String DELIMITER = ";";

    MovieRepository movieRepository;

    public CsvReader(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }



    public List<Movie> readCsv() {
        log.info("[Component] - Inicio - operacao readCsv");
        List<Movie> movies = new ArrayList<>();
        boolean skipFirstLine = true;

        log.info("[Component] - filePath: {}", csvFilePath);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (skipFirstLine) {
                    skipFirstLine = false;
                    continue;
                }
                String[] values = line.split(DELIMITER);
                if (values.length >= 4) {
                    Movie movie = new Movie();
                    movie.setYear(Integer.parseInt(values[0]));
                    movie.setTitle(values[1]);
                    movie.setStudios(values[2]);
                    movie.setProducers(values[3]);
                    if (values.length == 5) {
                        movie.setWinner(values[4]);
                    } else {
                        movie.setWinner("");
                    }
                    movies.add(movie);
                    System.out.println(movie.toString());
                }
            }
        } catch (IOException e) {
            log.error("[Component] - Falha ao ler o arquivo CSV: {}", e.getMessage());
            return null;
        }

        log.info("[Component] - Fim - operacao readCsv");
        return movies;
    }

    @PostConstruct
    public void saveCsvData() {

        log.info("[Component] - Inicio - operacao saveCsvData");

        if (movieRepository.count() == 0) {
            List<Movie> movies = readCsv();
            movieRepository.saveAll(movies != null ? movies : new ArrayList<>());
            log.info("[Component] - CSV carregado no banco.");
        } else {
            log.info("[Component] - Banco já possui dados, CSV não carregado novamente.");
        }

        log.info("[Component] - Fim - operacao saveCsvData");
    }

    @PreDestroy
    public void deleteDatabaseFile() {
        File dbFile = new File("./data/moviedb.mv.db");
        if (dbFile.exists()) {
            if (dbFile.delete()) {
                log.info("[Component] - Banco de dados removido ao iniciar a aplicação.");
            } else {
                log.warn("[Component] - Falha ao remover o banco de dados.");
            }
        }
    }
}
