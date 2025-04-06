package com.hubspot.hubspot.exceptions;

public class ContactCreationException extends RuntimeException {
    public ContactCreationException(String message) {
        super(message);
    }
}