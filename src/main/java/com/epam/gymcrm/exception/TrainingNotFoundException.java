package com.epam.gymcrm.exception;

public class TrainingNotFoundException extends RuntimeException {

    public TrainingNotFoundException(String message) {
        super(message);
    }
}
