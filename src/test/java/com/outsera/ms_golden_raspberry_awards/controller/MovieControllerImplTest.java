package com.outsera.ms_golden_raspberry_awards.controller;

import org.junit.jupiter.api.Test;
import com.outsera.ms_golden_raspberry_awards.dto.IntervalDTO;
import com.outsera.ms_golden_raspberry_awards.service.MovieService;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MovieControllerImplTest {

    private final MovieService movieService = Mockito.mock(MovieService.class);
    private final MovieControllerImpl movieController = new MovieControllerImpl(movieService);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();

    @Test
    void testGetIntevalsByFilter() throws Exception {
        IntervalDTO intervalDTO = new IntervalDTO(null, null);
        Mockito.when(movieService.getIntervals()).thenReturn(intervalDTO);

        mockMvc.perform(get("/movie/v1/intervals/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
