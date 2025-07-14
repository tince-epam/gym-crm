package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.domain.Trainee;
import com.epam.gymcrm.domain.User;
import com.epam.gymcrm.dto.TraineeDto;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.mapper.TraineeMapper;
import com.epam.gymcrm.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TraineeService {

    private final TraineeDao traineeDao;

    private final AtomicLong traineeIdSequence = new AtomicLong(1);
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    public TraineeService(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    public TraineeDto createTrainee(TraineeDto traineeDto) {
        logger.info("Creating new trainee: {} {}", traineeDto.getFirstName(), traineeDto.getLastName());
        Trainee trainee = TraineeMapper.toTrainee(traineeDto);

        trainee.setId(traineeIdSequence.getAndIncrement());

        User user = new User();
        user.setFirstName(traineeDto.getFirstName());
        user.setLastName(traineeDto.getLastName());
        user.setUsername(generateUsername(user));
        user.setPassword(UserUtils.generateRandomPassword());
        user.setActive(true);

        trainee.setUser(user);

        // Save with DAO
        Trainee savedTrainee = traineeDao.save(trainee);

        logger.info("Trainee created: id={}, username={}", trainee.getId(), user.getUsername());
        return TraineeMapper.toTraineeDto(savedTrainee);
    }

    public TraineeDto findById(Long id) {
        logger.info("Finding trainee by id: {}", id);
        Trainee trainee = traineeDao.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Trainee not found for id: {}", id);
                    return new TraineeNotFoundException("Trainee not found with id: " + id);
                });
        logger.info("Trainee found: id={}, username={}", trainee.getId(), trainee.getUser().getUsername());
        return TraineeMapper.toTraineeDto(trainee);
    }

    public List<TraineeDto> findAll() {
        logger.info("Retrieving all trainees");
        return traineeDao.findAll().stream()
                .map(TraineeMapper::toTraineeDto)
                .toList();
    }

    public void deleteById(Long id) {
        logger.info("Deleting trainee with id: {}", id);
        findById(id);
        traineeDao.deleteById(id);
        logger.info("Trainee deleted: id={}", id);
    }

    public void update(TraineeDto traineeDto) {
        Long id = traineeDto.getId();
        Trainee trainee = traineeDao.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Trainee to update not found: id={}", id);
                    return new TraineeNotFoundException("Trainee not found with id: " + id);
                });

        logger.info("Updating trainee: id={}, username={}", id, trainee.getUser().getUsername());

        if (!Objects.isNull(traineeDto.getFirstName())) trainee.getUser().setFirstName(traineeDto.getFirstName());
        if (!Objects.isNull(traineeDto.getLastName())) trainee.getUser().setLastName(traineeDto.getLastName());
        if (!Objects.isNull(traineeDto.getActive())) trainee.getUser().setActive(traineeDto.getActive());

        if (!Objects.isNull(traineeDto.getDateOfBirth()))
            trainee.setDateOfBirth(LocalDate.parse(traineeDto.getDateOfBirth()));
        if (!Objects.isNull(traineeDto.getAddress())) trainee.setAddress(traineeDto.getAddress());

        String updatedUsername = generateUsername(trainee.getUser());
        if (!updatedUsername.equals(trainee.getUser().getUsername())) {
            trainee.getUser().setUsername(updatedUsername);
        }

        traineeDao.update(trainee);

        logger.info("Trainee updated: id={}, username={}", trainee.getId(), trainee.getUser().getUsername());
    }

    private String generateUsername(User user) {
        List<String> existingUsernames = traineeDao.findAll()
                .stream()
                .map(trainee -> trainee.getUser().getUsername())
                .toList();

        return UserUtils.generateUniqueUsername(user.getFirstName(), user.getLastName(), existingUsernames);
    }
}
