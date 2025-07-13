package com.epam.gymcrm.controller;

import com.epam.gymcrm.dto.TrainingTypeDto;
import com.epam.gymcrm.service.TrainingTypeService;
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
class TrainingTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingTypeService trainingTypeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateTrainingType() throws Exception {
        TrainingTypeDto request = new TrainingTypeDto();
        request.setName("Crossfit");

        TrainingTypeDto response = new TrainingTypeDto();
        response.setId(12L);
        response.setName("Crossfit");

        when(trainingTypeService.create(any(TrainingTypeDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/training-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(12))
                .andExpect(jsonPath("$.name").value("Crossfit"));
    }

    @Test
    void shouldGetTrainingTypeById() throws Exception {
        TrainingTypeDto response = new TrainingTypeDto();
        response.setId(7L);
        response.setName("Yoga");

        when(trainingTypeService.findById(7L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/training-types/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Yoga"));
    }

    @Test
    void shouldGetAllTrainingTypes() throws Exception {
        TrainingTypeDto t1 = new TrainingTypeDto();
        t1.setId(1L);
        t1.setName("TypeA");
        TrainingTypeDto t2 = new TrainingTypeDto();
        t2.setId(2L);
        t2.setName("TypeB");

        when(trainingTypeService.findAll()).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/api/v1/training-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldUpdateTrainingType() throws Exception {
        TrainingTypeDto request = new TrainingTypeDto();
        request.setName("Updated Name");

        doNothing().when(trainingTypeService).update(any(TrainingTypeDto.class));

        mockMvc.perform(put("/api/v1/training-types/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteTrainingType() throws Exception {

        doNothing().when(trainingTypeService).deleteById(13L);

        mockMvc.perform(delete("/api/v1/training-types/13"))
                .andExpect(status().isNoContent());
    }
}