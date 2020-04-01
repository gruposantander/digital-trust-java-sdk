package com.santander.digital.verifiedid.exceptions;

public class HTTPClientException extends Exception {
    public HTTPClientException(Exception e) {
        super(e);
    }

    public HTTPClientException(String cause) {
        super(cause);
    }
}
