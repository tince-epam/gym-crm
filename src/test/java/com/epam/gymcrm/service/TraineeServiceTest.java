package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.domain.Trainee;
import com.epam.gymcrm.domain.User;
import com.epam.gymcrm.dto.TraineeDto;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.mapper.TraineeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TraineeService traineeService;

    @Test
    void shouldCreateTrainee() {
        // Arrange
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setFirstName("Ali");
        traineeDto.setLastName("Veli");
        traineeDto.setAddress("Istanbul");
        traineeDto.setDateOfBirth("1990-01-01");

        Trainee trainee = TraineeMapper.toTrainee(traineeDto);
        trainee.setId(1L);
        User user = new User();
        user.setFirstName("Ali");
        user.setLastName("Veli");
        user.setUsername("Ali.Veli");
        user.setPassword("password123");
        user.setActive(true);
        trainee.setUser(user);

        when(traineeDao.save(any(Trainee.class))).thenReturn(trainee);

        // Act
        TraineeDto result = traineeService.createTrainee(traineeDto);

        // Assert
        assertNotNull(result);
        assertEquals("Ali", result.getFirstName());
        assertEquals("Veli", result.getLastName());
        assertEquals("Ali.Veli", result.getUsername()); // Username assert’i
        verify(traineeDao, times(1)).save(any(Trainee.class));
    }

    @Test
    void shouldUpdateTrainee() {
        // Arrange
        Trainee existing = new Trainee();
        existing.setId(1L);
        User user = new User();
        user.setFirstName("Ali");
        user.setLastName("Veli");
        user.setUsername("Ali.Veli");
        user.setActive(true);
        existing.setUser(user);
        existing.setAddress("Istanbul");
        existing.setDateOfBirth(LocalDate.parse("1990-01-01"));

        TraineeDto updateDto = new TraineeDto();
        updateDto.setId(1L);
        updateDto.setFirstName("Mehmet");
        updateDto.setLastName("Veli");
        updateDto.setActive(false);
        updateDto.setAddress("Ankara");
        updateDto.setDateOfBirth("1988-12-31");

        when(traineeDao.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(traineeDao).update(any(Trainee.class));

        // Act
        traineeService.update(updateDto);

        // Assert
        verify(traineeDao, times(1)).findById(1L);
        verify(traineeDao, times(1)).update(any(Trainee.class));
    }


    @Test
    void shouldThrowExceptionWhenUpdateTraineeNotFound() {
        // Arrange
        TraineeDto updateDto = new TraineeDto();
        updateDto.setId(99L);

        when(traineeDao.findById(99L)).thenReturn(Optional.empty());

        // Assert & Act
        assertThrows(TraineeNotFoundException.class, () -> traineeService.update(updateDto));
        verify(traineeDao, times(1)).findById(99L);
        verify(traineeDao, times(0)).update(any(Trainee.class));
    }

    @Test
    void shouldFindTraineeById() {
        // Arrange
        Trainee trainee = new Trainee();
        trainee.setId(1L);

        User user = new User();
        user.setFirstName("Ali");
        user.setLastName("Veli");
        user.setUsername("Ali.Veli");
        trainee.setUser(user);

        when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));

        // Act
        TraineeDto result = traineeService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Ali", result.getFirstName());
        assertEquals("Veli", result.getLastName());
        assertEquals("Ali.Veli", result.getUsername());
        verify(traineeDao, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTraineeNotFound() {
        // Arrange
        when(traineeDao.findById(100L)).thenReturn(Optional.empty());

        // Assert & Act
        assertThrows(TraineeNotFoundException.class, () -> traineeService.findById(100L));
        verify(traineeDao, times(1)).findById(100L);
    }

    @Test
    void shouldDeleteTrainee() {
        // Arrange
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        User user = new User();
        user.setFirstName("Ali");
        user.setLastName("Veli");
        trainee.setUser(user);

        when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));
        doNothing().when(traineeDao).deleteById(1L);

        // Act
        traineeService.deleteById(1L);

        // Assert
        verify(traineeDao, times(1)).deleteById(1L);
    }


    @Test
    void shouldReturnAllTrainees() {
        // Arrange
        Trainee t1 = new Trainee();
        t1.setId(1L);
        User user1 = new User();
        user1.setFirstName("Ali");
        user1.setLastName("Veli");
        user1.setUsername("Ali.Veli");
        t1.setUser(user1);

        Trainee t2 = new Trainee();
        t2.setId(2L);
        User user2 = new User();
        user2.setFirstName("Ayşe");
        user2.setLastName("Yılmaz");
        user2.setUsername("Ayşe.Yılmaz");
        t2.setUser(user2);

        List<Trainee> traineeList = List.of(t1, t2);
        when(traineeDao.findAll()).thenReturn(traineeList);

        // Act
        List<TraineeDto> result = traineeService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ali", result.get(0).getFirstName());
        assertEquals("Ayşe", result.get(1).getFirstName());
        assertEquals("Ali.Veli", result.get(0).getUsername());
        assertEquals("Ayşe.Yılmaz", result.get(1).getUsername());
        verify(traineeDao, times(1)).findAll();
    }


}