package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.domain.Trainer;
import com.epam.gymcrm.dto.TrainerDto;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.mapper.TrainerMapper;
import com.epam.gymcrm.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

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

        // Generate unique username
        trainer.setUsername(generateUsername(trainer));

        // Generate password
        trainer.setPassword(UserUtils.generateRandomPassword());
        trainer.setActive(true);

        Trainer savedTrainer = trainerDao.save(trainer);

        logger.info("Trainer created: id={}, username={}", trainer.getId(), trainer.getUsername());
        return TrainerMapper.toTrainerDto(savedTrainer);
    }

    public TrainerDto findById(Long id) {
        logger.info("Finding trainer by id: {}", id);
        Trainer trainer = trainerDao.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Trainer not found for id: {}", id);
                    return new TrainerNotFoundException("Trainer not found with id: " + id);
                });
        logger.info("Trainer found: id={}, username={}", trainer.getId(), trainer.getUsername());
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

        logger.info("Updating trainer: id={}, username={}", id, trainer.getUsername());

        if (trainerDto.getFirstName() != null) trainer.setFirstName(trainerDto.getFirstName());
        if (trainerDto.getLastName() != null) trainer.setLastName(trainerDto.getLastName());
        if (trainerDto.getActive() != null) trainer.setActive(trainerDto.getActive());
        if (trainerDto.getSpecialization() != null) trainer.setSpecialization(trainerDto.getSpecialization());


        String updatedUsername = generateUsername(trainer);
        if (!updatedUsername.equals(trainer.getUsername())) {
            trainer.setUsername(updatedUsername);
        }

        trainerDao.update(trainer);

        logger.info("Trainer updated: id={}, username={}", trainer.getId(), trainer.getUsername());
    }

    // Unique username generation
    private String generateUsername(Trainer trainer) {
        List<String> existingUsernames = trainerDao.findAll()
                .stream()
                .map(Trainer::getUsername)
                .toList();
        return UserUtils.generateUniqueUsername(trainer.getFirstName(), trainer.getLastName(), existingUsernames);
    }
}
