package com.lakeel.altla.vision.nearby.rx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.functions.Action1;

public final class ErrorAction<T> implements Action1<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorAction.class);

    private final String errorMessage;

    public ErrorAction(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void call(T t) {
        LOGGER.error(errorMessage);
    }
}
