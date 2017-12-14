package com.company.exceptions;

public class KeyNotFoundException extends RuntimeException {
    public static final String NOT_ENROLLED_IN_COURSE = "BashSoftStudent must be enrolled in a course before you set his mark.";

    public KeyNotFoundException() {
        super(NOT_ENROLLED_IN_COURSE);
    }

    public KeyNotFoundException(String message) {
        super(message);
    }
}
