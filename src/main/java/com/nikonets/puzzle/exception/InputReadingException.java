package com.nikonets.puzzle.exception;

public class InputReadingException extends RuntimeException {
    public InputReadingException(String message) {
        super(message);
    }

    public InputReadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
