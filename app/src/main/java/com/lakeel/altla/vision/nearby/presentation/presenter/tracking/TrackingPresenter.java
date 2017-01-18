package com.lakeel.altla.vision.nearby.presentation.presenter.tracking;

import android.os.Bundle;

import com.firebase.geofire.GeoLocation;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.lakeel.altla.vision.nearby.data.entity.LocationDataEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLocationDataUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLocationUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsEvent;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsParam;
import com.lakeel.altla.vision.nearby.presentation.analytics.UserParam;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.intent.GoogleMapDirectionIntent;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.view.TrackingView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class TrackingPresenter extends BasePresenter<TrackingView> {

    @Inject
    FirebaseAnalytics firebaseAnalytics;

    @Inject
    FindLocationDataUseCase findLocationDataUseCase;

    @Inject
    FindLocationUseCase findLocationUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackingPresenter.class);

    private String beaconId;

    private String beaconName;

    private GeoLocation geoLocation;

    private boolean isMapReadied;

    private boolean isMenuEnabled;

    @Inject
    TrackingPresenter() {
    }

    public void setBeaconData(String beaconId, String beaconName) {
        this.beaconId = beaconId;
        this.beaconName = beaconName;
    }

    public void onResume() {
        Subscription subscription = findLocationDataUseCase
                .execute(beaconId)
                .doOnSuccess(entity -> {
                    if (entity != null) getView().showFoundDate(entity.passingTime);
                })
                .flatMap(new Func1<LocationDataEntity, Single<GeoLocation>>() {
                    @Override
                    public Single<GeoLocation> call(LocationDataEntity entity) {
                        if (entity == null) return Single.just(null);
                        return findLocation(entity.uniqueId);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(location -> {
                    if (location == null) {
                        getView().showEmptyView();
                    } else {
                        geoLocation = location;
                        if (isMapReadied) {
                            isMenuEnabled = true;
                            getView().showLocationMap(location);
                            getView().showOptionMenu();
                        }
                    }
                }, e -> LOGGER.error("Failed to find location.", e));
        subscriptions.add(subscription);
    }

    public void onMapReady() {
        isMapReadied = true;
        if (geoLocation != null) {
            getView().showLocationMap(geoLocation);
        }
    }

    public boolean isMenuEnabled() {
        return isMenuEnabled;
    }

    public void onDistanceEstimationMenuClick() {
        // Analytics
        UserParam userParam = new UserParam();
        userParam.putString(AnalyticsParam.TARGET_NAME.getValue(), beaconName);
        firebaseAnalytics.logEvent(AnalyticsEvent.ESTIMATE_DISTANCE.getValue(), userParam.toBundle());

        ArrayList<String> beaconIds = new ArrayList<>();
        beaconIds.add(beaconId);
        getView().showDistanceEstimationFragment(beaconIds, beaconName);
    }

    public void onDirectionMenuClick() {
        MyUser.UserData userData = MyUser.getUserData();
        Bundle bundle = new Bundle();
        bundle.putString(AnalyticsParam.USER_ID.getValue(), userData.userId);
        bundle.getString(AnalyticsParam.USER_NAME.getValue(), userData.userName);
        firebaseAnalytics.logEvent(AnalyticsEvent.LAUNCH_GOOGLE_MAP.getValue(), bundle);

        GoogleMapDirectionIntent intent = new GoogleMapDirectionIntent(String.valueOf(geoLocation.latitude), String.valueOf(geoLocation.longitude));
        getView().launchGoogleMapApp(intent);
    }

    private Single<GeoLocation> findLocation(String uniqueKey) {
        return findLocationUseCase.execute(uniqueKey).subscribeOn(Schedulers.io());
    }
}
