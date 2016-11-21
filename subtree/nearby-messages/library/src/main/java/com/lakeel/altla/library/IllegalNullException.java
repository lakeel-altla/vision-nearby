package com.lakeel.altla.library;

public final class IllegalNullException extends RuntimeException {

    public IllegalNullException(String variable) {
        super(variable + " is null.");
    }
}
