package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.domain.Trainer;
import com.epam.gymcrm.domain.User;
import com.epam.gymcrm.dto.TrainerDto;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.mapper.TrainerMapper;
import com.epam.gymcrm.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TrainerService {

    private final TrainerDao trainerDao;

    private final AtomicLong trainerIdSequence = new AtomicLong(1);
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    public TrainerService(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public TrainerDto createTrainer(TrainerDto trainerDto) {
        logger.info("Creating new trainer: {} {}", trainerDto.getFirstName(), trainerDto.getLastName());
        Trainer trainer = TrainerMapper.toTrainer(trainerDto);

        // Set Id
        trainer.setId(trainerIdSequence.getAndIncrement());

        User user = new User();
        user.setFirstName(trainerDto.getFirstName());
        user.setLastName(trainerDto.getLastName());
        user.setUsername(generateUsername(user));
        user.setPassword(UserUtils.generateRandomPassword());
        user.setActive(true);

        trainer.setUser(user);

        // Save
        Trainer savedTrainer = trainerDao.save(trainer);

        logger.info("Trainer created: id={}, username={}", trainer.getId(), user.getUsername());
        return TrainerMapper.toTrainerDto(savedTrainer);
    }

    public TrainerDto findById(Long id) {
        logger.info("Finding trainer by id: {}", id);
        Trainer trainer = trainerDao.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Trainer not found for id: {}", id);
                    return new TrainerNotFoundException("Trainer not found with id: " + id);
                });
        logger.info("Trainer found: id={}, username={}", trainer.getId(), trainer.getUser().getUsername());
        return TrainerMapper.toTrainerDto(trainer);
    }

    public List<TrainerDto> findAll() {
        logger.info("Retrieving all trainers");
        return trainerDao.findAll().stream()
                .map(TrainerMapper::toTrainerDto)
                .toList();
    }

    public void deleteById(Long id) {
        logger.info("Deleting trainer with id: {}", id);
        findById(id);
        trainerDao.deleteById(id);
        logger.info("Trainer deleted: id={}", id);
    }

    public void update(TrainerDto trainerDto) {
        Long id = trainerDto.getId();
        Trainer trainer = trainerDao.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Trainer to update not found: id={}", id);
                    return new TrainerNotFoundException("Trainer not found with id: " + id);
                });

        logger.info("Updating trainer: id={}, username={}", id, trainer.getUser().getUsername());

        if (!Objects.isNull(trainerDto.getFirstName())) trainer.getUser().setFirstName(trainerDto.getFirstName());
        if (!Objects.isNull(trainerDto.getLastName())) trainer.getUser().setLastName(trainerDto.getLastName());
        if (!Objects.isNull(trainerDto.getActive())) trainer.getUser().setActive(trainerDto.getActive());

        if (trainerDto.getSpecialization() != null) trainer.setSpecialization(trainerDto.getSpecialization());

        String updatedUsername = generateUsername(trainer.getUser());
        if (!updatedUsername.equals(trainer.getUser().getUsername())) {
            trainer.getUser().setUsername(updatedUsername);
        }

        trainerDao.update(trainer);

        logger.info("Trainer updated: id={}, username={}", trainer.getId(), trainer.getUser().getUsername());
    }

    // Unique username generation
    private String generateUsername(User user) {
        List<String> existingUsernames = trainerDao.findAll()
                .stream()
                .map(trainer -> trainer.getUser().getUsername())
                .toList();
        return UserUtils.generateUniqueUsername(user.getFirstName(), user.getLastName(), existingUsernames);
    }
}
