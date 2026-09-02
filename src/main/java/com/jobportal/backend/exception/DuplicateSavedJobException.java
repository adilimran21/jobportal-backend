package com.jobportal.backend.exception;

public class DuplicateSavedJobException
        extends RuntimeException {

    public DuplicateSavedJobException(String message) {
        super(message);
    }
}