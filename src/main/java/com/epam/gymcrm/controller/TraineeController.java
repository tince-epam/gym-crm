package com.epam.gymcrm.controller;

import com.epam.gymcrm.dto.TraineeDto;
import com.epam.gymcrm.service.TraineeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/trainees", produces = "application/json")
public class TraineeController {

    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping
    public ResponseEntity<TraineeDto> createTrainee(@RequestBody @Valid TraineeDto traineeDto) {
        return new ResponseEntity<>(traineeService.createTrainee(traineeDto), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TraineeDto> getTraineeById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(traineeService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TraineeDto>> getAllTrainees() {
        return ResponseEntity.ok(traineeService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTrainee(@PathVariable("id") Long id, @RequestBody @Valid TraineeDto traineeDto) {
        traineeDto.setId(id);
        traineeService.update(traineeDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable("id") Long id) {
        traineeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
