package com.lakeel.altla.vision.nearby.rx;

import android.support.annotation.NonNull;

import rx.functions.Action1;

public abstract class DefaultAction<T> implements Action1<T> {

    private Resume resume;

    public DefaultAction(@NonNull Resume resume) {
        this.resume = resume;
    }

    @Override
    public void call(T t) {
        if (resume.isResume()) {
            onCall(t);
        }
    }

    abstract void onCall(T t);
}
