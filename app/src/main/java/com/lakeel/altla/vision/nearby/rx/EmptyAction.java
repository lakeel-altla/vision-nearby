package com.lakeel.altla.vision.nearby.rx;

import rx.functions.Action1;

public final class EmptyAction<T> implements Action1<T> {
    @Override
    public void call(T t) {
        // do nothing.
    }
}
