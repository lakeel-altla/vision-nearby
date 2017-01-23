package com.lakeel.altla.vision.nearby.core;

public final class NullArgumentException extends RuntimeException {

    public NullArgumentException(String arg) {
        super(arg + "is null.");
    }
}
