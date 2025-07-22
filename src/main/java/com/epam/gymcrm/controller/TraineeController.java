package com.epam.gymcrm.controller;

import com.epam.gymcrm.dto.PasswordChangeRequestDto;
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
    public ResponseEntity<TraineeDto> getTraineeById(
            @PathVariable("id") Long id,
            @RequestHeader("X-Username") String username,
            @RequestHeader("X-Password") String password
    ) {
        traineeService.isTraineeCredentialsValid(username, password);
        return ResponseEntity.ok(traineeService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TraineeDto>> getAllTrainees(
            @RequestHeader("X-Username") String username,
            @RequestHeader("X-Password") String password
    ) {
        traineeService.isTraineeCredentialsValid(username, password);
        return ResponseEntity.ok(traineeService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTrainee(
            @PathVariable("id") Long id,
            @RequestBody @Valid TraineeDto traineeDto,
            @RequestHeader("X-Username") String username,
            @RequestHeader("X-Password") String password
    ) {
        traineeService.isTraineeCredentialsValid(username, password);
        traineeDto.setId(id);
        traineeService.update(traineeDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainee(
            @PathVariable("id") Long id,
            @RequestHeader("X-Username") String username,
            @RequestHeader("X-Password") String password
    ) {
        traineeService.isTraineeCredentialsValid(username, password);
        traineeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<TraineeDto> getTraineeByUsername(
            @RequestParam(name = "username") String uname,
            @RequestHeader("X-Username") String username,
            @RequestHeader("X-Password") String password
    ) {
        traineeService.isTraineeCredentialsValid(username, password);
        return ResponseEntity.ok(traineeService.findByUsername(uname));
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> changeTraineePassword(
            @RequestHeader("X-Username") String username,
            @RequestHeader("X-Password") String password,
            @RequestBody PasswordChangeRequestDto passwordChangeRequest
    ) {
        traineeService.isTraineeCredentialsValid(username, password);
        traineeService.changeTraineePassword(username, password, passwordChangeRequest.getNewPassword());
        return ResponseEntity.noContent().build();
    }
}
