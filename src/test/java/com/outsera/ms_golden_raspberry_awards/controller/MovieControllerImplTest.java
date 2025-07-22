package com.outsera.ms_golden_raspberry_awards.controller;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsera.ms_golden_raspberry_awards.dto.IntervalDTO;
import com.outsera.ms_golden_raspberry_awards.service.MovieService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerImplTest {

    private final MovieService movieService = Mockito.mock(MovieService.class);

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetIntevalsByFilter() throws Exception {
        IntervalDTO intervalDTO = new IntervalDTO(null, null);
        Mockito.when(movieService.getIntervals()).thenReturn(intervalDTO);

        mockMvc.perform(get("/movie/v1/intervals/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldMatchApiResponseWithExpectedJson() throws Exception {

        String responseJson = mockMvc.perform(get("/movie/v1/intervals/")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedJson = Files.readString(new ClassPathResource("expected-intervals.json").getFile().toPath());

        IntervalDTO actual = objectMapper.readValue(responseJson, IntervalDTO.class);
        IntervalDTO expected = objectMapper.readValue(expectedJson, IntervalDTO.class);

        String actualNormalized = objectMapper.writeValueAsString(actual);
        String expectedNormalized = objectMapper.writeValueAsString(expected);

        assertTrue(actualNormalized.equals(expectedNormalized),
                () -> "\n A resposta da API não corresponde ao esperado!\n\nEsperado:\n" + expectedNormalized
                        + "\n\nObtido:\n" + actualNormalized);
    }

}
