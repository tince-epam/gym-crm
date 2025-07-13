package com.epam.gymcrm.controller;

import com.epam.gymcrm.dto.TraineeDto;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.service.TraineeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
class TraineeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TraineeService traineeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateTrainee() throws Exception {
        TraineeDto request = new TraineeDto();
        request.setFirstName("John");
        request.setLastName("Doe");

        TraineeDto response = new TraineeDto();
        response.setId(1L);
        response.setFirstName("John");
        response.setLastName("Doe");
        response.setUsername("John.Doe");

        when(traineeService.createTrainee(any(TraineeDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("John.Doe"));
    }

    @Test
    void shouldGetTraineeById() throws Exception {
        TraineeDto response = new TraineeDto();
        response.setId(2L);
        response.setFirstName("Jane");
        response.setLastName("Smith");
        response.setUsername("Jane.Smith");

        when(traineeService.findById(2L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/trainees/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.username").value("Jane.Smith"));
    }

    @Test
    void shouldGetAllTrainees() throws Exception {
        TraineeDto t1 = new TraineeDto();
        t1.setId(1L); t1.setUsername("John.Doe");
        TraineeDto t2 = new TraineeDto();
        t2.setId(2L); t2.setUsername("Jane.Smith");

        when(traineeService.findAll()).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/api/v1/trainees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldUpdateTrainee() throws Exception {
        TraineeDto request = new TraineeDto();
        request.setFirstName("Updated");
        request.setLastName("User");

        doNothing().when(traineeService).update(any(TraineeDto.class));

        mockMvc.perform(put("/api/v1/trainees/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteTrainee() throws Exception {
        doNothing().when(traineeService).deleteById(3L);

        mockMvc.perform(delete("/api/v1/trainees/3"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundAndErrorResponseWhenTraineeNotFound() throws Exception {
        Long notFoundId = 999L;
        when(traineeService.findById(notFoundId)).thenThrow(new TraineeNotFoundException("Trainee not found with id: " + notFoundId));

        mockMvc.perform(get("/api/v1/trainees/" + notFoundId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Trainee not found with id: " + notFoundId));
    }
}