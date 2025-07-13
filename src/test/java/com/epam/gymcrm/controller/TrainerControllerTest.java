package com.epam.gymcrm.controller;

import com.epam.gymcrm.dto.TrainerDto;
import com.epam.gymcrm.service.TrainerService;
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
class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainerService trainerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateTrainer() throws Exception {
        TrainerDto request = new TrainerDto();
        request.setFirstName("Ali");
        request.setLastName("Veli");
        request.setSpecialization("Fitness");

        TrainerDto response = new TrainerDto();
        response.setId(10L);
        response.setFirstName("Ali");
        response.setLastName("Veli");
        response.setUsername("Ali.Veli");
        response.setSpecialization("Fitness");

        when(trainerService.createTrainer(any(TrainerDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.username").value("Ali.Veli"));
    }

    @Test
    void shouldGetTrainerById() throws Exception {
        TrainerDto response = new TrainerDto();
        response.setId(5L);
        response.setFirstName("Ayşe");
        response.setLastName("Yılmaz");
        response.setUsername("Ayşe.Yılmaz");

        when(trainerService.findById(5L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/trainers/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.username").value("Ayşe.Yılmaz"));
    }

    @Test
    void shouldGetAllTrainers() throws Exception {
        TrainerDto t1 = new TrainerDto();
        t1.setId(1L);
        t1.setUsername("Trainer.One");
        TrainerDto t2 = new TrainerDto();
        t2.setId(2L);
        t2.setUsername("Trainer.Two");

        when(trainerService.findAll()).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/api/v1/trainers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldUpdateTrainer() throws Exception {
        TrainerDto request = new TrainerDto();
        request.setFirstName("Updated");
        request.setLastName("Trainer");
        request.setSpecialization("Fitness");

        doNothing().when(trainerService).update(any(TrainerDto.class));

        mockMvc.perform(put("/api/v1/trainers/9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteTrainer() throws Exception {
        doNothing().when(trainerService).deleteById(7L);

        mockMvc.perform(delete("/api/v1/trainers/7"))
                .andExpect(status().isNoContent());
    }
}