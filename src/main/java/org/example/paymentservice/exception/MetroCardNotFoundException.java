package org.example.paymentservice.exception;

public class MetroCardNotFoundException extends RuntimeException {
    public MetroCardNotFoundException(String message) {
        super(message);
    }
}
