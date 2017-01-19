package com.lakeel.altla.vision.nearby.presentation.presenter.information;

import com.lakeel.altla.vision.nearby.domain.usecase.FindInformationUseCase;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.InformationModelMapper;
import com.lakeel.altla.vision.nearby.presentation.view.InformationView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class InformationPresenter extends BasePresenter<InformationView> {

    @Inject
    FindInformationUseCase findInformationUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(InformationPresenter.class);

    private InformationModelMapper modelMapper = new InformationModelMapper();

    @Inject
    public InformationPresenter() {
    }

    public void onActivityCreated(String informationId) {
        Subscription subscription = findInformationUseCase
                .execute(MyUser.getUid(), informationId)
                .map(entity -> modelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showInformation(model),
                        e -> LOGGER.error("Failed to find information.", e));
        subscriptions.add(subscription);
    }
}
