package com.epam.gymcrm.mapper;

import com.epam.gymcrm.domain.Trainer;
import com.epam.gymcrm.dto.TrainerDto;

import java.time.format.DateTimeFormatter;

public class TrainerMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Trainer toTrainer(TrainerDto trainerDto) {
        Trainer trainer = new Trainer();
        trainer.setId(trainerDto.getId());
        trainer.setFirstName(trainerDto.getFirstName());
        trainer.setLastName(trainerDto.getLastName());
        trainer.setUsername(trainerDto.getUsername());
        trainer.setActive(trainerDto.getActive());
        trainer.setSpecialization(trainerDto.getSpecialization());

        return trainer;
    }

    public static TrainerDto toTrainerDto(Trainer trainer) {
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setId(trainer.getId());
        trainerDto.setFirstName(trainer.getFirstName());
        trainerDto.setLastName(trainer.getLastName());
        trainerDto.setUsername(trainer.getUsername());
        trainerDto.setActive(trainer.getActive());
        trainerDto.setSpecialization(trainer.getSpecialization());

        return trainerDto;
    }
}
