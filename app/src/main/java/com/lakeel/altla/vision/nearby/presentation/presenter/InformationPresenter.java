package com.lakeel.altla.vision.nearby.presentation.presenter;

import android.os.Bundle;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindInformationUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.InformationModelMapper;
import com.lakeel.altla.vision.nearby.presentation.view.InformationView;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class InformationPresenter extends BasePresenter<InformationView> {

    @Inject
    FindInformationUseCase findInformationUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(InformationPresenter.class);

    private static final String BUNDLE_INFORMATION_ID = "informationId";

    private final ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    private String informationId;

    @Inject
    InformationPresenter() {
    }

    public void onCreateView(InformationView view, Bundle bundle) {
        super.onCreateView(view);
        informationId = bundle.getString(BUNDLE_INFORMATION_ID);
    }

    public void onActivityCreated() {
        Subscription subscription = findInformationUseCase
                .execute(informationId)
                .map(InformationModelMapper::map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showInformation(model),
                        e -> {
                            LOGGER.error("Failed.", e);
                            getView().showSnackBar(R.string.snackBar_error_failed);
                        });
        subscriptions.add(subscription);
    }

    public void onStop() {
        subscriptions.unSubscribe();
    }
}
