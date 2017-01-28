package com.lakeel.altla.vision.nearby.core;

public final class NullArgumentException extends NullPointerException {

    public NullArgumentException(String arg) {
        super(arg + "is null.");
    }
}
