package com.epam.gymcrm.service;

import com.epam.gymcrm.domain.Trainee;
import com.epam.gymcrm.domain.Trainer;
import com.epam.gymcrm.domain.User;
import com.epam.gymcrm.dto.TraineeDto;
import com.epam.gymcrm.exception.InvalidCredentialsException;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.mapper.TraineeMapper;
import com.epam.gymcrm.repository.TraineeRepository;
import com.epam.gymcrm.repository.TrainerRepository;
import com.epam.gymcrm.repository.UserRepository;
import com.epam.gymcrm.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    public TraineeService(TraineeRepository traineeRepository, TrainerRepository trainerRepository, UserRepository userRepository) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TraineeDto createTrainee(TraineeDto traineeDto) {
        logger.info("Creating new trainee: {} {}", traineeDto.getFirstName(), traineeDto.getLastName());
        Trainee trainee = TraineeMapper.toTrainee(traineeDto);

        User user = UserUtils.createUser(traineeDto.getFirstName(), traineeDto.getLastName(), userRepository);

        trainee.setUser(user);

        // Relationship: set Trainer (ManyToMany)
        if (Objects.nonNull(traineeDto.getTrainerIds()) && !traineeDto.getTrainerIds().isEmpty()) {
            Set<Trainer> trainers = new HashSet<>(trainerRepository.findAllById(traineeDto.getTrainerIds()));
            trainee.setTrainers(trainers);
        }

        // Save with DAO
        Trainee savedTrainee = traineeRepository.save(trainee);

        logger.info("Trainee created: id={}, username={}", savedTrainee.getId(), savedTrainee.getUser().getUsername());
        return TraineeMapper.toTraineeDto(savedTrainee);
    }

    public TraineeDto findById(Long id) {
        logger.info("Finding trainee by id: {}", id);
        Trainee trainee = traineeRepository.findByIdWithTrainers(id)
                .orElseThrow(() -> {
                    logger.warn("Trainee not found for id: {}", id);
                    return new TraineeNotFoundException("Trainee not found with id: " + id);
                });
        logger.info("Trainee found by Id: id={}, username={}", trainee.getId(), trainee.getUser().getUsername());
        return TraineeMapper.toTraineeDto(trainee);
    }

    public List<TraineeDto> findAll() {
        logger.info("Retrieving all trainees");
        return traineeRepository.findAllWithTrainers().stream()
                .map(TraineeMapper::toTraineeDto)
                .toList();
    }

    @Transactional
    public void deleteById(Long id) {
        logger.info("Deleting trainee with id: {}", id);
        Trainee trainee = traineeRepository.findByIdWithTrainers(id)
                .orElseThrow(() -> {
                    logger.warn("Trainee to delete not found: id={}", id);
                    return new TraineeNotFoundException("Trainee not found with id: " + id);
                });
        traineeRepository.delete(trainee);
        logger.info("Trainee deleted: id={}", id);
    }

    @Transactional
    public void update(TraineeDto traineeDto) {
        Long id = traineeDto.getId();
        Trainee trainee = traineeRepository.findByIdWithTrainers(id)
                .orElseThrow(() -> {
                    logger.warn("Trainee to update not found: id={}", id);
                    return new TraineeNotFoundException("Trainee not found with id: " + id);
                });

        logger.info("Updating trainee: id={}, username={}", id, trainee.getUser().getUsername());

        String oldFirstName = trainee.getUser().getFirstName();
        String oldLastName = trainee.getUser().getLastName();

        if (Objects.nonNull(traineeDto.getFirstName())) {
            trainee.getUser().setFirstName(traineeDto.getFirstName());
        }
        if (Objects.nonNull(traineeDto.getLastName())) {
            trainee.getUser().setLastName(traineeDto.getLastName());
        }
        if (Objects.nonNull(traineeDto.getActive())) {
            trainee.getUser().setActive(traineeDto.getActive());
        }
        if (Objects.nonNull(traineeDto.getDateOfBirth())) {
            try {
                trainee.setDateOfBirth(LocalDate.parse(traineeDto.getDateOfBirth()));
            } catch (Exception e) {
                logger.warn("Invalid dateOfBirth: {}", traineeDto.getDateOfBirth());
            }
        }
        if (Objects.nonNull(traineeDto.getAddress())) {
            trainee.setAddress(traineeDto.getAddress());
        }

        if (Objects.nonNull(traineeDto.getTrainerIds())) {
            Set<Trainer> trainers = new HashSet<>(trainerRepository.findAllById(traineeDto.getTrainerIds()));
            trainee.setTrainers(trainers);
        }

        boolean isNameChanged =
                (Objects.nonNull(traineeDto.getFirstName()) && !Objects.equals(traineeDto.getFirstName(), oldFirstName)) ||
                        (Objects.nonNull(traineeDto.getLastName()) && !Objects.equals(traineeDto.getLastName(), oldLastName));

        if (isNameChanged) {
            String newUsername = UserUtils.generateUniqueUsername(
                    trainee.getUser().getFirstName(),
                    trainee.getUser().getLastName(),
                    userRepository
            );
            trainee.getUser().setUsername(newUsername);
        }

        traineeRepository.save(trainee);

        logger.info("Trainee updated: id={}, username={}", trainee.getId(), trainee.getUser().getUsername());
    }

    public boolean isTraineeCredentialsValid(String username, String password) {
        Trainee trainee = traineeRepository.findByUserUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Login failed: No Trainee found with username '{}'. Credentials: [username='{}', password='***']", username, username);
                    return new TraineeNotFoundException("Login failed: No Trainee found with username '" + username + "'");
                });

        if (!trainee.getUser().getPassword().equals(password)) {
            logger.warn("Login failed: Invalid password for Trainee with username '{}'. Provided password: '***'", username);
            throw new InvalidCredentialsException("Login failed: Invalid password for Trainee with username '" + username + "'");
        }

        logger.info("Login success: Trainee '{}' authenticated successfully.", username);
        return Boolean.TRUE;
    }

    public TraineeDto findByUsername(String username) {
        logger.info("Finding trainee by username: {}", username);
        Trainee trainee = traineeRepository.findByUserUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Trainee not found with username: {}", username);
                    return new TraineeNotFoundException("Trainee not found with username: " + username);
                });
        logger.info("Trainee found by username: id={}, username={}", trainee.getId(), trainee.getUser().getUsername());
        return TraineeMapper.toTraineeDto(trainee);
    }
}
