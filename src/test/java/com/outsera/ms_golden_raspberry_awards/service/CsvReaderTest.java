package com.outsera.ms_golden_raspberry_awards.service;

import org.junit.jupiter.api.Test;
import com.outsera.ms_golden_raspberry_awards.entity.Movie;
import com.outsera.ms_golden_raspberry_awards.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.test.util.ReflectionTestUtils;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CsvReaderTest {

    private MovieRepository movieRepository;
    private CsvReader csvReader;
    private String tempCsvPath = "test_movies.csv";

    @BeforeEach
    void setUp() throws IOException {
        movieRepository = mock(MovieRepository.class);
        csvReader = new CsvReader(movieRepository);
        ReflectionTestUtils.setField(csvReader, "csvFilePath", tempCsvPath);

        // Create a temporary CSV file for testing
        try (FileWriter writer = new FileWriter(tempCsvPath)) {
            writer.write("year;title;studios;producers;winner\n");
            writer.write("1980;Movie A;Studio A;Producer A;yes\n");
            writer.write("1981;Movie B;Studio B;Producer B;\n");
        }
    }

    @Test
    void testReadCsv() {
        List<Movie> movies = csvReader.readCsv();
        assertNotNull(movies);
        assertEquals(2, movies.size());
        assertEquals("Movie A", movies.get(0).getTitle());
        assertEquals("yes", movies.get(0).getWinner());
        assertEquals("Movie B", movies.get(1).getTitle());
        assertEquals("", movies.get(1).getWinner());
    }

    @Test
    void testReadCsvWithMissingWinnerColumn() throws IOException {
        try (FileWriter writer = new FileWriter(tempCsvPath)) {
            writer.write("year;title;studios;producers\n");
            writer.write("1982;Movie C;Studio C;Producer C\n");
        }
        List<Movie> movies = csvReader.readCsv();
        assertEquals(1, movies.size());
        assertEquals("", movies.get(0).getWinner());
    }

    @Test
    void testReadCsvWithInvalidFile() {
        ReflectionTestUtils.setField(csvReader, "csvFilePath", "invalid_path.csv");
        List<Movie> movies = csvReader.readCsv();
        assertTrue(movies.isEmpty());
    }

    @Test
    void testSaveCsvDataWhenRepositoryIsNotEmpty() {
        when(movieRepository.count()).thenReturn(1L);
        csvReader.saveCsvData();
        verify(movieRepository, never()).saveAll(anyList());
    }
}
