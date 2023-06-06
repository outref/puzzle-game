package com.nikonets.puzzle.exception;

public class PuzzleRepositoryException extends RuntimeException {
    public PuzzleRepositoryException(String message) {
        super(message);
    }

    public PuzzleRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
