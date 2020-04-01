package com.santander.digital.verifiedid.exceptions;

public class DigitalIdConfigurationException extends RuntimeException {
    public DigitalIdConfigurationException(Exception e) {
        super(e);
    }

    public DigitalIdConfigurationException(String cause) {
        super(cause);
    }
}
