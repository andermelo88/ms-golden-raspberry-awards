package com.outsera.ms_golden_raspberry_awards.service;

import org.junit.jupiter.api.Test;

import com.outsera.ms_golden_raspberry_awards.entity.Movie;
import com.outsera.ms_golden_raspberry_awards.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CsvReaderTest {

    private MovieRepository movieRepository;
    private CsvReader csvReader;
    private String tempCsvPath = "src/main/resources/Movielist.csv";

    @BeforeEach
    void setUp() throws IOException {
        movieRepository = mock(MovieRepository.class);
        csvReader = new CsvReader(movieRepository);
        ReflectionTestUtils.setField(csvReader, "csvFilePath", tempCsvPath);

    }

    @Test
    void testReadCsv() {
        List<Movie> movies = csvReader.readCsv();
        assertNotNull(movies);
        assertEquals(206, movies.size());
        assertEquals("Can't Stop the Music", movies.get(0).getTitle());
        assertEquals("yes", movies.get(0).getWinner());
        assertEquals("Cruising", movies.get(1).getTitle());
        assertEquals("", movies.get(1).getWinner());
    }

    @Test
    void testReadCsvWithMissingWinnerColumn() throws IOException {
        List<Movie> movies = csvReader.readCsv();
        assertEquals(206, movies.size());
        assertEquals("", movies.get(17).getWinner());
    }

    @Test
    void testReadCsvWithInvalidFile() {
        ReflectionTestUtils.setField(csvReader, "csvFilePath", "invalid_path.csv");
        List<Movie> movies = csvReader.readCsv();
        assertTrue(movies == null);
    }

    @Test
    void testSaveCsvDataWhenRepositoryIsNotEmpty() {
        when(movieRepository.count()).thenReturn(1L);
        csvReader.saveCsvData();
        verify(movieRepository, never()).saveAll(anyList());
    }

}
