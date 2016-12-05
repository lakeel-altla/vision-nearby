package com.lakeel.altla.vision.nearby.presentation.presenter.settings.cm;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.data.entity.CMLinkEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindCMLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindConfigsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveCMApiKeyUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveCMJidUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveCMSecretKeyUseCase;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.CMLinksModelMapper;
import com.lakeel.altla.vision.nearby.presentation.view.CmSettingsView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class CmSettingsPresenter extends BasePresenter<CmSettingsView> {

    @Inject
    FindConfigsUseCase findConfigsUseCase;

    @Inject
    FindCMLinkUseCase findCMLinkUseCase;

    @Inject
    SaveCMApiKeyUseCase saveCMApiKeyUseCase;

    @Inject
    SaveCMSecretKeyUseCase saveCMSecretKeyUseCase;

    @Inject
    SaveCMJidUseCase saveCMJidUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(CmSettingsPresenter.class);

    private CMLinksModelMapper CMLinksModelMapper = new CMLinksModelMapper();

    @Inject
    CmSettingsPresenter() {
    }

    @Override
    public void onActivityCreated() {
        Subscription subscription = findConfigsUseCase
                .execute()
                .toObservable()
                .filter(entity -> entity.isCmLinkEnabled)
                .flatMap(entity -> findCMLink(MyUser.getUid()))
                .map(entity -> CMLinksModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    getView().showCMPreferences(model);
                }, e -> {
                    LOGGER.error("Failed to find CM links.", e);
                });
        reusableCompositeSubscription.add(subscription);
    }

    public void onSaveCMApiKey(String apiKey) {
        Subscription subscription = saveCMApiKeyUseCase
                .execute(MyUser.getUid(), apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    getView().showSnackBar(R.string.message_saved);
                    getView().updateCMApiKeyPreference(apiKey);
                }, e -> {
                    LOGGER.error("Failed to save API key.", e);
                    getView().showSnackBar(R.string.error_not_saved);
                });
        reusableCompositeSubscription.add(subscription);
    }

    public void onSaveCMSecretKey(String secretKey) {
        Subscription subscription = saveCMSecretKeyUseCase
                .execute(MyUser.getUid(), secretKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    getView().showSnackBar(R.string.message_saved);
                    getView().updateCMSecretKeyPreference(secretKey);
                }, e -> {
                    LOGGER.error("Failed to save secret key.", e);
                    getView().showSnackBar(R.string.error_not_saved);
                });
        reusableCompositeSubscription.add(subscription);
    }

    public void onSaveCMJid(String jid) {
        Subscription subscription = saveCMJidUseCase
                .execute(MyUser.getUid(), jid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    getView().showSnackBar(R.string.message_saved);
                    getView().updateCMJidPreference(jid);
                }, e -> {
                    LOGGER.error("Failed to save jid.", e);
                    getView().showSnackBar(R.string.error_not_saved);
                });
        reusableCompositeSubscription.add(subscription);
    }

    Observable<CMLinkEntity> findCMLink(String userId) {
        return findCMLinkUseCase.execute(userId).subscribeOn(Schedulers.io()).toObservable();
    }
}
