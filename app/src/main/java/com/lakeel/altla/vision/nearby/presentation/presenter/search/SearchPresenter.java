package com.lakeel.altla.vision.nearby.presentation.presenter.search;

import com.lakeel.altla.vision.nearby.domain.usecase.FindItemNameUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.UserModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.UserModel;
import com.lakeel.altla.vision.nearby.presentation.view.SearchItemView;
import com.lakeel.altla.vision.nearby.presentation.view.SearchView;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.SearchAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.IntRange;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class SearchPresenter extends BasePresenter<SearchView> {

    @Inject
    FindItemNameUseCase mFindItemNameUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchPresenter.class);

    private UserModelMapper mMapper = new UserModelMapper();

    private List<UserModel> mUserModels = new ArrayList<>();

    @Inject
    SearchPresenter() {
    }

    public void onSearch(String query) {
        Subscription subscription = mFindItemNameUseCase
                .execute(query)
                .map(entity -> mMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    mUserModels.clear();
                    mUserModels.add(model);
                    LOGGER.debug("Searched user is " + model.name);
                }, e -> LOGGER.error("Failed to search.", e));
        reusableCompositeSubscription.add(subscription);
    }

    public int getItemCount() {
        return mUserModels.size();
    }

    public void onCreateItemView(SearchAdapter.SearchViewHolder viewHolder) {
        SearchItemPresenter itemPresenter = new SearchItemPresenter();
        itemPresenter.onCreateItemView(viewHolder);
        viewHolder.setItemPresenter(itemPresenter);
    }

    public class SearchItemPresenter extends BaseItemPresenter<SearchItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(mUserModels.get(position));
        }

        public void onClick(UserModel model) {
            getView().showTrackingFragment(model.userId);
        }
    }
}
