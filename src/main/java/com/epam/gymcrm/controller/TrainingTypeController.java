package com.epam.gymcrm.controller;

import com.epam.gymcrm.dto.TrainingTypeDto;
import com.epam.gymcrm.service.TrainingTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/training-types")
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;

    public TrainingTypeController(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingTypeDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(trainingTypeService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TrainingTypeDto>> getAll() {
        return ResponseEntity.ok(trainingTypeService.findAll());
    }

}
