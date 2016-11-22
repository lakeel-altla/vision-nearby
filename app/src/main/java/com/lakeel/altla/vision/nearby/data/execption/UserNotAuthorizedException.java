package com.lakeel.altla.vision.nearby.data.execption;

public class UserNotAuthorizedException extends RuntimeException {

    public UserNotAuthorizedException(String message) {
        super(message);
    }
}
