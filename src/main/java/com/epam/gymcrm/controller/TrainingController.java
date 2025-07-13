package com.epam.gymcrm.controller;

import com.epam.gymcrm.dto.TrainingDto;
import com.epam.gymcrm.service.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trainings")
public class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    public ResponseEntity<TrainingDto> createTraining(@RequestBody TrainingDto dto) {
        return new ResponseEntity<>(trainingService.createTraining(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingDto> getTrainingById(@PathVariable Long id) {
        return ResponseEntity.ok(trainingService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TrainingDto>> getAllTrainings() {
        return ResponseEntity.ok(trainingService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTraining(@PathVariable Long id, @RequestBody TrainingDto dto) {
        dto.setId(id);
        trainingService.update(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTraining(@PathVariable Long id) {
        trainingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
