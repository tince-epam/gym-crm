package com.epam.gymcrm.service;

import com.epam.gymcrm.domain.TrainingType;
import com.epam.gymcrm.dto.TrainingTypeDto;
import com.epam.gymcrm.exception.TrainingTypeNotFoundException;
import com.epam.gymcrm.mapper.TrainingTypeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceTest {

    /*@Mock
    private TrainingTypeDao trainingTypeDao;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    @Test
    void shouldCreateTrainingType() {
        // Arrange
        TrainingTypeDto dto = new TrainingTypeDto();
        dto.setName("Yoga");

        TrainingType type = TrainingTypeMapper.toTrainingType(dto);
        type.setId(1L);

        when(trainingTypeDao.save(any(TrainingType.class))).thenReturn(type);

        // Act
        TrainingTypeDto result = trainingTypeService.create(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Yoga", result.getName());
        verify(trainingTypeDao, times(1)).save(any(TrainingType.class));
    }

    @Test
    void shouldFindTrainingTypeById() {
        // Arrange
        TrainingType type = new TrainingType();
        type.setId(1L);
        type.setTrainingTypeName("Yoga");

        when(trainingTypeDao.findById(1L)).thenReturn(Optional.of(type));

        // Act
        TrainingTypeDto result = trainingTypeService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Yoga", result.getName());
        verify(trainingTypeDao, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTrainingTypeNotFound() {
        // Arrange
        when(trainingTypeDao.findById(100L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TrainingTypeNotFoundException.class, () -> trainingTypeService.findById(100L));
        verify(trainingTypeDao, times(1)).findById(100L);
    }

    @Test
    void shouldReturnAllTrainingTypes() {
        // Arrange
        TrainingType t1 = new TrainingType();
        t1.setId(1L);
        t1.setTrainingTypeName("Yoga");
        TrainingType t2 = new TrainingType();
        t2.setId(2L);
        t2.setTrainingTypeName("Pilates");

        List<TrainingType> types = List.of(t1, t2);
        when(trainingTypeDao.findAll()).thenReturn(types);

        // Act
        List<TrainingTypeDto> result = trainingTypeService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Yoga", result.get(0).getName());
        assertEquals("Pilates", result.get(1).getName());
        verify(trainingTypeDao, times(1)).findAll();
    }

    @Test
    void shouldUpdateTrainingType() {
        // Arrange
        TrainingType existing = new TrainingType();
        existing.setId(1L);
        existing.setTrainingTypeName("Yoga");

        TrainingTypeDto updateDto = new TrainingTypeDto();
        updateDto.setId(1L);
        updateDto.setName("Power Yoga");

        when(trainingTypeDao.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(trainingTypeDao).update(any(TrainingType.class));

        // Act
        trainingTypeService.update(updateDto);

        // Assert
        verify(trainingTypeDao, times(1)).findById(1L);
        verify(trainingTypeDao, times(1)).update(any(TrainingType.class));
        assertEquals("Power Yoga", existing.getTrainingTypeName());
    }

    @Test
    void shouldThrowExceptionWhenUpdateTrainingTypeNotFound() {
        // Arrange
        TrainingTypeDto updateDto = new TrainingTypeDto();
        updateDto.setId(99L);
        updateDto.setName("Crossfit");

        when(trainingTypeDao.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TrainingTypeNotFoundException.class, () -> trainingTypeService.update(updateDto));
        verify(trainingTypeDao, times(1)).findById(99L);
        verify(trainingTypeDao, never()).update(any(TrainingType.class));
    }

    @Test
    void shouldDeleteTrainingType() {
        // Arrange
        TrainingType type = new TrainingType();
        type.setId(1L);
        type.setTrainingTypeName("Yoga");
        when(trainingTypeDao.findById(1L)).thenReturn(Optional.of(type));
        doNothing().when(trainingTypeDao).deleteById(1L);

        // Act
        trainingTypeService.deleteById(1L);

        // Assert
        verify(trainingTypeDao, times(1)).findById(1L);
        verify(trainingTypeDao, times(1)).deleteById(1L);
    }*/
}