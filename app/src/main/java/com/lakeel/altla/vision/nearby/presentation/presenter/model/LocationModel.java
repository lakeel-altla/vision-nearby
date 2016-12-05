package com.lakeel.altla.vision.nearby.presentation.presenter.model;

import java.util.Map;

public final class LocationModel {

    public String latitude;

    public String longitude;

    public LocationTextModel mLocationTextModel;

    public static class LocationTextModel {

        public Map<String, String> mTextMap;
    }
}
