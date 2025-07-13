package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.dao.TrainingDao;
import com.epam.gymcrm.dao.TrainingTypeDao;
import com.epam.gymcrm.domain.Training;
import com.epam.gymcrm.dto.TrainingDto;
import com.epam.gymcrm.exception.*;
import com.epam.gymcrm.mapper.TrainingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TrainingService {

    private final TrainingDao trainingDao;
    private final TrainerDao trainerDao;
    private final TraineeDao traineeDao;
    private final TrainingTypeDao trainingTypeDao;
    private final AtomicLong trainingIdSequence = new AtomicLong(1);
    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    public TrainingService(TrainingDao trainingDao,
                           TrainerDao trainerDao,
                           TraineeDao traineeDao,
                           TrainingTypeDao trainingTypeDao) {
        this.trainingDao = trainingDao;
        this.trainerDao = trainerDao;
        this.traineeDao = traineeDao;
        this.trainingTypeDao = trainingTypeDao;
    }

    public TrainingDto createTraining(TrainingDto dto) {
        logger.info("Creating new training: {}", dto.getTrainingName());
        Training training = TrainingMapper.toTraining(dto);

        if (Objects.isNull(dto.getTrainerId()) || trainerDao.findById(dto.getTrainerId()).isEmpty()) {
            logger.warn("Trainer not found for id: {}", dto.getTrainerId());
            throw new TrainerNotFoundException("Trainer not found with id: " + dto.getTrainerId());
        }
        if (Objects.isNull(dto.getTraineeId()) || traineeDao.findById(dto.getTraineeId()).isEmpty()) {
            logger.warn("Trainee not found for id: {}", dto.getTraineeId());
            throw new TraineeNotFoundException("Trainee not found with id: " + dto.getTraineeId());
        }
        if (Objects.isNull(dto.getTrainingTypeId()) || trainingTypeDao.findById(dto.getTrainingTypeId()).isEmpty()) {
            logger.warn("TrainingType not found for id: {}", dto.getTrainingTypeId());
            throw new TrainingTypeNotFoundException("TrainingType not found with id: " + dto.getTrainingTypeId());
        }

        boolean isTrainerBusy = trainingDao.findAll().stream()
                .anyMatch(t -> t.getTrainerId().equals(dto.getTrainerId())
                        && t.getTrainingDate().equals(training.getTrainingDate()));

        if (isTrainerBusy) {
            logger.warn("Trainer {} has a schedule conflict at {}", dto.getTrainerId(), dto.getTrainingDate());
            throw new TrainerScheduleConflictException("Trainer is already assigned to another training at the same time!");
        }

        training.setId(trainingIdSequence.getAndIncrement());
        Training savedTraining = trainingDao.save(training);
        logger.info("Training created: id={}, name={}", savedTraining.getId(), savedTraining.getTrainingName());
        return TrainingMapper.toTrainingDto(savedTraining);
    }

    public TrainingDto findById(Long id) {
        logger.info("Finding training by id: {}", id);
        Training training = trainingDao.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Training not found for id: {}", id);
                    return new TrainingNotFoundException("Training not found with id: " + id);
                });
        logger.info("Training found: id={}, name={}", training.getId(), training.getTrainingName());
        return TrainingMapper.toTrainingDto(training);
    }

    public List<TrainingDto> findAll() {
        logger.info("Retrieving all trainings");
        return trainingDao.findAll().stream()
                .map(TrainingMapper::toTrainingDto)
                .toList();
    }

    public void deleteById(Long id) {
        logger.info("Deleting training with id: {}", id);
        Training training = trainingDao.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Training not found for delete operation with id: {}", id);
                    return new TrainingNotFoundException("Training not found for delete operation with id: " + id);
                });
        trainingDao.deleteById(id);
        logger.info("Training deleted: id={}", id);
    }

    public void update(TrainingDto dto) {
        Long id = dto.getId();
        Training existing = trainingDao.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Training not found for update operation with id: {}", id);
                    return new TrainingNotFoundException("Training not found for update operation with id: " + id);
                });

        // Trainer existence
        if (dto.getTrainerId() != null && trainerDao.findById(dto.getTrainerId()).isEmpty()) {
            logger.warn("Trainer not found for update operation with id: {}", dto.getTrainerId());
            throw new TrainerNotFoundException("Trainer not found for update operation with id: " + dto.getTrainerId());
        }
        // Trainee existence
        if (dto.getTraineeId() != null && traineeDao.findById(dto.getTraineeId()).isEmpty()) {
            logger.warn("Trainee not found for update operation with id: {}", dto.getTraineeId());
            throw new TraineeNotFoundException("Trainee not found for update operation with id: " + dto.getTraineeId());
        }
        // TrainingType existence
        if (dto.getTrainingTypeId() != null && trainingTypeDao.findById(dto.getTrainingTypeId()).isEmpty()) {
            logger.warn("TrainingType not found for update operation with id: {}", dto.getTrainingTypeId());
            throw new TrainingTypeNotFoundException("TrainingType not found for update operation with id: " + dto.getTrainingTypeId());
        }

        // Trainer schedule conflict check
        if (dto.getTrainerId() != null && dto.getTrainingDate() != null) {
            boolean conflict = trainingDao.findAll().stream()
                    .anyMatch(t -> !t.getId().equals(id) &&
                            t.getTrainerId().equals(dto.getTrainerId()) &&
                            t.getTrainingDate().equals(dto.getTrainingDate()));

            if (conflict) {
                logger.warn("Trainer {} has a schedule conflict at {} during update operation", dto.getTrainerId(), dto.getTrainingDate());
                throw new TrainerScheduleConflictException("Trainer is already assigned to another training at the same time during update operation!");
            }
        }

        // Partial update
        if (!Objects.isNull(dto.getTrainingName())) existing.setTrainingName(dto.getTrainingName());
        if (!Objects.isNull(dto.getTrainerId())) existing.setTrainerId(dto.getTrainerId());
        if (!Objects.isNull(dto.getTraineeId())) existing.setTraineeId(dto.getTraineeId());
        if (!Objects.isNull(dto.getTrainingTypeId())) existing.setTrainingTypeId(dto.getTrainingTypeId());
        if (!Objects.isNull(dto.getTrainingDate())) existing.setTrainingDate(dto.getTrainingDate());
        if (dto.getTrainingDuration() != 0) existing.setTrainingDuration(dto.getTrainingDuration());

        trainingDao.update(existing);
        logger.info("Training updated: id={}, name={}", existing.getId(), existing.getTrainingName());
    }
}
