package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteUserModel;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.EstimationTarget;

public interface FavoriteUserView {

    void showTitle(@NonNull String title);

    void updateModel(@NonNull FavoriteUserModel model);

    void showLocation(@NonNull String latitude, @NonNull String longitude);

    void hideLocation();

    void showDistanceEstimationFragment(@NonNull EstimationTarget target);

    void showSnackBar(@StringRes int resId);
}
