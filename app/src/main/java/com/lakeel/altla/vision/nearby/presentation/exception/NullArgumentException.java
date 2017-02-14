package com.lakeel.altla.vision.nearby.presentation.exception;

public final class NullArgumentException extends IllegalArgumentException {

    public NullArgumentException(String arg) {
        super(arg + "is null.");
    }
}
