package com.epam.gymcrm.service;

import com.epam.gymcrm.domain.TrainingType;
import com.epam.gymcrm.dto.TrainingTypeDto;
import com.epam.gymcrm.exception.TrainingTypeNotFoundException;
import com.epam.gymcrm.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    TrainingType type1, type2;
    TrainingTypeDto updateDto;

    @BeforeEach
    void setUp() {
        type1 = new TrainingType();
        type1.setId(1L);
        type1.setTrainingTypeName("Yoga");

        type2 = new TrainingType();
        type2.setId(2L);
        type2.setTrainingTypeName("Pilates");

        updateDto = new TrainingTypeDto();
        updateDto.setId(1L);
        updateDto.setTrainingTypeName("Updated Yoga");
    }

    @Test
    void shouldFindTrainingTypeById() {
        when(trainingTypeRepository.findById(1L)).thenReturn(Optional.of(type1));
        TrainingTypeDto result = trainingTypeService.findById(1L);

        assertNotNull(result);
        assertEquals("Yoga", result.getTrainingTypeName());
        verify(trainingTypeRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTrainingTypeNotFound() {
        when(trainingTypeRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(TrainingTypeNotFoundException.class, () -> trainingTypeService.findById(100L));
        verify(trainingTypeRepository).findById(100L);
    }

    @Test
    void shouldReturnAllTrainingTypes() {
        when(trainingTypeRepository.findAll()).thenReturn(List.of(type1, type2));
        List<TrainingTypeDto> result = trainingTypeService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Yoga", result.get(0).getTrainingTypeName());
        assertEquals("Pilates", result.get(1).getTrainingTypeName());
        verify(trainingTypeRepository).findAll();
    }
}