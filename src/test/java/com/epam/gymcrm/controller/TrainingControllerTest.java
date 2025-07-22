package com.epam.gymcrm.controller;

import com.epam.gymcrm.dto.TrainingDto;
import com.epam.gymcrm.exception.GlobalExceptionHandler;
import com.epam.gymcrm.exception.TrainingNotFoundException;
import com.epam.gymcrm.service.TrainingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController trainingController;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(trainingController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldCreateTraining() throws Exception {
        TrainingDto request = new TrainingDto();
        request.setTrainingName("Cardio");
        request.setTrainerId(1L);
        request.setTraineeId(2L);
        request.setTrainingTypeId(3L);
        request.setTrainingDate(LocalDateTime.of(2025, 7, 20, 14, 0));
        request.setTrainingDuration(60);

        TrainingDto response = new TrainingDto();
        response.setId(10L);
        response.setTrainingName("Cardio");

        when(trainingService.createTraining(any(TrainingDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.trainingName").value("Cardio"));
    }

    @Test
    void shouldGetTrainingById() throws Exception {
        TrainingDto response = new TrainingDto();
        response.setId(5L);
        response.setTrainingName("Strength");

        when(trainingService.findById(5L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/trainings/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.trainingName").value("Strength"));
    }

    @Test
    void shouldReturnNotFoundWhenTrainingNotFound() throws Exception {
        Long notFoundId = 123L;
        when(trainingService.findById(notFoundId))
                .thenThrow(new TrainingNotFoundException("Training not found with id: " + notFoundId));

        mockMvc.perform(get("/api/v1/trainings/" + notFoundId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Training Not Found"))
                .andExpect(jsonPath("$.message").value("Training not found with id: " + notFoundId));
    }


    @Test
    void shouldGetAllTrainings() throws Exception {
        TrainingDto t1 = new TrainingDto();
        t1.setId(1L);
        t1.setTrainingName("A");
        TrainingDto t2 = new TrainingDto();
        t2.setId(2L);
        t2.setTrainingName("B");

        when(trainingService.findAll()).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/api/v1/trainings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldUpdateTraining() throws Exception {
        TrainingDto request = new TrainingDto();
        request.setTrainingName("Updated Training");
        request.setTrainerId(2L);
        request.setTraineeId(3L);
        request.setTrainingTypeId(1L);
        request.setTrainingDate(LocalDateTime.of(2025, 7, 15, 13, 30));
        request.setTrainingDuration(60);

        doNothing().when(trainingService).update(any(TrainingDto.class));

        mockMvc.perform(put("/api/v1/trainings/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteTraining() throws Exception {
        doNothing().when(trainingService).deleteById(3L);

        mockMvc.perform(delete("/api/v1/trainings/3"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
        TrainingDto invalidRequest = new TrainingDto();

        mockMvc.perform(post("/api/v1/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation Error"))
                .andExpect(jsonPath("$.details").isArray());
    }

}