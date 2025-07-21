package com.epam.gymcrm.service;

import com.epam.gymcrm.domain.Trainer;
import com.epam.gymcrm.domain.User;
import com.epam.gymcrm.dto.TrainerDto;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.mapper.TrainerMapper;
import com.epam.gymcrm.repository.TrainerRepository;
import com.epam.gymcrm.repository.UserRepository;
import com.epam.gymcrm.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    public TrainerService(TrainerRepository trainerRepository, UserRepository userRepository) {
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
    }

    public TrainerDto createTrainer(TrainerDto trainerDto) {
        logger.info("Creating new trainer: {} {}", trainerDto.getFirstName(), trainerDto.getLastName());
        Trainer trainer = TrainerMapper.toTrainer(trainerDto);

        User user = UserUtils.createUser(trainerDto.getFirstName(), trainerDto.getLastName(), userRepository);

        trainer.setUser(user);

        // Save
        Trainer savedTrainer = trainerRepository.save(trainer);

        logger.info("Trainer created: id={}, username={}", trainer.getId(), user.getUsername());
        return TrainerMapper.toTrainerDto(savedTrainer);
    }

    public TrainerDto findById(Long id) {
        logger.info("Finding trainer by id: {}", id);
        Trainer trainer = trainerRepository.findByIdWithTrainees(id)
                .orElseThrow(() -> {
                    logger.warn("Trainer not found for id: {}", id);
                    return new TrainerNotFoundException("Trainer not found with id: " + id);
                });
        logger.info("Trainer found: id={}, username={}", trainer.getId(), trainer.getUser().getUsername());
        return TrainerMapper.toTrainerDto(trainer);
    }

    public List<TrainerDto> findAll() {
        logger.info("Retrieving all trainers");
        return trainerRepository.findAllWithTrainees().stream()
                .map(TrainerMapper::toTrainerDto)
                .toList();
    }

    public void deleteById(Long id) {
        logger.info("Deleting trainer with id: {}", id);
        Trainer trainer = trainerRepository.findByIdWithTrainees(id)
                .orElseThrow(() -> {
                    logger.warn("Trainer to delete not found for id: {}", id);
                    return new TrainerNotFoundException("Trainer not found with id: " + id);
                });
        trainerRepository.delete(trainer);
        logger.info("Trainer deleted: id={}", id);
    }

    public void update(TrainerDto trainerDto) {
        Long id = trainerDto.getId();
        Trainer trainer = trainerRepository.findByIdWithTrainees(id)
                .orElseThrow(() -> {
                    logger.warn("Trainer to update not found: id={}", id);
                    return new TrainerNotFoundException("Trainer not found with id: " + id);
                });

        logger.info("Updating trainer: id={}, username={}", id, trainer.getUser().getUsername());

        if (!Objects.isNull(trainerDto.getFirstName())) trainer.getUser().setFirstName(trainerDto.getFirstName());
        if (!Objects.isNull(trainerDto.getLastName())) trainer.getUser().setLastName(trainerDto.getLastName());
        if (!Objects.isNull(trainerDto.getActive())) trainer.getUser().setActive(trainerDto.getActive());

        if (trainerDto.getSpecialization() != null) trainer.setSpecialization(trainerDto.getSpecialization());

        String updatedUsername = UserUtils.generateUniqueUsername(trainerDto.getFirstName(), trainerDto.getLastName(), userRepository);
        if (!updatedUsername.equals(trainer.getUser().getUsername())) {
            trainer.getUser().setUsername(updatedUsername);
        }

        Trainer updatedTrainer = trainerRepository.save(trainer);

        logger.info("Trainer updated: id={}, username={}", updatedTrainer.getId(), updatedTrainer.getUser().getUsername());
    }
}
