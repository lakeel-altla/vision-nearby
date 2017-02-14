package com.lakeel.altla.vision.nearby.presentation.presenter.tracking;

import android.os.Bundle;

import com.firebase.geofire.GeoLocation;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindDeviceLocationUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.TrackingModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.TrackingModel;
import com.lakeel.altla.vision.nearby.presentation.view.TrackingView;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.EstimationTarget;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.TrackingBeacon;
import com.lakeel.altla.vision.nearby.presentation.view.intent.GoogleMapIntent;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class TrackingPresenter extends BasePresenter<TrackingView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    FindDeviceLocationUseCase findDeviceLocationUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackingPresenter.class);

    private static final String BUNDLE_TRACKING_BEACON = "trackingBeacon";

    private final ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    private TrackingModel viewModel = new TrackingModel();

    private TrackingBeacon trackingBeacon;

    private boolean isMapReadied;

    private boolean isMenuEnabled;

    @Inject
    TrackingPresenter() {
    }

    public void onCreateView(TrackingView view, Bundle bundle) {
        super.onCreateView(view);
        trackingBeacon = (TrackingBeacon) bundle.getSerializable(BUNDLE_TRACKING_BEACON);
    }

    public void onResume() {
        Subscription subscription = findDeviceLocationUseCase.execute(trackingBeacon.beaconId)
                .toObservable()
                .doOnNext(location -> {
                    if (location == null) {
                        getView().showEmptyView();
                    }
                })
                .filter(location -> location != null)
                .map(TrackingModelMapper::map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    this.viewModel = model;

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
                }, e -> {
                    LOGGER.error("Failed.", e);
                    getView().showSnackBar(R.string.snackBar_error_failed);
                });
        subscriptions.add(subscription);
    }

    public void onStop() {
        subscriptions.unSubscribe();
    }

    public void onMapReady() {
        isMapReadied = true;

        if (viewModel.geoLocation != null) {
            getView().showLocationMap(viewModel.geoLocation);
        }
    }

    public boolean isMenuEnabled() {
        return isMenuEnabled;
    }

    public void onDistanceEstimationMenuClick() {
        analyticsReporter.estimateDistance(trackingBeacon.name);

        ArrayList<String> beaconIds = new ArrayList<>(1);
        beaconIds.add(trackingBeacon.beaconId);

        EstimationTarget target = new EstimationTarget(trackingBeacon.name, beaconIds);
        getView().showDistanceEstimationFragment(target);
    }

    public void onDirectionMenuClick() {
        analyticsReporter.launchGoogleMap();

        GeoLocation geoLocation = viewModel.geoLocation;
        GoogleMapIntent intent = new GoogleMapIntent(geoLocation.latitude, geoLocation.longitude);
        getView().launchGoogleMapApp(intent);
    }
}
