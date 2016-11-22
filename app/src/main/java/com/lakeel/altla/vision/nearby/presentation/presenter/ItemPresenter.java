package com.lakeel.altla.vision.nearby.presentation.presenter;

import com.lakeel.altla.vision.nearby.presentation.view.ItemView;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

public interface ItemPresenter<IV extends ItemView> {

    void onCreateItemView(@NonNull IV itemView);

    void onBind(@IntRange(from = 0) int position);
}
