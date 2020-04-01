package com.santander.digital.verifiedid.exceptions;

public class DigitalIdGenericException extends RuntimeException {
    public DigitalIdGenericException(Exception e) {
        super(e);
    }

    public DigitalIdGenericException(String message) {
        super(message);
    }
}
