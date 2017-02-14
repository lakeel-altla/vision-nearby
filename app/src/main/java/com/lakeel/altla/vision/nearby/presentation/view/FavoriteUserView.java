package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteUserModel;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.EstimationTarget;

public interface FavoriteUserView {

    void showTitle(String title);

    void updateModel(FavoriteUserModel model);

    void showLocation(String latitude, String longitude);

    void hideLocation();

    void showSnackBar(@StringRes int resId);

    void showDistanceEstimationFragment(EstimationTarget target);
}
