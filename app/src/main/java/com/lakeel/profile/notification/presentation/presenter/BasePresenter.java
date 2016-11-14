package com.lakeel.profile.notification.presentation.presenter;

import android.support.annotation.CallSuper;

public class BasePresenter<V> {

    protected ReusableCompositeSubscription mCompositeSubscription = new ReusableCompositeSubscription();

    protected V mV;

    @CallSuper
    public void onCreateView(V v) {
        mV = v;
    }

    protected V getView() {
        return mV;
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onStart() {
    }

    @CallSuper
    public void onStop() {
        mCompositeSubscription.unsubscribe();
    }
}
