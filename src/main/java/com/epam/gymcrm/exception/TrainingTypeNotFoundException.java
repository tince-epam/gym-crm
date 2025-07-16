package com.epam.gymcrm.exception;

import java.io.Serial;

public class TrainingTypeNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public TrainingTypeNotFoundException(String message) {
        super(message);
    }
}
