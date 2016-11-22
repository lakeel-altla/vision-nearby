package com.lakeel.altla.vision.nearby.presentation.view;


import rx.functions.Action1;

public abstract class DefaultAction<T> implements Action1<T> {

    private Resume mResume;

    protected DefaultAction(Resume resume) {
        mResume = resume;
    }

    @Override
    public final void call(T t) {
        if (mResume.isResume()) {
            onCall(t);
        }
    }

    protected abstract void onCall(T t);
}
