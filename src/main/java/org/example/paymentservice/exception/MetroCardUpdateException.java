package org.example.paymentservice.exception;

public class MetroCardUpdateException extends RuntimeException {
    public MetroCardUpdateException(String message) {
        super(message);
    }

    public MetroCardUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

}
