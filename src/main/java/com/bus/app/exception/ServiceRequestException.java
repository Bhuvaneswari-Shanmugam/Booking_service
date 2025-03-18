package com.bus.app.exception;

public class ServiceRequestException extends RuntimeException {
    public ServiceRequestException(final String message) {
        super(message);
    }
}
