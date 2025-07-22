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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TrainingTypeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private TrainingTypeController trainingTypeController;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(trainingTypeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldGetTrainingTypeById() throws Exception {
        TrainingTypeDto response = new TrainingTypeDto();
        response.setId(7L);
        response.setTrainingTypeName("Yoga");

        when(trainingTypeService.findById(7L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/training-types/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.trainingTypeName").value("Yoga"));
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
        t1.setTrainingTypeName("TypeA");
        TrainingTypeDto t2 = new TrainingTypeDto();
        t2.setId(2L);
        t2.setTrainingTypeName("TypeB");

        when(trainingTypeService.findAll()).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/api/v1/training-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}