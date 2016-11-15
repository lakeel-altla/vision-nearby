package com.lakeel.profile.notification.presentation.presenter.cm;

import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.data.entity.CMLinksEntity;
import com.lakeel.profile.notification.data.entity.ConfigsEntity;
import com.lakeel.profile.notification.domain.usecase.FindCMLinksUseCase;
import com.lakeel.profile.notification.domain.usecase.FindConfigsUseCase;
import com.lakeel.profile.notification.domain.usecase.SaveCMApiKeyUseCase;
import com.lakeel.profile.notification.domain.usecase.SaveCMJidUseCase;
import com.lakeel.profile.notification.domain.usecase.SaveCMSecretKeyUseCase;
import com.lakeel.profile.notification.presentation.presenter.BasePresenter;
import com.lakeel.profile.notification.presentation.presenter.mapper.CMLinksModelMapper;
import com.lakeel.profile.notification.presentation.view.CmSettingsView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class CmSettingsPresenter extends BasePresenter<CmSettingsView> {

    @Inject
    FindConfigsUseCase mFindConfigsUseCase;

    @Inject
    FindCMLinksUseCase mFindCMLinksUseCase;

    @Inject
    SaveCMApiKeyUseCase mSaveCMApiKeyUseCase;

    @Inject
    SaveCMSecretKeyUseCase mSaveCMSecretKeyUseCase;

    @Inject
    SaveCMJidUseCase mSaveCMJidUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(CmSettingsPresenter.class);

    private CMLinksModelMapper mCMLinksModelMapper = new CMLinksModelMapper();

    @Inject
    CmSettingsPresenter() {
    }

    @Override
    public void onActivityCreated() {
        Subscription subscription1 = mFindConfigsUseCase
                .execute()
                .toObservable()
                .filter(entity -> entity.isCmLinkEnabled)
                .flatMap(new Func1<ConfigsEntity, Observable<CMLinksEntity>>() {
                    @Override
                    public Observable<CMLinksEntity> call(ConfigsEntity entity) {
                        return mFindCMLinksUseCase.
                                execute()
                                .subscribeOn(Schedulers.io())
                                .toObservable();
                    }
                })
                .map(entity -> mCMLinksModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    getView().showCMPreferences(model);
                }, e -> {
                    LOGGER.error("Failed to find CM links settings.", e);
                });
        mCompositeSubscription.add(subscription1);
    }

    public void onSaveCMApiKey(String apiKey) {
        Subscription subscription = mSaveCMApiKeyUseCase
                .execute(apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    getView().showSnackBar(R.string.message_saved);
                    getView().updateCMApiKeyPreference(apiKey);
                }, e -> {
                    LOGGER.error("Failed to save CM api key.", e);
                    getView().showSnackBar(R.string.error_not_saved);
                });
        mCompositeSubscription.add(subscription);
    }

    public void onSaveCMSecretKey(String secretKey) {
        Subscription subscription = mSaveCMSecretKeyUseCase
                .execute(secretKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    getView().showSnackBar(R.string.message_saved);
                    getView().updateCMSecretKeyPreference(secretKey);
                }, e -> {
                    LOGGER.error("Failed to save CM secret key.", e);
                    getView().showSnackBar(R.string.error_not_saved);
                });
        mCompositeSubscription.add(subscription);
    }

    public void onSaveCMJid(String jid) {
        Subscription subscription = mSaveCMJidUseCase
                .execute(jid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    getView().showSnackBar(R.string.message_saved);
                    getView().updateCMJidPreference(jid);
                }, e -> {
                    LOGGER.error("Failed to save CM JID.", e);
                    getView().showSnackBar(R.string.error_not_saved);
                });
        mCompositeSubscription.add(subscription);
    }
}
