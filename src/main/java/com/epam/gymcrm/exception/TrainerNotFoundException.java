package com.epam.gymcrm.exception;

import java.io.Serial;

public class TrainerNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    public TrainerNotFoundException(String message) {
        super(message);
    }
}
