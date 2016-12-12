package com.lakeel.altla.vision.nearby.presentation.presenter.settings.cm;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.data.entity.CmLinkEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindCmLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindConfigsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveCmApiKeyUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveCmJidUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveCmSecretKeyUseCase;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.CmLinksModelMapper;
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
    FindCmLinkUseCase findCmLinkUseCase;

    @Inject
    SaveCmApiKeyUseCase saveCmApiKeyUseCase;

    @Inject
    SaveCmSecretKeyUseCase saveCmSecretKeyUseCase;

    @Inject
    SaveCmJidUseCase saveCmJidUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(CmSettingsPresenter.class);

    private CmLinksModelMapper CmLinksModelMapper = new CmLinksModelMapper();

    @Inject
    CmSettingsPresenter() {
    }

    public void onActivityCreated() {
        Subscription subscription = findConfigsUseCase
                .execute()
                .toObservable()
                .filter(entity -> entity.isCmLinkEnabled)
                .flatMap(entity -> findCmLink(MyUser.getUid()))
                .map(entity -> CmLinksModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    getView().showCmPreferences(model);
                }, e -> {
                    LOGGER.error("Failed to find CM links.", e);
                });
        subscriptions.add(subscription);
    }

    public void onSaveCmApiKey(String apiKey) {
        Subscription subscription = saveCmApiKeyUseCase
                .execute(MyUser.getUid(), apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    getView().showSnackBar(R.string.message_saved);
                    getView().updateCmApiKeyPreference(apiKey);
                }, e -> {
                    LOGGER.error("Failed to save API key.", e);
                    getView().showSnackBar(R.string.error_not_saved);
                });
        subscriptions.add(subscription);
    }

    public void onSaveCmSecretKey(String secretKey) {
        Subscription subscription = saveCmSecretKeyUseCase
                .execute(MyUser.getUid(), secretKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    getView().showSnackBar(R.string.message_saved);
                    getView().updateCmSecretKeyPreference(secretKey);
                }, e -> {
                    LOGGER.error("Failed to save secret key.", e);
                    getView().showSnackBar(R.string.error_not_saved);
                });
        subscriptions.add(subscription);
    }

    public void onSaveCmJid(String jid) {
        Subscription subscription = saveCmJidUseCase
                .execute(MyUser.getUid(), jid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    getView().showSnackBar(R.string.message_saved);
                    getView().updateCmJidPreference(jid);
                }, e -> {
                    LOGGER.error("Failed to save jid.", e);
                    getView().showSnackBar(R.string.error_not_saved);
                });
        subscriptions.add(subscription);
    }

    Observable<CmLinkEntity> findCmLink(String userId) {
        return findCmLinkUseCase.execute(userId).subscribeOn(Schedulers.io()).toObservable();
    }
}
