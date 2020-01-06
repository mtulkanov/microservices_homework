package com.mtulkanov.po.exceptions;

public class SpecificationNotFoundException extends RuntimeException {
    public SpecificationNotFoundException() {
    }

    public SpecificationNotFoundException(String message) {
        super(message);
    }

    public SpecificationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
