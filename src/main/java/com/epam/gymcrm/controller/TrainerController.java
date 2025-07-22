package com.epam.gymcrm.controller;

import com.epam.gymcrm.dto.TrainerDto;
import com.epam.gymcrm.service.TrainerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trainers")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping
    public ResponseEntity<TrainerDto> createTrainer(@RequestBody @Valid TrainerDto trainerDto) {
        return new ResponseEntity<>(trainerService.createTrainer(trainerDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainerDto> getTrainerById(
            @PathVariable("id") Long id,
            @RequestHeader("X-Username") String username,
            @RequestHeader("X-Password") String password
    ) {
        trainerService.isTrainerCredentialsValid(username, password);
        return ResponseEntity.ok(trainerService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TrainerDto>> getAllTrainers(
            @RequestHeader("X-Username") String username,
            @RequestHeader("X-Password") String password
    ) {
        trainerService.isTrainerCredentialsValid(username, password);
        return ResponseEntity.ok(trainerService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTrainer(
            @PathVariable("id") Long id,
            @RequestBody @Valid TrainerDto trainerDto,
            @RequestHeader("X-Username") String username,
            @RequestHeader("X-Password") String password
    ) {
        trainerService.isTrainerCredentialsValid(username, password);
        trainerDto.setId(id);
        trainerService.update(trainerDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainer(
            @PathVariable("id") Long id,
            @RequestHeader("X-Username") String username,
            @RequestHeader("X-Password") String password
    ) {
        trainerService.isTrainerCredentialsValid(username, password);
        trainerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
