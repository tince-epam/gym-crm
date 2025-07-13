package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.TrainingTypeDao;
import com.epam.gymcrm.domain.TrainingType;
import com.epam.gymcrm.dto.TrainingTypeDto;
import com.epam.gymcrm.exception.TrainingTypeNotFoundException;
import com.epam.gymcrm.mapper.TrainingTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TrainingTypeService {

    private final TrainingTypeDao trainingTypeDao;
    private final AtomicLong typeIdSequence = new AtomicLong(1);
    private static final Logger logger = LoggerFactory.getLogger(TrainingTypeService.class);


    public TrainingTypeService(TrainingTypeDao trainingTypeDao) {
        this.trainingTypeDao = trainingTypeDao;
    }

    public TrainingTypeDto create(TrainingTypeDto dto) {
        logger.info("Creating new training type: {}", dto.getName());
        TrainingType type = TrainingTypeMapper.toTrainingType(dto);
        type.setId(typeIdSequence.getAndIncrement());
        TrainingType saved = trainingTypeDao.save(type);
        logger.info("TrainingType created: id={}, name={}", saved.getId(), saved.getName());
        return TrainingTypeMapper.toTrainingTypeDto(saved);
    }

    public TrainingTypeDto findById(Long id) {
        logger.info("Finding training type by id: {}", id);
        TrainingType type = trainingTypeDao.findById(id)
                .orElseThrow(() -> {
                    logger.warn("TrainingType not found for id: {}", id);
                    return new TrainingTypeNotFoundException("TrainingType not found with id: " + id);
                });
        logger.info("TrainingType found: id={}, name={}", type.getId(), type.getName());
        return TrainingTypeMapper.toTrainingTypeDto(type);
    }

    public List<TrainingTypeDto> findAll() {
        logger.info("Retrieving all training types");
        return trainingTypeDao.findAll().stream()
                .map(TrainingTypeMapper::toTrainingTypeDto)
                .toList();
    }

    public void update(TrainingTypeDto dto) {
        Long id = dto.getId();
        TrainingType existing = trainingTypeDao.findById(id)
                .orElseThrow(() -> {
                    logger.warn("TrainingType not found for update operation with id: {}", id);
                    return new TrainingTypeNotFoundException("TrainingType not found with id: " + id);
                });

        if (dto.getName() != null) existing.setName(dto.getName());

        trainingTypeDao.update(existing);
        logger.info("TrainingType updated: id={}, name={}", existing.getId(), existing.getName());
    }

    public void deleteById(Long id) {
        logger.info("Deleting training type with id: {}", id);
        findById(id);
        trainingTypeDao.deleteById(id);
        logger.info("TrainingType deleted: id={}", id);
    }
}
