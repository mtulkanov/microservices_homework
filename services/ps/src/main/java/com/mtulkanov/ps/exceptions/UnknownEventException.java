package com.mtulkanov.ps.exceptions;

public class UnknownEventException extends RuntimeException {

    public UnknownEventException() {
    }

    public UnknownEventException(String message) {
        super(message);
    }

    public UnknownEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
