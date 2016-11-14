package com.lakeel.profile.notification.presentation.presenter.search;

import com.lakeel.profile.notification.domain.usecase.FindItemNameUseCase;
import com.lakeel.profile.notification.presentation.presenter.BaseItemPresenter;
import com.lakeel.profile.notification.presentation.presenter.BasePresenter;
import com.lakeel.profile.notification.presentation.presenter.mapper.ItemModelMapper;
import com.lakeel.profile.notification.presentation.presenter.model.ItemModel;
import com.lakeel.profile.notification.presentation.view.SearchItemView;
import com.lakeel.profile.notification.presentation.view.SearchView;
import com.lakeel.profile.notification.presentation.view.adapter.SearchAdapter;

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

    private ItemModelMapper mMapper = new ItemModelMapper();

    private List<ItemModel> mItemModels = new ArrayList<>();

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
                    mItemModels.clear();
                    mItemModels.add(model);
                    LOGGER.debug("Searched user is " + model.mName);
                }, e -> LOGGER.error("Failed to search.", e));
        mCompositeSubscription.add(subscription);
    }

    public int getItemCount() {
        return mItemModels.size();
    }

    public void onCreateItemView(SearchAdapter.SearchViewHolder viewHolder) {
        SearchItemPresenter itemPresenter = new SearchItemPresenter();
        itemPresenter.onCreateItemView(viewHolder);
        viewHolder.setItemPresenter(itemPresenter);
    }

    public class SearchItemPresenter extends BaseItemPresenter<SearchItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(mItemModels.get(position));
        }

        public void onClick(ItemModel model) {
            getView().showTrackingFragment(model.mId);
        }
    }
}
