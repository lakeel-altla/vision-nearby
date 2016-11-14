package com.lakeel.profile.notification.presentation.presenter.tracking;

import com.firebase.geofire.GeoLocation;
import com.lakeel.profile.notification.data.entity.LocationsDataEntity;
import com.lakeel.profile.notification.domain.usecase.FindLocationDataUseCase;
import com.lakeel.profile.notification.domain.usecase.FindLocationUseCase;
import com.lakeel.profile.notification.presentation.presenter.BasePresenter;
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

    private String mId;

    private GeoLocation mGeoLocation;

    private boolean isMapReady;

    @Inject
    TrackingPresenter() {
    }

    @Override
    public void onResume() {
        Subscription subscription = mFindLocationDataUseCase
                .execute(mId)
                .flatMap(new Func1<LocationsDataEntity, Single<GeoLocation>>() {
                    @Override
                    public Single<GeoLocation> call(LocationsDataEntity entity) {
                        return mFindLocationUseCase.execute(entity.key).observeOn(Schedulers.io());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(location -> {
                    mGeoLocation = location;
                    if (isMapReady) {
                        getView().showLocationMap(location);
                    }
                }, e -> LOGGER.error("Failed to find location.", e));
        mCompositeSubscription.add(subscription);
    }

    public void setId(String id) {
        mId = id;
    }


    public void onMapReady() {
        isMapReady = true;
        if (mGeoLocation != null) {
            getView().showLocationMap(mGeoLocation);
        }
    }
}
