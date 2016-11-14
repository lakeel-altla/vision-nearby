package com.lakeel.profile.notification.presentation.presenter.model;

import java.util.Map;

public final class LocationModel {

    public String mLatitude;

    public String mLongitude;

    public LocationTextModel mLocationTextModel;

    public static class LocationTextModel {

        public Map<String, String> mTextMap;
    }
}
