package com.epam.gymcrm.controller;

import com.epam.gymcrm.dto.TrainerDto;
import com.epam.gymcrm.exception.GlobalExceptionHandler;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.service.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController trainerController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Example test credentials for headers
    private static final String USERNAME = "test.user";
    private static final String PASSWORD = "test.pass";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(trainerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldCreateTrainer() throws Exception {
        TrainerDto request = new TrainerDto();
        request.setFirstName("Jack");
        request.setLastName("Smith");
        request.setSpecialization("Yoga");

        TrainerDto response = new TrainerDto();
        response.setId(10L);
        response.setFirstName("Jack");
        response.setLastName("Smith");
        response.setSpecialization("Yoga");

        when(trainerService.createTrainer(any(TrainerDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.firstName").value("Jack"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.specialization").value("Yoga"));
    }

    @Test
    void shouldGetTrainerById() throws Exception {
        TrainerDto response = new TrainerDto();
        response.setId(20L);
        response.setFirstName("Emma");
        response.setLastName("Stone");
        response.setSpecialization("Pilates");

        when(trainerService.findById(20L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/trainers/20")
                        .header("X-Username", USERNAME)
                        .header("X-Password", PASSWORD))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(20))
                .andExpect(jsonPath("$.firstName").value("Emma"))
                .andExpect(jsonPath("$.specialization").value("Pilates"));
    }

    @Test
    void shouldReturnNotFoundWhenTrainerNotFound() throws Exception {
        Long notFoundId = 999L;
        when(trainerService.findById(notFoundId))
                .thenThrow(new TrainerNotFoundException("Trainer not found with id: " + notFoundId));

        mockMvc.perform(get("/api/v1/trainers/" + notFoundId)
                        .header("X-Username", USERNAME)
                        .header("X-Password", PASSWORD))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Trainer Not Found"))
                .andExpect(jsonPath("$.message").value("Trainer not found with id: " + notFoundId));
    }

    @Test
    void shouldGetAllTrainers() throws Exception {
        TrainerDto t1 = new TrainerDto();
        t1.setId(1L);
        t1.setFirstName("Jack");
        TrainerDto t2 = new TrainerDto();
        t2.setId(2L);
        t2.setFirstName("Emma");

        when(trainerService.findAll()).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/api/v1/trainers")
                        .header("X-Username", USERNAME)
                        .header("X-Password", PASSWORD))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldUpdateTrainer() throws Exception {
        TrainerDto request = new TrainerDto();
        request.setFirstName("Updated");
        request.setLastName("Trainer");
        request.setSpecialization("Strength");

        doNothing().when(trainerService).update(any(TrainerDto.class));

        mockMvc.perform(put("/api/v1/trainers/5")
                        .header("X-Username", USERNAME)
                        .header("X-Password", PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteTrainer() throws Exception {
        doNothing().when(trainerService).deleteById(7L);

        mockMvc.perform(delete("/api/v1/trainers/7")
                        .header("X-Username", USERNAME)
                        .header("X-Password", PASSWORD))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
        TrainerDto invalidRequest = new TrainerDto();

        mockMvc.perform(post("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation Error"))
                .andExpect(jsonPath("$.details").isArray());
    }
}