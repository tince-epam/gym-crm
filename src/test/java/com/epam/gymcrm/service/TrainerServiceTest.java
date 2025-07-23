package com.epam.gymcrm.service;

import com.epam.gymcrm.domain.Trainer;
import com.epam.gymcrm.domain.User;
import com.epam.gymcrm.dto.TrainerDto;
import com.epam.gymcrm.exception.InvalidCredentialsException;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.mapper.TrainerMapper;
import com.epam.gymcrm.repository.TrainerRepository;
import com.epam.gymcrm.repository.UserRepository;
import com.epam.gymcrm.util.UserUtils;
import org.junit.jupiter.api.BeforeEach;
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

    @Mock
    TrainerRepository trainerRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    TrainerService trainerService;

    TrainerDto trainerDto;
    Trainer trainer;
    User user;

    @BeforeEach
    void setUp() {
        trainerDto = new TrainerDto();
        trainerDto.setFirstName("Mehmet");
        trainerDto.setLastName("Yılmaz");
        trainerDto.setSpecialization("Fitness");

        user = new User();
        user.setId(100L);
        user.setFirstName("Mehmet");
        user.setLastName("Yılmaz");
        user.setUsername("mehmet.yilmaz");
        user.setPassword("pass");
        user.setActive(true);

        trainer = TrainerMapper.toTrainer(trainerDto);
        trainer.setId(1L);
        trainer.setUser(user);
    }

    @Test
    void shouldCreateTrainer() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(inv -> {
            Trainer t = inv.getArgument(0);
            t.setId(1L);
            t.getUser().setId(100L);
            return t;
        });

        TrainerDto result = trainerService.createTrainer(trainerDto);

        assertNotNull(result);
        assertEquals("Mehmet", result.getFirstName());
        assertEquals("Yılmaz", result.getLastName());
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void shouldFindTrainerById() {
        when(trainerRepository.findByIdWithTrainees(1L)).thenReturn(Optional.of(trainer));

        TrainerDto result = trainerService.findById(1L);

        assertNotNull(result);
        assertEquals("Mehmet", result.getFirstName());
        assertEquals("Yılmaz", result.getLastName());
        assertEquals("mehmet.yilmaz", result.getUsername());
        verify(trainerRepository).findByIdWithTrainees(1L);
    }

    @Test
    void shouldThrowExceptionWhenTrainerNotFound() {
        when(trainerRepository.findByIdWithTrainees(100L)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.findById(100L));
        verify(trainerRepository).findByIdWithTrainees(100L);
    }

    @Test
    void shouldDeleteTrainer() {
        when(trainerRepository.findByIdWithTrainees(1L)).thenReturn(Optional.of(trainer));

        doNothing().when(trainerRepository).delete(trainer);

        trainerService.deleteById(1L);

        verify(trainerRepository).delete(trainer);
    }

    @Test
    void shouldReturnAllTrainers() {
        Trainer t2 = new Trainer();
        t2.setId(2L);
        User user2 = new User();
        user2.setFirstName("Ayşe");
        user2.setLastName("Kaya");
        user2.setUsername("ayse.kaya");
        t2.setUser(user2);

        when(trainerRepository.findAllWithTrainees()).thenReturn(List.of(trainer, t2));

        List<TrainerDto> result = trainerService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Mehmet", result.get(0).getFirstName());
        assertEquals("Ayşe", result.get(1).getFirstName());
        assertEquals("mehmet.yilmaz", result.get(0).getUsername());
        assertEquals("ayse.kaya", result.get(1).getUsername());
        verify(trainerRepository).findAllWithTrainees();
    }

    @Test
    void shouldUpdateTrainerWithAllFields() {
        TrainerDto dto = new TrainerDto();
        dto.setId(5L);
        dto.setFirstName("NewFirstName");
        dto.setLastName("NewLastName");
        dto.setActive(false);
        dto.setSpecialization("NewSpecialization");

        when(trainerRepository.findByIdWithTrainees(5L)).thenReturn(Optional.of(trainer));
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(i -> i.getArgument(0));

        trainerService.update(dto);

        String expectedUsername = UserUtils.generateUniqueUsername("NewFirstName", "NewLastName", userRepository);

        assertEquals("NewFirstName", trainer.getUser().getFirstName());
        assertEquals("NewLastName", trainer.getUser().getLastName());
        assertEquals("NewSpecialization", trainer.getSpecialization());
        assertFalse(trainer.getUser().getActive());
        assertEquals(expectedUsername, trainer.getUser().getUsername());

        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void shouldThrowWhenTrainerNotFound() {
        TrainerDto dto = new TrainerDto();
        dto.setId(777L);

        when(trainerRepository.findByIdWithTrainees(777L)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.update(dto));
        verify(trainerRepository, never()).save(any(Trainer.class));
    }

    @Test
    void shouldNotUpdateUsernameIfNotChanged() {
        Trainer trainer = new Trainer();
        trainer.setId(5L);
        User user = new User();
        user.setFirstName("OldFirstName");
        user.setLastName("OldLastName");
        user.setUsername("old.username");
        trainer.setUser(user);

        TrainerDto dto = new TrainerDto();
        dto.setId(5L);
        dto.setFirstName("OldFirstName");
        dto.setLastName("OldLastName");

        when(trainerRepository.findByIdWithTrainees(5L)).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(i -> i.getArgument(0));

        trainerService.update(dto);

        assertEquals("old.username", trainer.getUser().getUsername());
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenTrainerNotFound() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("correct_pw");
        Trainer t = new Trainer();
        t.setUser(user);
        when(trainerRepository.findByUserUsername("user1")).thenReturn(Optional.of(trainer));

        assertThrows(InvalidCredentialsException.class, () ->
                trainerService.isTrainerCredentialsValid("user1", "wrong_pw"));
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenTrainerPasswordIncorrect() {
        User user = new User();
        user.setUsername("trainer1");
        user.setPassword("correct_pw");
        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(trainerRepository.findByUserUsername("trainer1")).thenReturn(Optional.of(trainer));

        assertThrows(InvalidCredentialsException.class, () ->
                trainerService.isTrainerCredentialsValid("trainer1", "wrong_pw"));
    }

    @Test
    void shouldReturnTrueWhenTrainerCredentialsAreValid() {
        User user = new User();
        user.setUsername("trainer1");
        user.setPassword("correct_pw");
        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(trainerRepository.findByUserUsername("trainer1")).thenReturn(Optional.of(trainer));

        assertTrue(trainerService.isTrainerCredentialsValid("trainer1", "correct_pw"));
    }

    @Test
    void shouldFindTrainerByUsername() {
        when(trainerRepository.findByUserUsername("mehmet.yilmaz")).thenReturn(Optional.of(trainer));
        TrainerDto dto = trainerService.findByUsername("mehmet.yilmaz");
        assertNotNull(dto);
        assertEquals("mehmet.yilmaz", dto.getUsername());
    }

    @Test
    void shouldThrowTrainerNotFoundExceptionWhenFindByUsername() {
        when(trainerRepository.findByUserUsername("nouser")).thenReturn(Optional.empty());
        assertThrows(TrainerNotFoundException.class, () ->
                trainerService.findByUsername("nouser"));
    }

    @Test
    void shouldChangeTrainerPasswordWhenOldPasswordMatches() {
        User user = new User();
        user.setUsername("trainer.user");
        user.setPassword("oldPass");
        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(trainerRepository.findByUserUsername("trainer.user"))
                .thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        trainerService.changeTrainerPassword("trainer.user", "oldPass", "newPass");

        assertEquals("newPass", trainer.getUser().getPassword());
        verify(trainerRepository).save(trainer);
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenTrainerOldPasswordDoesNotMatch() {
        User user = new User();
        user.setUsername("trainer.user");
        user.setPassword("oldPass");
        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(trainerRepository.findByUserUsername("trainer.user"))
                .thenReturn(Optional.of(trainer));

        assertThrows(InvalidCredentialsException.class, () ->
                trainerService.changeTrainerPassword("trainer.user", "wrongOldPass", "newPass")
        );
        verify(trainerRepository, never()).save(any());
    }

    @Test
    void shouldThrowTrainerNotFoundExceptionWhenTrainerUserNotFound() {
        when(trainerRepository.findByUserUsername("nouser"))
                .thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () ->
                trainerService.changeTrainerPassword("nouser", "anyPass", "newPass")
        );
        verify(trainerRepository, never()).save(any());
    }

    @Test
    void shouldActivateTrainerWhenInactive() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setActive(false);
        trainer.setUser(user);

        when(trainerRepository.findByIdWithTrainees(1L)).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(inv -> inv.getArgument(0));

        trainerService.activateTrainer(1L);

        assertTrue(trainer.getUser().getActive());
        verify(trainerRepository).save(trainer);
    }

    @Test
    void shouldThrowWhenActivatingAlreadyActiveTrainer() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setActive(true);
        trainer.setUser(user);

        when(trainerRepository.findByIdWithTrainees(1L)).thenReturn(Optional.of(trainer));

        assertThrows(IllegalStateException.class, () -> trainerService.activateTrainer(1L));
        verify(trainerRepository, never()).save(any());
    }


    @Test
    void shouldThrowWhenActivatingNotFoundTrainer() {
        when(trainerRepository.findByIdWithTrainees(1L)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.activateTrainer(1L));
        verify(trainerRepository, never()).save(any());
    }

    @Test
    void shouldDeactivateTrainerWhenActive() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setActive(true);
        trainer.setUser(user);

        when(trainerRepository.findByIdWithTrainees(2L)).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(inv -> inv.getArgument(0));

        trainerService.deactivateTrainer(2L);

        assertFalse(trainer.getUser().getActive());
        verify(trainerRepository).save(trainer);
    }

    @Test
    void shouldThrowWhenDeactivatingAlreadyInactiveTrainer() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setActive(false);
        trainer.setUser(user);

        when(trainerRepository.findByIdWithTrainees(2L)).thenReturn(Optional.of(trainer));

        assertThrows(IllegalStateException.class, () -> trainerService.deactivateTrainer(2L));
        verify(trainerRepository, never()).save(any());
    }


    @Test
    void shouldThrowWhenDeactivatingNotFoundTrainer() {
        when(trainerRepository.findByIdWithTrainees(2L)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.deactivateTrainer(2L));
        verify(trainerRepository, never()).save(any());
    }

}