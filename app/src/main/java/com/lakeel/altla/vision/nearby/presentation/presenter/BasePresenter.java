package com.lakeel.altla.vision.nearby.presentation.presenter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

public class BasePresenter<V> {

    protected V view;

    protected V getView() {
        return view;
    }

    @CallSuper
    public void onCreateView(@NonNull V v) {
        view = v;
    }
}
