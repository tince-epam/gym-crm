package com.epam.gymcrm.service;

import com.epam.gymcrm.domain.Trainee;
import com.epam.gymcrm.domain.User;
import com.epam.gymcrm.dto.TraineeDto;
import com.epam.gymcrm.exception.InvalidCredentialsException;
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
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

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

    @Test
    void shouldAuthenticateTraineeWithValidCredentials() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("secret");
        Trainee trainee = new Trainee();
        trainee.setUser(user);
        when(traineeRepository.findByUserUsername("testuser")).thenReturn(Optional.of(trainee));

        assertTrue(traineeService.isTraineeCredentialsValid("testuser", "secret"));
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWithWrongPassword() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("secret");
        Trainee trainee = new Trainee();
        trainee.setUser(user);
        when(traineeRepository.findByUserUsername("testuser")).thenReturn(Optional.of(trainee));

        assertThrows(InvalidCredentialsException.class, () ->
                traineeService.isTraineeCredentialsValid("testuser", "wrongpw"));
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenUserNotFound() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("correct_pw");
        Trainee trainee = new Trainee();
        trainee.setUser(user);

        when(traineeRepository.findByUserUsername("user1")).thenReturn(Optional.of(trainee));

        assertThrows(InvalidCredentialsException.class, () ->
                traineeService.isTraineeCredentialsValid("user1", "wrong_pw"));
    }

    @Test
    void shouldReturnTraineeDtoWhenUsernameExists() {
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        User user = new User();
        user.setUsername("jane.smith");
        trainee.setUser(user);

        when(traineeRepository.findByUserUsername("jane.smith")).thenReturn(Optional.of(trainee));

        TraineeDto result = traineeService.findByUsername("jane.smith");

        assertNotNull(result);
        assertEquals("jane.smith", result.getUsername());
        verify(traineeRepository).findByUserUsername("jane.smith");
    }

    @Test
    void shouldThrowTraineeNotFoundExceptionWhenUsernameNotExists() {
        when(traineeRepository.findByUserUsername("nouser")).thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> traineeService.findByUsername("nouser"));
        verify(traineeRepository).findByUserUsername("nouser");
    }

    @Test
    void shouldChangePasswordWhenOldPasswordMatches() {
        User user = new User();
        user.setUsername("test.user");
        user.setPassword("oldPass");
        Trainee trainee = new Trainee();
        trainee.setUser(user);

        when(traineeRepository.findByUserUsername("test.user"))
                .thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        traineeService.changeTraineePassword("test.user", "oldPass", "newPass");

        assertEquals("newPass", trainee.getUser().getPassword());
        verify(traineeRepository).save(trainee);
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenOldPasswordDoesNotMatch() {
        User user = new User();
        user.setUsername("test.user");
        user.setPassword("oldPass");
        Trainee trainee = new Trainee();
        trainee.setUser(user);

        when(traineeRepository.findByUserUsername("test.user"))
                .thenReturn(Optional.of(trainee));

        assertThrows(InvalidCredentialsException.class, () ->
                traineeService.changeTraineePassword("test.user", "wrongOldPass", "newPass")
        );
        verify(traineeRepository, never()).save(any());
    }

    @Test
    void shouldThrowTraineeNotFoundExceptionWhenUserNotFound() {
        when(traineeRepository.findByUserUsername("nouser"))
                .thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () ->
                traineeService.changeTraineePassword("nouser", "anyPass", "newPass")
        );
        verify(traineeRepository, never()).save(any());
    }

    @Test
    void shouldActivateTraineeWhenInactive() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setActive(false);
        trainee.setUser(user);

        when(traineeRepository.findById(1L)).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenAnswer(inv -> inv.getArgument(0));

        traineeService.activateTrainee(1L);

        assertTrue(trainee.getUser().getActive());
        verify(traineeRepository).save(trainee);
    }

    @Test
    void shouldThrowWhenActivatingAlreadyActiveTrainee() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setActive(true);
        trainee.setUser(user);

        when(traineeRepository.findById(1L)).thenReturn(Optional.of(trainee));

        assertThrows(IllegalStateException.class, () -> traineeService.activateTrainee(1L));
        verify(traineeRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenActivatingNotFoundTrainee() {
        when(traineeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> traineeService.activateTrainee(1L));
        verify(traineeRepository, never()).save(any());
    }

    @Test
    void shouldDeactivateTraineeWhenActive() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setActive(true);
        trainee.setUser(user);

        when(traineeRepository.findById(2L)).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenAnswer(inv -> inv.getArgument(0));

        traineeService.deactivateTrainee(2L);

        assertFalse(trainee.getUser().getActive());
        verify(traineeRepository).save(trainee);
    }

    @Test
    void shouldThrowWhenDeactivatingAlreadyInactiveTrainee() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setActive(false);
        trainee.setUser(user);

        when(traineeRepository.findById(2L)).thenReturn(Optional.of(trainee));

        assertThrows(IllegalStateException.class, () -> traineeService.deactivateTrainee(2L));
        verify(traineeRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenDeactivatingNotFoundTrainee() {
        when(traineeRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> traineeService.deactivateTrainee(2L));
        verify(traineeRepository, never()).save(any());
    }

    @Test
    void shouldDeleteTraineeByUsername() {
        String username = "ali.veli";
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername(username);
        trainee.setUser(user);

        when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.of(trainee));
        doNothing().when(traineeRepository).delete(trainee);

        traineeService.deleteTraineeByUsername(username);

        verify(traineeRepository).findByUserUsername(username);
        verify(traineeRepository).delete(trainee);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentTrainee() {
        String username = "nouser";
        when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> traineeService.deleteTraineeByUsername(username));
        verify(traineeRepository).findByUserUsername(username);
        verify(traineeRepository, never()).delete(any());
    }

}