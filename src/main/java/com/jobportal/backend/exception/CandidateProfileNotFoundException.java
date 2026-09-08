package com.jobportal.backend.exception;

public class CandidateProfileNotFoundException extends RuntimeException {

    public CandidateProfileNotFoundException(String message) {
        super(message);
    }
}