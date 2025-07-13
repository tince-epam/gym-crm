package com.epam.gymcrm.storage;

import com.epam.gymcrm.domain.Trainee;
import com.epam.gymcrm.domain.Trainer;
import com.epam.gymcrm.domain.Training;
import com.epam.gymcrm.domain.TrainingType;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class Storage {

    private final ConcurrentMap<Long, Trainee> traineeMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Trainer> trainerMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Training> trainingMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, TrainingType> trainingTypeMap = new ConcurrentHashMap<>();

    public ConcurrentMap<Long, Trainee> getTraineeMap() {
        return traineeMap;
    }

    public ConcurrentMap<Long, Trainer> getTrainerMap() {
        return trainerMap;
    }

    public ConcurrentMap<Long, Training> getTrainingMap() {
        return trainingMap;
    }

    public ConcurrentMap<Long, TrainingType> getTrainingTypeMap() {
        return trainingTypeMap;
    }
}
