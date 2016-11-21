package com.lakeel.profile.notification.presentation.presenter.tracking;

import com.firebase.geofire.GeoLocation;
import com.lakeel.profile.notification.data.entity.LocationsDataEntity;
import com.lakeel.profile.notification.domain.usecase.FindLocationDataUseCase;
import com.lakeel.profile.notification.domain.usecase.FindLocationUseCase;
import com.lakeel.profile.notification.presentation.presenter.BasePresenter;
import com.lakeel.profile.notification.presentation.view.DateFormatter;
import com.lakeel.profile.notification.presentation.view.TrackingView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class TrackingPresenter extends BasePresenter<TrackingView> {

    @Inject
    FindLocationDataUseCase mFindLocationDataUseCase;

    @Inject
    FindLocationUseCase mFindLocationUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackingPresenter.class);

    private String mBeaconId;

    private String mBeaconName;

    private GeoLocation mGeoLocation;

    private boolean mMapReady;

    private long mDetectedTime;

    @Inject
    TrackingPresenter() {
    }

    @Override
    public void onActivityCreated() {
        Subscription subscription = mFindLocationDataUseCase
                .execute(mBeaconId)
                .flatMap(new Func1<LocationsDataEntity, Single<GeoLocation>>() {
                    @Override
                    public Single<GeoLocation> call(LocationsDataEntity entity) {
                        if (entity == null) {
                            return Single.just(null);
                        }

                        mDetectedTime = entity.passingTime;
                        return mFindLocationUseCase
                                .execute(entity.key)
                                .observeOn(Schedulers.io());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(location -> {
                    if (location == null) {
                        getView().showEmptyView();
                        return;
                    }

                    DateFormatter formatter = new DateFormatter(mDetectedTime);
                    String formattedDate = formatter.format();
                    getView().showDetectedDate(formattedDate);

                    mGeoLocation = location;
                    if (mMapReady) {
                        getView().showLocationMap(location);
                    }
                }, e -> LOGGER.error("Failed to find location.", e));

        mCompositeSubscription.add(subscription);
    }

    public void setBeaconData(String beaconId, String beaconName) {
        mBeaconId = beaconId;
        mBeaconName = beaconName;
    }

    public void onMapReady() {
        mMapReady = true;
        if (mGeoLocation != null) {
            getView().showLocationMap(mGeoLocation);
        }
    }

    public void onMarkerClick() {
        getView().showFindNearbyDeviceConfirmDialog();
    }

    public void onFindNearbyDeviceDialogClicked() {
        getView().showFindNearbyDeviceFragment(mBeaconId,mBeaconName);
    }

}
