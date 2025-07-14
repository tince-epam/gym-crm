package com.epam.gymcrm.bootstrap;

import com.epam.gymcrm.dto.TraineeDto;
import com.epam.gymcrm.dto.TrainerDto;
import com.epam.gymcrm.dto.TrainingDto;
import com.epam.gymcrm.dto.TrainingTypeDto;
import com.epam.gymcrm.service.TraineeService;
import com.epam.gymcrm.service.TrainerService;
import com.epam.gymcrm.service.TrainingService;
import com.epam.gymcrm.service.TrainingTypeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class StorageInitializer {

    private static final Logger logger = LoggerFactory.getLogger(StorageInitializer.class);

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;
    private final TrainingService trainingService;

    public StorageInitializer(
            TraineeService traineeService,
            TrainerService trainerService,
            TrainingTypeService trainingTypeService,
            TrainingService trainingService
    ) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
        this.trainingService = trainingService;
    }

    @PostConstruct
    public void init() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            // 1. Loads Trainee
            InputStream traineeStream = getClass().getResourceAsStream("/data/trainees.json");
            List<TraineeDto> trainees = mapper.readValue(traineeStream, new TypeReference<>() {});
            trainees.forEach(traineeService::createTrainee);

            // 2. Loads Trainer
            InputStream trainerStream = getClass().getResourceAsStream("/data/trainers.json");
            List<TrainerDto> trainers = mapper.readValue(trainerStream, new TypeReference<>() {});
            trainers.forEach(trainerService::createTrainer);

            // 3. Loads TrainingType
            InputStream typeStream = getClass().getResourceAsStream("/data/trainingTypes.json");
            List<TrainingTypeDto> types = mapper.readValue(typeStream, new TypeReference<>() {});
            types.forEach(trainingTypeService::create);

            // 4. Loads Trainings
            InputStream trainingStream = getClass().getResourceAsStream("/data/trainings.json");
            List<TrainingDto> trainings = mapper.readValue(trainingStream, new TypeReference<>() {});
            trainings.forEach(trainingService::createTraining);

        } catch (Exception e) {
            logger.error("Failed to initialize storage data from file during application startup!", e);
        }
    }
}
