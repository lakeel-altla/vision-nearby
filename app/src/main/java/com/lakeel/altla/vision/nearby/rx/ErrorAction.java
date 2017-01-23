package com.lakeel.altla.vision.nearby.rx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.functions.Action1;

public final class ErrorAction<T> implements Action1<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorAction.class);

    @Override
    public void call(T t) {
        LOGGER.error("Failed to process.", t);
    }
}
