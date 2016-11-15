package com.lakeel.profile.notification.presentation.presenter.settings;

import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.data.entity.CMLinksEntity;
import com.lakeel.profile.notification.data.entity.ConfigsEntity;
import com.lakeel.profile.notification.domain.usecase.FindBeaconIdUseCase;
import com.lakeel.profile.notification.domain.usecase.FindCMLinksUseCase;
import com.lakeel.profile.notification.domain.usecase.FindConfigsUseCase;
import com.lakeel.profile.notification.domain.usecase.FindLINEUrlUseCase;
import com.lakeel.profile.notification.domain.usecase.SaveCMApiKeyUseCase;
import com.lakeel.profile.notification.domain.usecase.SaveCMJidUseCase;
import com.lakeel.profile.notification.domain.usecase.SaveCMSecretKeyUseCase;
import com.lakeel.profile.notification.domain.usecase.SaveLINEUrlUseCase;
import com.lakeel.profile.notification.presentation.checker.BleState;
import com.lakeel.profile.notification.presentation.checker.BluetoothChecker;
import com.lakeel.profile.notification.presentation.firebase.MyUser;
import com.lakeel.profile.notification.presentation.presenter.BasePresenter;
import com.lakeel.profile.notification.presentation.presenter.mapper.BeaconIdModelMapper;
import com.lakeel.profile.notification.presentation.presenter.mapper.CMLinksModelMapper;
import com.lakeel.profile.notification.presentation.service.PublishService;
import com.lakeel.profile.notification.presentation.view.SettingsView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.content.Context.ACTIVITY_SERVICE;

public final class SettingsPresenter extends BasePresenter<SettingsView> {

    @Inject
    FindBeaconIdUseCase mFindBeaconIdUseCase;

    @Inject
    SaveLINEUrlUseCase mSaveLINEUrlUseCase;

    @Inject
    FindLINEUrlUseCase mFindLINEUrlUseCase;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsPresenter.class);

    private Context mContext;

    private BeaconIdModelMapper mBeaconIdModelMapper = new BeaconIdModelMapper();

    private CMLinksModelMapper mCMLinksModelMapper = new CMLinksModelMapper();

    @Inject
    SettingsPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void onResume() {
        getView().showTitle(R.string.title_settings);

        BluetoothChecker checker = new BluetoothChecker(mContext);
        BleState state = checker.getState();
        if (state == BleState.SUBSCRIBE_ONLY) {
            getView().disablePublishSettings();
        }

        Subscription subscription = mFindLINEUrlUseCase
                .execute(MyUser.getUid())
                .toObservable()
                .filter(entity -> entity != null)
                .map(entity -> entity.url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(url -> getView().showLINEUrl(url),
                        e -> LOGGER.error("Failed to find LINE url.", e));
        mCompositeSubscription.add(subscription);

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

    public void onStartToPublish() {
        Subscription subscription = mFindBeaconIdUseCase
                .execute()
                .map(entity -> mBeaconIdModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().startPublishInService(model),
                        e -> LOGGER.error("Failed to find beacon id", e));
        mCompositeSubscription.add(subscription);
    }

    public void onStopToPublish() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> listServiceInfo = am.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : listServiceInfo) {
            if (runningServiceInfo.service.getClassName().equals(PublishService.class.getName())) {
                Intent intent = new Intent(mContext, PublishService.class);
                mContext.stopService(intent);
            }
        }
    }

    public void onSaveLineUrl(String url) {
        Subscription subscription = mSaveLINEUrlUseCase
                .execute(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                            getView().showLINEUrl(url);
                            getView().showSnackBar(R.string.message_added);
                        },
                        e -> {
                            LOGGER.error("Failed to save line URL.", e);
                            getView().showSnackBar(R.string.error_not_added);
                        });
        mCompositeSubscription.add(subscription);
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
