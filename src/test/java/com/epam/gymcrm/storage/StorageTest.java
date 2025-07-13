package com.epam.gymcrm.storage;

import com.epam.gymcrm.domain.Trainee;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StorageTest {

    @Test
    void allMapsShouldBeInitiallyEmpty() {
        Storage storage = new Storage();

        assertThat(storage.getTraineeMap()).isEmpty();
        assertThat(storage.getTrainerMap()).isEmpty();
        assertThat(storage.getTrainingMap()).isEmpty();
        assertThat(storage.getTrainingTypeMap()).isEmpty();
    }

    @Test
    void traineeMapShouldAllowPutAndGet() {
        Storage storage = new Storage();
        Trainee trainee = new Trainee();
        storage.getTraineeMap().put(1L, trainee);

        assertThat(storage.getTraineeMap()).containsKey(1L);
        assertThat(storage.getTraineeMap().get(1L)).isEqualTo(trainee);
    }
}