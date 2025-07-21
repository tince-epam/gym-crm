package com.epam.gymcrm.service;

import com.epam.gymcrm.domain.Trainer;
import com.epam.gymcrm.domain.User;
import com.epam.gymcrm.dto.TrainerDto;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.mapper.TrainerMapper;
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
class TrainerServiceTest {

    /*@Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void shouldCreateTrainer() {
        // Arrange
        TrainerDto dto = new TrainerDto();
        dto.setFirstName("Mehmet");
        dto.setLastName("Yılmaz");
        dto.setSpecialization("Fitness");

        Trainer trainer = TrainerMapper.toTrainer(dto);
        trainer.setId(1L);
        User user = new User();
        user.setFirstName("Mehmet");
        user.setLastName("Yılmaz");
        user.setUsername("Mehmet.Yılmaz");
        user.setPassword("password123");
        user.setActive(true);
        trainer.setUser(user);

        when(trainerDao.save(any(Trainer.class))).thenReturn(trainer);

        // Act
        TrainerDto result = trainerService.createTrainer(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Mehmet", result.getFirstName());
        assertEquals("Yılmaz", result.getLastName());
        verify(trainerDao, times(1)).save(any(Trainer.class));
    }


    @Test
    void shouldFindTrainerById() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setId(1L);
        User user = new User();
        user.setFirstName("Mehmet");
        user.setLastName("Yılmaz");
        user.setUsername("Mehmet.Yılmaz");
        trainer.setUser(user);

        when(trainerDao.findById(1L)).thenReturn(Optional.of(trainer));

        // Act
        TrainerDto result = trainerService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Mehmet", result.getFirstName());
        assertEquals("Yılmaz", result.getLastName());
        assertEquals("Mehmet.Yılmaz", result.getUsername());
        verify(trainerDao, times(1)).findById(1L);
    }


    @Test
    void shouldThrowExceptionWhenTrainerNotFound() {
        // Arrange
        when(trainerDao.findById(100L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TrainerNotFoundException.class, () -> trainerService.findById(100L));
        verify(trainerDao, times(1)).findById(100L);
    }

    @Test
    void shouldDeleteTrainer() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setId(1L);
        User user = new User();
        user.setFirstName("Mehmet");
        trainer.setUser(user);

        when(trainerDao.findById(1L)).thenReturn(Optional.of(trainer));
        doNothing().when(trainerDao).deleteById(1L);

        // Act
        trainerService.deleteById(1L);

        // Assert
        verify(trainerDao, times(1)).deleteById(1L);
    }


    @Test
    void shouldUpdateTrainer() {
        // Arrange
        Trainer existing = new Trainer();
        existing.setId(1L);
        User user = new User();
        user.setFirstName("Mehmet");
        user.setLastName("Yılmaz");
        user.setUsername("Mehmet.Yılmaz");
        user.setActive(true);
        existing.setUser(user);
        existing.setSpecialization("Fitness");

        TrainerDto updateDto = new TrainerDto();
        updateDto.setId(1L);
        updateDto.setFirstName("Mehmetcan");
        updateDto.setLastName("Yılmaz");
        updateDto.setSpecialization("Pilates");
        updateDto.setActive(false);

        when(trainerDao.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(trainerDao).update(any(Trainer.class));

        // Act
        trainerService.update(updateDto);

        // Assert
        verify(trainerDao, times(1)).findById(1L);
        verify(trainerDao, times(1)).update(any(Trainer.class));
    }


    @Test
    void shouldThrowExceptionWhenUpdateTrainerNotFound() {
        // Arrange
        TrainerDto updateDto = new TrainerDto();
        updateDto.setId(99L);

        when(trainerDao.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TrainerNotFoundException.class, () -> trainerService.update(updateDto));
        verify(trainerDao, times(1)).findById(99L);
        verify(trainerDao, never()).update(any(Trainer.class));
    }

    @Test
    void shouldReturnAllTrainers() {
        // Arrange
        Trainer t1 = new Trainer();
        t1.setId(1L);
        User user1 = new User();
        user1.setFirstName("Mehmet");
        user1.setLastName("Yılmaz");
        user1.setUsername("Mehmet.Yılmaz");
        t1.setUser(user1);

        Trainer t2 = new Trainer();
        t2.setId(2L);
        User user2 = new User();
        user2.setFirstName("Ayşe");
        user2.setLastName("Kaya");
        user2.setUsername("Ayşe.Kaya");
        t2.setUser(user2);

        List<Trainer> trainers = List.of(t1, t2);
        when(trainerDao.findAll()).thenReturn(trainers);

        // Act
        List<TrainerDto> result = trainerService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Mehmet", result.get(0).getFirstName());
        assertEquals("Ayşe", result.get(1).getFirstName());
        assertEquals("Mehmet.Yılmaz", result.get(0).getUsername());
        assertEquals("Ayşe.Kaya", result.get(1).getUsername());
        verify(trainerDao, times(1)).findAll();
    }
*/
}