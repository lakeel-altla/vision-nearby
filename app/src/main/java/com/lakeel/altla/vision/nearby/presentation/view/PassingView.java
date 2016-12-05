package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.PassingModel;

public interface PassingView {

    void showPassingData(PassingModel model);

    void showTimes(long times);

    void hideLocation();

    void showLocationMap(String latitude, String longitude);
}
