package com.epam.gymcrm.service;

import com.epam.gymcrm.domain.Trainee;
import com.epam.gymcrm.domain.Trainer;
import com.epam.gymcrm.domain.Training;
import com.epam.gymcrm.domain.TrainingType;
import com.epam.gymcrm.dto.TrainingDto;
import com.epam.gymcrm.exception.*;
import com.epam.gymcrm.mapper.TrainingMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    /*@Mock
    private TrainingDao trainingDao;
    @Mock
    private TrainerDao trainerDao;
    @Mock
    private TraineeDao traineeDao;
    @Mock
    private TrainingTypeDao trainingTypeDao;

    @InjectMocks
    private TrainingService trainingService;


    @Test
    void shouldCreateTraining() {
        TrainingDto dto = new TrainingDto();
        dto.setTrainerId(1L);
        dto.setTraineeId(2L);
        dto.setTrainingTypeId(3L);
        dto.setTrainingName("Cardio");
        dto.setTrainingDate(LocalDateTime.of(2025, 7, 14, 10, 0));
        dto.setTrainingDuration(60);

        Trainer trainer = new Trainer(); trainer.setId(1L);
        Trainee trainee = new Trainee(); trainee.setId(2L);
        TrainingType trainingType = new TrainingType(); trainingType.setId(3L);

        when(trainerDao.findById(1L)).thenReturn(Optional.of(trainer));
        when(traineeDao.findById(2L)).thenReturn(Optional.of(trainee));
        when(trainingTypeDao.findById(3L)).thenReturn(Optional.of(trainingType));
        when(trainingDao.findAll()).thenReturn(Collections.emptyList());

        Training training = TrainingMapper.toTraining(dto);
        training.setId(1L);
        when(trainingDao.save(any(Training.class))).thenReturn(training);

        TrainingDto result = trainingService.createTraining(dto);

        assertNotNull(result);
        assertEquals("Cardio", result.getTrainingName());
        verify(trainingDao, times(1)).save(any(Training.class));
    }

    @Test
    void shouldThrowIfTrainerNotFound() {
        TrainingDto dto = new TrainingDto();
        dto.setTrainerId(1L);
        when(trainerDao.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(TrainerNotFoundException.class, () -> trainingService.createTraining(dto));
        assertTrue(ex.getMessage().contains("Trainer not found"));
    }

    @Test
    void shouldThrowIfTraineeNotFoundWhenCreate() {
        TrainingDto dto = new TrainingDto();
        dto.setTrainerId(1L);
        dto.setTraineeId(2L);
        dto.setTrainingTypeId(3L);

        Trainer trainer = new Trainer(); trainer.setId(1L);
        when(trainerDao.findById(1L)).thenReturn(Optional.of(trainer));
        when(traineeDao.findById(2L)).thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> trainingService.createTraining(dto));
    }

    @Test
    void shouldThrowIfTrainingTypeNotFoundWhenCreate() {
        TrainingDto dto = new TrainingDto();
        dto.setTrainerId(1L);
        dto.setTraineeId(2L);
        dto.setTrainingTypeId(3L);

        Trainer trainer = new Trainer(); trainer.setId(1L);
        Trainee trainee = new Trainee(); trainee.setId(2L);

        when(trainerDao.findById(1L)).thenReturn(Optional.of(trainer));
        when(traineeDao.findById(2L)).thenReturn(Optional.of(trainee));
        when(trainingTypeDao.findById(3L)).thenReturn(Optional.empty());

        assertThrows(TrainingTypeNotFoundException.class, () -> trainingService.createTraining(dto));
    }

    @Test
    void shouldThrowIfTrainerScheduleConflict() {
        TrainingDto dto = new TrainingDto();
        dto.setTrainerId(1L);
        dto.setTraineeId(2L);
        dto.setTrainingTypeId(3L);
        dto.setTrainingName("Cardio");
        LocalDateTime trainingDate = LocalDateTime.of(2025, 7, 14, 10, 0);
        dto.setTrainingDate(trainingDate);

        Trainer trainer = new Trainer(); trainer.setId(1L);
        Trainee trainee = new Trainee(); trainee.setId(2L);
        TrainingType trainingType = new TrainingType(); trainingType.setId(3L);

        when(trainerDao.findById(1L)).thenReturn(Optional.of(trainer));
        when(traineeDao.findById(2L)).thenReturn(Optional.of(trainee));
        when(trainingTypeDao.findById(3L)).thenReturn(Optional.of(trainingType));

        Training existing = new Training();
        existing.setId(1L);
//        existing.setTrainerId(1L);
        existing.setTrainingDate(trainingDate);

        when(trainingDao.findAll()).thenReturn(List.of(existing));

        assertThrows(TrainerScheduleConflictException.class, () -> trainingService.createTraining(dto));
    }

    @Test
    void shouldFindById() {
        Training training = new Training();
        training.setId(1L);
        training.setTrainingName("Strength");
//        training.setTrainerId(1L);
//        training.setTraineeId(2L);
//        training.setTrainingTypeId(3L);
        training.setTrainingDate(LocalDateTime.of(2025, 7, 14, 10, 0));
        training.setTrainingDuration(60);

        when(trainingDao.findById(1L)).thenReturn(Optional.of(training));

        TrainingDto dto = trainingService.findById(1L);

        assertNotNull(dto);
        assertEquals("Strength", dto.getTrainingName());
    }

    @Test
    void shouldThrowIfTrainingNotFoundById() {
        when(trainingDao.findById(77L)).thenReturn(Optional.empty());
        assertThrows(TrainingNotFoundException.class, () -> trainingService.findById(77L));
    }

    @Test
    void shouldFindAllTrainings() {
        Training training1 = new Training(); training1.setId(1L);
        Training training2 = new Training(); training2.setId(2L);
        when(trainingDao.findAll()).thenReturn(List.of(training1, training2));

        List<TrainingDto> list = trainingService.findAll();
        assertEquals(2, list.size());
    }

    @Test
    void shouldDeleteTraining() {
        Training training = new Training();
        training.setId(1L);
        when(trainingDao.findById(1L)).thenReturn(Optional.of(training));
        doNothing().when(trainingDao).deleteById(1L);

        trainingService.deleteById(1L);

        verify(trainingDao, times(1)).findById(1L);
        verify(trainingDao, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeleteByIdIfTrainingNotFound() {
        when(trainingDao.findById(99L)).thenReturn(Optional.empty());
        assertThrows(TrainingNotFoundException.class, () -> trainingService.deleteById(99L));
    }

    @Test
    void shouldUpdateTraining() {
        TrainingDto dto = new TrainingDto();
        dto.setId(1L);
        dto.setTrainingName("Updated Training");
        dto.setTrainerId(1L);
        dto.setTraineeId(2L);
        dto.setTrainingTypeId(3L);
        dto.setTrainingDate(LocalDateTime.of(2025, 7, 15, 10, 0));
        dto.setTrainingDuration(80);

        Training existing = new Training();
        existing.setId(1L);
        existing.setTrainingName("Old Name");

        Trainer trainer = new Trainer(); trainer.setId(1L);
        Trainee trainee = new Trainee(); trainee.setId(2L);
        TrainingType trainingType = new TrainingType(); trainingType.setId(3L);

        when(trainingDao.findById(1L)).thenReturn(Optional.of(existing));
        when(trainerDao.findById(1L)).thenReturn(Optional.of(trainer));
        when(traineeDao.findById(2L)).thenReturn(Optional.of(trainee));
        when(trainingTypeDao.findById(3L)).thenReturn(Optional.of(trainingType));
        when(trainingDao.findAll()).thenReturn(List.of(existing));
        doNothing().when(trainingDao).update(any(Training.class));

        trainingService.update(dto);

        assertEquals("Updated Training", existing.getTrainingName());
        verify(trainingDao, times(1)).update(any(Training.class));
    }

    @Test
    void shouldThrowIfTrainingNotFoundWhenUpdate() {
        TrainingDto dto = new TrainingDto();
        dto.setId(111L);
        when(trainingDao.findById(111L)).thenReturn(Optional.empty());

        assertThrows(TrainingNotFoundException.class, () -> trainingService.update(dto));
    }

    @Test
    void shouldThrowIfTrainerNotFoundDuringUpdate() {
        TrainingDto dto = new TrainingDto();
        dto.setId(1L);
        dto.setTrainerId(5L);

        Training existing = new Training(); existing.setId(1L);
        when(trainingDao.findById(1L)).thenReturn(Optional.of(existing));
        when(trainerDao.findById(5L)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainingService.update(dto));
    }

    @Test
    void shouldThrowIfTraineeNotFoundDuringUpdate() {
        TrainingDto dto = new TrainingDto();
        dto.setId(1L);
        dto.setTraineeId(22L);

        Training existing = new Training(); existing.setId(1L);
        when(trainingDao.findById(1L)).thenReturn(Optional.of(existing));
        when(traineeDao.findById(22L)).thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> trainingService.update(dto));
    }

    @Test
    void shouldThrowIfTrainingTypeNotFoundDuringUpdate() {
        TrainingDto dto = new TrainingDto();
        dto.setId(1L);
        dto.setTrainingTypeId(33L);

        Training existing = new Training(); existing.setId(1L);
        when(trainingDao.findById(1L)).thenReturn(Optional.of(existing));
        when(trainingTypeDao.findById(33L)).thenReturn(Optional.empty());

        assertThrows(TrainingTypeNotFoundException.class, () -> trainingService.update(dto));
    }

    @Test
    void shouldThrowIfTrainerScheduleConflictDuringUpdate() {
        TrainingDto dto = new TrainingDto();
        dto.setId(2L);
        dto.setTrainerId(1L);
        dto.setTrainingDate(LocalDateTime.of(2025, 7, 15, 10, 0));

        Training existing = new Training(); existing.setId(2L);

        Training other = new Training();
        other.setId(3L);
//        other.setTrainerId(1L);
        other.setTrainingDate(dto.getTrainingDate());

        Trainer trainer = new Trainer(); trainer.setId(1L);

        when(trainingDao.findById(2L)).thenReturn(Optional.of(existing));
        when(trainerDao.findById(1L)).thenReturn(Optional.of(trainer));
        when(trainingDao.findAll()).thenReturn(List.of(existing, other));

        assertThrows(TrainerScheduleConflictException.class, () -> trainingService.update(dto));
    }*/
}