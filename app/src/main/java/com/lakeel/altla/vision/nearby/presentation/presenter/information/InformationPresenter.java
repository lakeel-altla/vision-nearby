package com.lakeel.altla.vision.nearby.presentation.presenter.information;

import com.lakeel.altla.vision.nearby.domain.usecase.FindInformationUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.InformationModelMapper;
import com.lakeel.altla.vision.nearby.presentation.view.InformationView;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class InformationPresenter extends BasePresenter<InformationView> {

    @Inject
    FindInformationUseCase findInformationUseCase;

    private InformationModelMapper modelMapper = new InformationModelMapper();

    @Inject
    InformationPresenter() {
    }

    public void onActivityCreated(String informationId) {
        Subscription subscription = findInformationUseCase.execute(informationId)
                .map(entity -> modelMapper.map(entity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showInformation(model), new ErrorAction<>());
        subscriptions.add(subscription);
    }
}
