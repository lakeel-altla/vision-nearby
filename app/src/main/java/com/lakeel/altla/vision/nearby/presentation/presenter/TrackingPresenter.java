package com.lakeel.altla.vision.nearby.presentation.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindDeviceLocationUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.TrackingModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.TrackingModel;
import com.lakeel.altla.vision.nearby.presentation.view.TrackingView;
import com.lakeel.altla.vision.nearby.presentation.view.date.DateFormatter;
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

    private TrackingModel model;

    private TrackingBeacon trackingBeacon;

    private boolean isMapReadied;

    private boolean isMenuEnabled;

    @Inject
    TrackingPresenter() {
    }

    public void onCreateView(@NonNull TrackingView view, @NonNull Bundle bundle) {
        super.onCreateView(view);
        trackingBeacon = (TrackingBeacon) bundle.getSerializable(BUNDLE_TRACKING_BEACON);
    }

    public void onResume() {
        Subscription subscription = findDeviceLocationUseCase
                .execute(trackingBeacon.id)
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
                    this.model = model;

                    if (model.geoLocation == null) {
                        getView().showEmptyView();
                    } else {
                        DateFormatter formatter = new DateFormatter(model.foundTime);
                        String dateText = formatter.format();
                        getView().showFoundDate(dateText);

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

        if (model != null && model.geoLocation != null) {
            getView().showLocationMap(model.geoLocation);
        }
    }

    public boolean isMenuEnabled() {
        return isMenuEnabled;
    }

    public void onDistanceEstimationMenuClick() {
        analyticsReporter.estimateDistance(trackingBeacon.name);

        ArrayList<String> beaconIds = new ArrayList<>(1);
        beaconIds.add(trackingBeacon.id);

        EstimationTarget target = new EstimationTarget(trackingBeacon.name, beaconIds);
        getView().showDistanceEstimationFragment(target);
    }

    public void onDirectionMenuClick() {
        analyticsReporter.launchGoogleMap();

        GoogleMapIntent intent = new GoogleMapIntent(model.geoLocation.latitude, model.geoLocation.longitude);
        getView().launchGoogleMapApp(intent);
    }
}
