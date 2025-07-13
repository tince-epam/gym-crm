package com.epam.gymcrm.dao.impl;

import com.epam.gymcrm.dao.TrainingTypeDao;
import com.epam.gymcrm.domain.TrainingType;
import com.epam.gymcrm.storage.Storage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainingTypeDaoImpl implements TrainingTypeDao {

    private final Storage storage;

    public TrainingTypeDaoImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public TrainingType save(TrainingType trainingType) {
        storage.getTrainingTypeMap().put(trainingType.getId(), trainingType);
        return trainingType;
    }

    @Override
    public Optional<TrainingType> findById(Long id) {
        return Optional.ofNullable(storage.getTrainingTypeMap().get(id));
    }

    @Override
    public List<TrainingType> findAll() {
        return new ArrayList<>(storage.getTrainingTypeMap().values());
    }

    @Override
    public void deleteById(Long id) {
        storage.getTrainingTypeMap().remove(id);
    }

    @Override
    public void update(TrainingType trainingType) {
        storage.getTrainingTypeMap().put(trainingType.getId(), trainingType);
    }
}
