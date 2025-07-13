package com.epam.gymcrm.dao.impl;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.domain.Trainee;
import com.epam.gymcrm.storage.Storage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TraineeDaoImpl implements TraineeDao {

    private final Storage storage;

    public TraineeDaoImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Trainee save(Trainee trainee) {
        storage.getTraineeMap().put(trainee.getId(), trainee);
        return trainee;
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(storage.getTraineeMap().get(id));
    }

    @Override
    public List<Trainee> findAll() {
        return new ArrayList<>(storage.getTraineeMap().values());
    }

    @Override
    public void deleteById(Long id) {
        storage.getTraineeMap().remove(id);
    }

    @Override
    public void update(Trainee trainee) {
        storage.getTraineeMap().put(trainee.getId(), trainee);
    }
}
