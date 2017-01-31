package com.lakeel.altla.vision.nearby.presentation.presenter.tracking;

import com.firebase.geofire.GeoLocation;
import com.lakeel.altla.vision.nearby.domain.usecase.FindDeviceLocationUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.TrackingModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.TrackingModel;
import com.lakeel.altla.vision.nearby.presentation.view.TrackingView;
import com.lakeel.altla.vision.nearby.presentation.view.intent.GoogleMapIntent;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class TrackingPresenter extends BasePresenter<TrackingView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    FindDeviceLocationUseCase findDeviceLocationUseCase;

    private TrackingModel model = new TrackingModel();

    private TrackingModelMapper modelMapper = new TrackingModelMapper();

    private String beaconId;

    private String beaconName;

    private boolean isMapReadied;

    private boolean isMenuEnabled;

    @Inject
    TrackingPresenter() {
    }

    public void setBeaconIdAndBeaconName(String beaconId, String beaconName) {
        this.beaconId = beaconId;
        this.beaconName = beaconName;
    }

    public void onResume() {
        Subscription subscription = findDeviceLocationUseCase.execute(beaconId)
                .toObservable()
                .doOnNext(location -> {
                    if (location == null) {
                        getView().showEmptyView();
                    }
                })
                .filter(location -> location != null)
                .map(location -> modelMapper.map(location))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    this.model = model;

                    if (model.geoLocation == null) {
                        getView().showEmptyView();
                    } else {
                        getView().showFoundDate(model.foundTime);
                        if (isMapReadied) {
                            isMenuEnabled = true;
                            getView().showLocationMap(model.geoLocation);
                            getView().showOptionMenu();
                        }
                    }
                }, new ErrorAction<>());
        subscriptions.add(subscription);
    }

    public void onMapReady() {
        isMapReadied = true;
        if (model.geoLocation != null) {
            getView().showLocationMap(model.geoLocation);
        }
    }

    public boolean isMenuEnabled() {
        return isMenuEnabled;
    }

    public void onDistanceEstimationMenuClick() {
        analyticsReporter.estimateDistance(beaconName);

        ArrayList<String> beaconIds = new ArrayList<>(1);
        beaconIds.add(beaconId);
        getView().showDistanceEstimationFragment(beaconIds, beaconName);
    }

    public void onDirectionMenuClick() {
        analyticsReporter.launchGoogleMap();

        GeoLocation geoLocation = model.geoLocation;
        GoogleMapIntent intent = new GoogleMapIntent(geoLocation.latitude, geoLocation.longitude);
        getView().launchGoogleMapApp(intent);
    }
}
