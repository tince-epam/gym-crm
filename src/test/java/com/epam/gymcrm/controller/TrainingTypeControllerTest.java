package com.epam.gymcrm.controller;

import com.epam.gymcrm.dto.TrainingTypeDto;
import com.epam.gymcrm.exception.GlobalExceptionHandler;
import com.epam.gymcrm.exception.TrainingTypeNotFoundException;
import com.epam.gymcrm.service.TrainingTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
class TrainingTypeControllerTest {

    /*private MockMvc mockMvc;

    @Mock
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private TrainingTypeController trainingTypeController;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    ;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(trainingTypeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

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
    void shouldReturnNotFoundWhenTrainingTypeNotFound() throws Exception {
        Long notFoundId = 111L;
        when(trainingTypeService.findById(notFoundId))
                .thenThrow(new TrainingTypeNotFoundException("Training Type not found with id: " + notFoundId));

        mockMvc.perform(get("/api/v1/training-types/" + notFoundId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Training Type Not Found"))
                .andExpect(jsonPath("$.message").value("Training Type not found with id: " + notFoundId));
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

    @Test
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
        TrainingTypeDto invalidRequest = new TrainingTypeDto();
        mockMvc.perform(post("/api/v1/training-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation Error"))
                .andExpect(jsonPath("$.details").isArray());
    }*/
}