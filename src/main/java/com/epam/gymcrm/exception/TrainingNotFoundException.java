package com.epam.gymcrm.exception;

import java.io.Serial;

public class TrainingNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    public TrainingNotFoundException(String message) {
        super(message);
    }
}
