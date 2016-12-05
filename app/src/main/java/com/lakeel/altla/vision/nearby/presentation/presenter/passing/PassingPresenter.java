package com.lakeel.altla.vision.nearby.presentation.presenter.passing;

import com.lakeel.altla.vision.nearby.domain.usecase.FindHistoryUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindTimesUseCase;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.PassingModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.LocationModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PassingModel;
import com.lakeel.altla.vision.nearby.presentation.view.PassingView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class PassingPresenter extends BasePresenter<PassingView> {

    @Inject
    FindHistoryUseCase findHistoryUseCase;

    @Inject
    FindTimesUseCase findTimesUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(PassingPresenter.class);

    private PassingModelMapper modelMapper = new PassingModelMapper();

    private String uniqueKey;

    private String otherUserId;

    private PassingModel passingModel;

    @Inject
    PassingPresenter() {
    }

    public void setRecentlyUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public void onMapReady() {
        if (passingModel == null) {
            getView().hideLocation();
            return;
        }

        LocationModel locationModel = passingModel.locationModel;
        String latitude = locationModel.latitude;
        String longitude = locationModel.longitude;

        if (latitude == null && longitude == null) {
            getView().hideLocation();
        } else {
            getView().showLocationMap(latitude, longitude);
        }
    }

    @Override
    public void onActivityCreated() {
        Subscription subscription = findHistoryUseCase
                .execute(MyUser.getUid(), uniqueKey)
                .map(entity -> modelMapper.map(entity))
                .doOnSuccess(model -> {
                    passingModel = model;
                    getView().showPassingData(model);
                })
                .flatMap(model -> findTimesUseCase.execute(MyUser.getUid(), otherUserId).subscribeOn(Schedulers.io()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(time -> getView().showTimes(time),
                        e -> LOGGER.error("Failed to find passing data.", e));
        reusableCompositeSubscription.add(subscription);
    }
}
