package com.epam.gymcrm.dao.impl;

import com.epam.gymcrm.dao.TrainingDao;
import com.epam.gymcrm.domain.Training;
import com.epam.gymcrm.storage.Storage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainingDaoImpl implements TrainingDao {

    private final Storage storage;

    public TrainingDaoImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Training save(Training training) {
        storage.getTrainingMap().put(training.getId(), training);
        return training;
    }

    @Override
    public Optional<Training> findById(Long id) {
        return Optional.ofNullable(storage.getTrainingMap().get(id));
    }

    @Override
    public List<Training> findAll() {
        return new ArrayList<>(storage.getTrainingMap().values());
    }

    @Override
    public void deleteById(Long id) {
        storage.getTrainingMap().remove(id);
    }

    @Override
    public void update(Training training) {
        storage.getTrainingMap().put(training.getId(), training);
    }
}
