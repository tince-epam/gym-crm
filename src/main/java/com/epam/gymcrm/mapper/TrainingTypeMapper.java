package com.epam.gymcrm.mapper;

import com.epam.gymcrm.domain.TrainingType;
import com.epam.gymcrm.dto.TrainingTypeDto;

public class TrainingTypeMapper {

    public static TrainingType toTrainingType(TrainingTypeDto dto) {
        if (dto == null) return null;
        TrainingType type = new TrainingType();
        type.setId(dto.getId());
        type.setName(dto.getName());
        return type;
    }

    public static TrainingTypeDto toTrainingTypeDto(TrainingType type) {
        if (type == null) return null;
        TrainingTypeDto dto = new TrainingTypeDto();
        dto.setId(type.getId());
        dto.setName(type.getName());
        return dto;
    }
}
