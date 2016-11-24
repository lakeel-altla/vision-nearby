package com.lakeel.altla.library;

/**
 * Thrown to indicate that a method has been passed an null argument.
 */
public final class IllegalArgumentNullException extends IllegalArgumentException {

    /**
     * Constructs an IllegalArgumentNullException with the specified detail message.
     * @param arg The detail message.
     */
    public IllegalArgumentNullException(String arg) {
        super(arg + " is null.");
    }
}
