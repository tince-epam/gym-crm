package com.epam.gymcrm.service;

import com.epam.gymcrm.domain.TrainingType;
import com.epam.gymcrm.dto.TrainingTypeDto;
import com.epam.gymcrm.exception.TrainingTypeNotFoundException;
import com.epam.gymcrm.mapper.TrainingTypeMapper;
import com.epam.gymcrm.repository.TrainingTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;
    private static final Logger logger = LoggerFactory.getLogger(TrainingTypeService.class);

    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    public TrainingTypeDto findById(Long id) {
        logger.info("Finding training type by id: {}", id);
        TrainingType type = trainingTypeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("TrainingType not found for id: {}", id);
                    return new TrainingTypeNotFoundException("TrainingType not found with id: " + id);
                });
        logger.info("TrainingType found: id={}, name={}", type.getId(), type.getTrainingTypeName());
        return TrainingTypeMapper.toTrainingTypeDto(type);
    }

    public List<TrainingTypeDto> findAll() {
        logger.info("Retrieving all training types");
        return trainingTypeRepository.findAll().stream()
                .map(TrainingTypeMapper::toTrainingTypeDto)
                .toList();
    }
}
