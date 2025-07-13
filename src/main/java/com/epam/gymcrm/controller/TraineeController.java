package com.epam.gymcrm.controller;

import com.epam.gymcrm.dto.TraineeDto;
import com.epam.gymcrm.service.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trainees")
public class TraineeController {

    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping
    public ResponseEntity<TraineeDto> createTrainee(@RequestBody TraineeDto traineeDto) {
        return new ResponseEntity<>(traineeService.createTrainee(traineeDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TraineeDto> getTraineeById(@PathVariable Long id) {
        return ResponseEntity.ok(traineeService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TraineeDto>> getAllTrainees() {
        return ResponseEntity.ok(traineeService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTrainee(@PathVariable Long id, @RequestBody TraineeDto traineeDto) {
        traineeDto.setId(id);
        traineeService.update(traineeDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable Long id) {
        traineeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
