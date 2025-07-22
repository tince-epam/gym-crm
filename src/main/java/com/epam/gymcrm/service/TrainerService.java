package com.epam.gymcrm.service;

import com.epam.gymcrm.domain.Trainee;
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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
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

    @Transactional
    public void deleteById(Long id) {
        logger.info("Deleting trainer with id: {}", id);
        Trainer trainer = trainerRepository.findByIdWithTrainees(id)
                .orElseThrow(() -> {
                    logger.warn("Trainer to delete not found for id: {}", id);
                    return new TrainerNotFoundException("Trainer not found with id: " + id);
                });
        
        for(Trainee trainee : trainer.getTrainees()) {
        	trainee.getTrainers().remove(trainer);
        }
        trainer.getTrainees().clear();
        
        trainerRepository.delete(trainer);
        logger.info("Trainer deleted: id={}", id);
    }

    @Transactional
    public void update(TrainerDto trainerDto) {
        Long id = trainerDto.getId();
        Trainer trainer = trainerRepository.findByIdWithTrainees(id)
                .orElseThrow(() -> {
                    logger.warn("Trainer to update not found: id={}", id);
                    return new TrainerNotFoundException("Trainer not found with id: " + id);
                });

        logger.info("Updating trainer: id={}, username={}", id, trainer.getUser().getUsername());

        String oldFirstName = trainer.getUser().getFirstName();
        String oldLastName = trainer.getUser().getLastName();

        // Update first name, last name, active status if present in DTO
        if (Objects.nonNull(trainerDto.getFirstName())) {
            trainer.getUser().setFirstName(trainerDto.getFirstName());
        }
        if (Objects.nonNull(trainerDto.getLastName())) {
            trainer.getUser().setLastName(trainerDto.getLastName());
        }
        if (Objects.nonNull(trainerDto.getActive())) {
            trainer.getUser().setActive(trainerDto.getActive());
        }

        // Update specialization if present
        if (trainerDto.getSpecialization() != null) {
            trainer.setSpecialization(trainerDto.getSpecialization());
        }

        // Check if first name or last name has changed, then update username accordingly
        boolean nameChanged =
                (Objects.nonNull(trainerDto.getFirstName()) && !Objects.equals(trainerDto.getFirstName(), oldFirstName)) ||
                        (Objects.nonNull(trainerDto.getLastName()) && !Objects.equals(trainerDto.getLastName(), oldLastName));

        if (nameChanged) {
            String newFirstName = Objects.nonNull(trainerDto.getFirstName()) ? trainerDto.getFirstName() : oldFirstName;
            String newLastName = Objects.nonNull(trainerDto.getLastName()) ? trainerDto.getLastName() : oldLastName;
            String newUsername = UserUtils.generateUniqueUsername(newFirstName, newLastName, userRepository);
            trainer.getUser().setUsername(newUsername);
        }

        Trainer updatedTrainer = trainerRepository.save(trainer);

        logger.info("Trainer updated: id={}, username={}", updatedTrainer.getId(), updatedTrainer.getUser().getUsername());
    }


}
