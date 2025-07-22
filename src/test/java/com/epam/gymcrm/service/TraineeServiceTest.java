package com.epam.gymcrm.service;

import com.epam.gymcrm.domain.Trainee;
import com.epam.gymcrm.domain.User;
import com.epam.gymcrm.dto.TraineeDto;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.repository.TraineeRepository;
import com.epam.gymcrm.repository.TrainerRepository;
import com.epam.gymcrm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TraineeService traineeService;

    private TraineeDto traineeDto;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        traineeDto = new TraineeDto();
        traineeDto.setId(1L);
        traineeDto.setFirstName("Ali");
        traineeDto.setLastName("Veli");
        traineeDto.setAddress("Istanbul");
        traineeDto.setDateOfBirth("1990-01-01");

        User user = new User();
        user.setId(10L);
        user.setFirstName("Ali");
        user.setLastName("Veli");
        user.setUsername("ali.veli");
        user.setActive(true);

        trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUser(user);
        trainee.setAddress("Istanbul");
        trainee.setDateOfBirth(LocalDate.parse("1990-01-01"));
    }

    @Test
    void shouldCreateTrainee() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(traineeRepository.save(any(Trainee.class))).thenAnswer(invocation -> {
            Trainee t = invocation.getArgument(0);
            t.setId(99L);
            t.getUser().setId(100L);
            return t;
        });

        TraineeDto result = traineeService.createTrainee(traineeDto);

        assertNotNull(result);
        assertEquals("Ali", result.getFirstName());
        assertEquals("Veli", result.getLastName());
        assertEquals("Istanbul", result.getAddress());
        assertNotNull(result.getUsername());
        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    void shouldUpdateTrainee() {
        when(traineeRepository.findByIdWithTrainers(1L)).thenReturn(Optional.of(trainee));
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        traineeDto.setFirstName("Mehmet");
        traineeDto.setAddress("Ankara");
        traineeDto.setDateOfBirth("1988-12-31");

        traineeService.update(traineeDto);

        assertEquals("Mehmet", trainee.getUser().getFirstName());
        assertEquals("Ankara", trainee.getAddress());
        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdateTraineeNotFound() {
        when(traineeRepository.findByIdWithTrainers(anyLong())).thenReturn(Optional.empty());
        assertThrows(TraineeNotFoundException.class, () -> traineeService.update(traineeDto));
        verify(traineeRepository).findByIdWithTrainers(anyLong());
    }

    @Test
    void shouldFindTraineeById() {
        when(traineeRepository.findByIdWithTrainers(1L)).thenReturn(Optional.of(trainee));

        TraineeDto result = traineeService.findById(1L);

        assertNotNull(result);
        assertEquals("Ali", result.getFirstName());
        verify(traineeRepository).findByIdWithTrainers(1L);
    }

    @Test
    void shouldThrowExceptionWhenTraineeNotFound() {
        when(traineeRepository.findByIdWithTrainers(anyLong())).thenReturn(Optional.empty());
        assertThrows(TraineeNotFoundException.class, () -> traineeService.findById(100L));
        verify(traineeRepository).findByIdWithTrainers(100L);
    }

    @Test
    void shouldDeleteTrainee() {
        when(traineeRepository.findByIdWithTrainers(1L)).thenReturn(Optional.of(trainee));
        traineeService.deleteById(1L);
        verify(traineeRepository).delete(trainee);
    }

    @Test
    void shouldReturnAllTrainees() {
        Trainee t1 = trainee;
        Trainee t2 = new Trainee();
        User u2 = new User();
        u2.setFirstName("Ayşe");
        u2.setLastName("Yılmaz");
        u2.setUsername("ayse.yilmaz");
        t2.setId(2L);
        t2.setUser(u2);

        when(traineeRepository.findAllWithTrainers()).thenReturn(List.of(t1, t2));

        List<TraineeDto> result = traineeService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ali", result.get(0).getFirstName());
        assertEquals("Ayşe", result.get(1).getFirstName());
        verify(traineeRepository).findAllWithTrainers();
    }
}