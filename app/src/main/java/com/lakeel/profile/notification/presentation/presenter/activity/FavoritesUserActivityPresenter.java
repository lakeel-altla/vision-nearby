package com.lakeel.profile.notification.presentation.presenter.activity;

import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.data.entity.LINELinksEntity;
import com.lakeel.profile.notification.domain.usecase.FindConfigsUseCase;
import com.lakeel.profile.notification.domain.usecase.FindItemUseCase;
import com.lakeel.profile.notification.domain.usecase.FindLINEUrlUseCase;
import com.lakeel.profile.notification.domain.usecase.FindPresenceUseCase;
import com.lakeel.profile.notification.domain.usecase.SaveUserToCmFavoritesUseCase;
import com.lakeel.profile.notification.presentation.presenter.BasePresenter;
import com.lakeel.profile.notification.presentation.presenter.mapper.ItemModelMapper;
import com.lakeel.profile.notification.presentation.presenter.mapper.PresencesModelMapper;
import com.lakeel.profile.notification.presentation.view.FavoritesUserActivityView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class FavoritesUserActivityPresenter extends BasePresenter<FavoritesUserActivityView> {

    @Inject
    FindPresenceUseCase mFindPresenceUseCase;

    @Inject
    FindItemUseCase mFindItemUseCase;

    @Inject
    SaveUserToCmFavoritesUseCase mSaveUserToCmFavoritesUseCase;

    @Inject
    FindConfigsUseCase mFindConfigsUseCase;

    @Inject
    FindLINEUrlUseCase mFindLINEUrlUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(FavoritesUserActivityPresenter.class);

    private PresencesModelMapper mPresencesModelMapper = new PresencesModelMapper();

    private ItemModelMapper mItemModelMapper = new ItemModelMapper();

    private String mId;

    private boolean mCmLinkEnabled;

    @Inject
    FavoritesUserActivityPresenter() {
    }

    @Override
    public void onResume() {

        // Show presence.
        Subscription subscription1 = mFindPresenceUseCase
                .execute(mId)
                .map(entity -> mPresencesModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showPresence(model),
                        e -> LOGGER.error("Failed to find presence.", e));
        mCompositeSubscription.add(subscription1);

        // Show profile.
        Subscription subscription2 = mFindItemUseCase.
                execute(mId)
                .subscribeOn(Schedulers.io())
                .map(entity -> mItemModelMapper.map(entity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    getView().showTitle(model.mName);
                    getView().showProfile(model);
                }, e -> LOGGER.error("Failed to find item.", e));
        mCompositeSubscription.add(subscription2);

        Subscription subscription3 = mFindConfigsUseCase
                .execute()
                .map(entity -> entity.isCmLinkEnabled)
                .flatMap(new Func1<Boolean, Single<LINELinksEntity>>() {
                    @Override
                    public Single<LINELinksEntity> call(Boolean bool) {
                        mCmLinkEnabled = bool;
                        return mFindLINEUrlUseCase
                                .execute(mId)
                                .subscribeOn(Schedulers.io());
                    }
                })
                .map(lineLinksEntity -> lineLinksEntity.url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lineUrl -> {
                    getView().showLineUrl(lineUrl);
                    getView().initializeOptionMenu();
                }, e -> LOGGER.error("Failed to find config settings.", e));
        mCompositeSubscription.add(subscription3);
    }

    public void setId(String id) {
        mId = id;
    }

    public void onShare() {
        getView().showShareSheet();
    }

    public void onAddToCmFavorites() {
        Subscription subscription = mSaveUserToCmFavoritesUseCase
                .execute(mId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> getView().showSnackBar(R.string.message_added),
                        e -> {
                            LOGGER.error("Failed to add to CM favorites.", e);
                            getView().showSnackBar(R.string.error_not_added);
                        });
        mCompositeSubscription.add(subscription);
    }

    public boolean isCmLinkEnabled() {
        return mCmLinkEnabled;
    }
}
