package com.outsera.ms_golden_raspberry_awards.repository;

import org.junit.jupiter.api.Test;
import com.outsera.ms_golden_raspberry_awards.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void testFindByWinner() {
        Movie movie1 = new Movie();
        movie1.setTitle("Movie 1");
        movie1.setWinner("yes");
        movieRepository.save(movie1);

        Movie movie2 = new Movie();
        movie2.setTitle("Movie 2");
        movie2.setWinner("no");
        movieRepository.save(movie2);

        List<Movie> winners = movieRepository.findByWinner("yes");
        assertThat(winners).hasSize(1);
        assertThat(winners.get(0).getTitle()).isEqualTo("Movie 1");
    }

    @Test
    void testFindByWinnerReturnsEmptyList() {
        List<Movie> winners = movieRepository.findByWinner("unknown");
        assertThat(winners).isEmpty();
    }

    @Test
    void testFindByWinnerMultipleResults() {
        Movie movie1 = new Movie();
        movie1.setTitle("Movie 1");
        movie1.setWinner("yes");
        movieRepository.save(movie1);

        Movie movie2 = new Movie();
        movie2.setTitle("Movie 2");
        movie2.setWinner("yes");
        movieRepository.save(movie2);

        List<Movie> winners = movieRepository.findByWinner("yes");
        assertThat(winners).hasSize(2);
        assertThat(winners).extracting(Movie::getTitle).containsExactlyInAnyOrder("Movie 1", "Movie 2");
    }
}
