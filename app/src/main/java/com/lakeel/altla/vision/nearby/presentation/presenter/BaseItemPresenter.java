package com.lakeel.altla.vision.nearby.presentation.presenter;

import com.lakeel.altla.vision.nearby.presentation.view.ItemView;

import android.support.annotation.NonNull;

public abstract class BaseItemPresenter<IV extends ItemView> implements ItemPresenter<IV> {

    private IV mItemView;

    @Override
    public void onCreateItemView(@NonNull IV itemView) {
        mItemView = itemView;
    }

    public IV getItemView() {
        return mItemView;
    }
}