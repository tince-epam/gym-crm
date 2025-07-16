package com.epam.gymcrm.exception;

import java.io.Serial;

public class TraineeNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public TraineeNotFoundException(String message) {
        super(message);
    }
}
