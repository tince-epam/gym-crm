package com.epam.gymcrm.controller;

import com.epam.gymcrm.dto.TraineeDto;
import com.epam.gymcrm.exception.GlobalExceptionHandler;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.service.TraineeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
//@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
class TraineeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private TraineeController traineeController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(traineeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldCreateTrainee() throws Exception {
        TraineeDto request = new TraineeDto();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setDateOfBirth("1990-01-01");
        request.setAddress("Some Address");

        TraineeDto response = new TraineeDto();
        response.setId(1L);
        response.setFirstName("John");
        response.setLastName("Doe");
        response.setUsername("John.Doe");
        response.setDateOfBirth("1990-01-01");
        response.setAddress("Some Address");

        Mockito.when(traineeService.createTrainee(Mockito.any(TraineeDto.class))).thenReturn(response);

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
    void shouldReturnNotFoundWhenTraineeNotFound() throws Exception {
        Long notFoundId = 999L;
        when(traineeService.findById(notFoundId))
                .thenThrow(new TraineeNotFoundException("Trainee not found with id: " + notFoundId));

        mockMvc.perform(get("/api/v1/trainees/" + notFoundId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Trainee Not Found"))
                .andExpect(jsonPath("$.message").value("Trainee not found with id: " + notFoundId));
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
        request.setDateOfBirth("1990-01-01");
        request.setAddress("Some Address");

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
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
        TraineeDto invalidRequest = new TraineeDto();
        invalidRequest.setFirstName("John");

        mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation Error"))
                .andExpect(jsonPath("$.details").isArray());
    }
}