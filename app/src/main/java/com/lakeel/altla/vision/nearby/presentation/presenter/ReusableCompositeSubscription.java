package com.lakeel.altla.vision.nearby.presentation.presenter;

import android.support.annotation.NonNull;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public final class ReusableCompositeSubscription {

    CompositeSubscription mCompositeSubscription;

    public void add(@NonNull Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    public void unsubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = null;
        }
    }
}
