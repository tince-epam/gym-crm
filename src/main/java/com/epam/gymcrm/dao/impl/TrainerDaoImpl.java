package com.epam.gymcrm.dao.impl;

import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.domain.Trainer;
import com.epam.gymcrm.storage.Storage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainerDaoImpl implements TrainerDao {

    private final Storage storage;

    public TrainerDaoImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Trainer save(Trainer trainer) {
        storage.getTrainerMap().put(trainer.getId(), trainer);
        return trainer;
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(storage.getTrainerMap().get(id));
    }

    @Override
    public List<Trainer> findAll() {
        return new ArrayList<>(storage.getTrainerMap().values());
    }

    @Override
    public void deleteById(Long id) {
        storage.getTrainerMap().remove(id);
    }

    @Override
    public void update(Trainer trainer) {
        storage.getTrainerMap().put(trainer.getId(), trainer);
    }
}
