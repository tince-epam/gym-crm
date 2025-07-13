package com.epam.gymcrm.mapper;

import com.epam.gymcrm.domain.Training;
import com.epam.gymcrm.dto.TrainingDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TrainingMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static Training toTraining(TrainingDto trainingDto) {
        Training training = new Training();
        training.setId(training.getId());
        training.setTraineeId(trainingDto.getTraineeId());
        training.setTrainerId(trainingDto.getTrainerId());
        training.setTrainingName(trainingDto.getTrainingName());
        training.setTrainingTypeId(trainingDto.getTrainingTypeId());
        training.setTrainingDate(trainingDto.getTrainingDate());
        training.setTrainingDuration(trainingDto.getTrainingDuration());

        return training;
    }

    public static TrainingDto toTrainingDto(Training training) {
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setId(training.getId());
        trainingDto.setTraineeId(training.getTraineeId());
        trainingDto.setTrainerId(training.getTrainerId());
        trainingDto.setTrainingName(training.getTrainingName());
        trainingDto.setTrainingTypeId(training.getTrainingTypeId());
        trainingDto.setTrainingDate(training.getTrainingDate());
        trainingDto.setTrainingDuration(training.getTrainingDuration());

        return trainingDto;
    }
}
