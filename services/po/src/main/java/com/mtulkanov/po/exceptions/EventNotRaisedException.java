package com.mtulkanov.po.exceptions;

public class EventNotRaisedException extends Exception {
    public EventNotRaisedException() {
        super();
    }

    public EventNotRaisedException(String message) {
        super(message);
    }

    public EventNotRaisedException(String message, Throwable cause) {
        super(message, cause);
    }
}
