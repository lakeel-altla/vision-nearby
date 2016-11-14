package com.lakeel.profile.notification.data.execption;

public final class SignatureCreateException extends RuntimeException {

    public SignatureCreateException(String signatureString) {
        super("Failed to create a hashed signature:signature = " + signatureString);
    }
}
