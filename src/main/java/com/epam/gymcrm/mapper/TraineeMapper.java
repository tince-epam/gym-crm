package com.epam.gymcrm.mapper;

import com.epam.gymcrm.domain.Trainee;
import com.epam.gymcrm.dto.TraineeDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class TraineeMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static TraineeDto toTraineeDto(Trainee trainee) {
        if (Objects.isNull(trainee))
            return null;

        TraineeDto dto = new TraineeDto();
        dto.setId(trainee.getId());
        dto.setFirstName(trainee.getFirstName());
        dto.setLastName(trainee.getLastName());
        dto.setUsername(trainee.getUsername());
        dto.setActive(trainee.getActive());
        dto.setDateOfBirth(
                !Objects.isNull(trainee.getDateOfBirth()) ? trainee.getDateOfBirth().format(FORMATTER) : null
        );
        dto.setAddress(trainee.getAddress());

        return dto;
    }

    public static Trainee toTrainee(TraineeDto traineeDto) {
        if (Objects.isNull(traineeDto))
            return null;

        Trainee trainee = new Trainee();
        trainee.setId(traineeDto.getId());
        trainee.setFirstName(traineeDto.getFirstName());
        trainee.setLastName(traineeDto.getLastName());
        trainee.setUsername(traineeDto.getUsername());
        trainee.setActive(traineeDto.getActive());
        if (!Objects.isNull(traineeDto.getDateOfBirth()) && !traineeDto.getDateOfBirth().isBlank()) {
            trainee.setDateOfBirth(LocalDate.parse(traineeDto.getDateOfBirth(), FORMATTER));
        }
        trainee.setAddress(traineeDto.getAddress());

        return trainee;
    }
}
