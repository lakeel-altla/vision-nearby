package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.ItemsEntity;
import com.lakeel.altla.vision.nearby.data.entity.RecentlyEntity;
import com.lakeel.altla.vision.nearby.data.entity.RecentlyEntity.LocationEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.LocationModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.LocationModel.LocationTextModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.RecentlyItemModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.RecentlyItemModel.Weather;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

public final class RecentlyItemModelMapper {

    @NonNull
    public RecentlyItemModel map(@NonNull ItemsEntity itemsEntity, @NonNull RecentlyEntity recentlyEntity) {
        RecentlyItemModel model = new RecentlyItemModel();

        model.mKey = recentlyEntity.key;
        model.mId = recentlyEntity.id;
        model.mName = itemsEntity.name;
        model.mImageUri = itemsEntity.imageUri;
        model.mUserActivity = recentlyEntity.userActivity;
        model.mPassingTime = recentlyEntity.passingTime;

        LocationEntity locationEntity = recentlyEntity.location;
        if (locationEntity != null) {
            LocationModel locationModel = new LocationModel();
            locationModel.mLatitude = locationEntity.latitude;
            locationModel.mLongitude = locationEntity.longitude;

            Map<String, String> textMap = locationEntity.text;
            if (textMap != null && !textMap.isEmpty()) {
                LocationTextModel locationTextModel = new LocationTextModel();
                locationTextModel.mTextMap = textMap;
                locationModel.mLocationTextModel = locationTextModel;
            }

            model.mLocationModel = locationModel;
        }

        if (recentlyEntity.weather != null) {
            List<Integer> conditionList = recentlyEntity.weather.conditions;
            int[] conditionArray = new int[conditionList.size()];
            for (int count = 0; count < conditionList.size(); count++) {
                conditionArray[count] = conditionList.get(count);
            }
            Weather weather = new Weather();
            weather.mConditions = conditionArray;
            weather.humidity = recentlyEntity.weather.humidity;
            weather.temparature = recentlyEntity.weather.temperature;
            model.mWeather = weather;
        }

        return model;
    }
}
