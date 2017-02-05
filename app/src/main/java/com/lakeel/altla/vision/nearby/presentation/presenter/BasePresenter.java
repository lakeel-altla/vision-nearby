package com.lakeel.altla.vision.nearby.presentation.presenter;

import android.support.annotation.CallSuper;

public class BasePresenter<V> {

    protected V view;

    protected V getView() {
        return view;
    }

    @CallSuper
    public void onCreateView(V v) {
        view = v;
    }
}
