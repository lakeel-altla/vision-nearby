package com.lakeel.altla.vision.nearby.presentation.presenter.history;

import android.os.Build;
import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.CollectionUtils;
import com.lakeel.altla.vision.nearby.domain.usecase.FindAllNearbyHistoryUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.RemoveNearbyHistoryUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.HistoryModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyHistoryModel;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyHistoryItemView;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyHistoryListView;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class NearbyHistoryListPresenter extends BasePresenter<NearbyHistoryListView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    FindAllNearbyHistoryUseCase findAllNearbyHistoryUseCase;

    @Inject
    RemoveNearbyHistoryUseCase removeNearbyHistoryUseCase;

    private final ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    private HistoryModelMapper modelMapper = new HistoryModelMapper();

    private final List<NearbyHistoryModel> viewModels = new ArrayList<>();

    @Inject
    NearbyHistoryListPresenter() {
    }

    public void onActivityCreated() {
        Subscription subscription = findAllNearbyHistoryUseCase.execute()
                .map(nearbyHistoryUserProfile -> modelMapper.map(nearbyHistoryUserProfile))
                .toSortedList((model1, model2) -> sortByLatest(model1.passingTime, model2.passingTime))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    viewModels.clear();
                    viewModels.addAll(model);

                    if (CollectionUtils.isEmpty(model)) {
                        getView().showEmptyView();
                    } else {
                        getView().hideEmptyView();
                    }

                    getView().updateItems();
                }, new ErrorAction<>());
        subscriptions.add(subscription);
    }

    public void onStop() {
        subscriptions.unSubscribe();
    }

    public void onCreateItemView(NearbyHistoryItemView nearbyHistoryItemView) {
        HistoryItemPresenter itemPresenter = new HistoryItemPresenter();
        itemPresenter.onCreateItemView(nearbyHistoryItemView);
        nearbyHistoryItemView.setItemPresenter(itemPresenter);
    }

    public List<NearbyHistoryModel> getItems() {
        return viewModels;
    }

    public final class HistoryItemPresenter extends BaseItemPresenter<NearbyHistoryItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(viewModels.get(position));
        }

        public void onClick(NearbyHistoryModel model) {
            getView().showPassingUserFragment(model.historyId);
        }

        public void onRemove(NearbyHistoryModel model) {
            analyticsReporter.removeHistory(model.userId, model.userName);

            Subscription subscription = removeNearbyHistoryUseCase.execute(model.historyId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ErrorAction<>(),
                            () -> {
                                int size = viewModels.size();

                                viewModels.remove(model);

                                if (CollectionUtils.isEmpty(viewModels)) {
                                    getView().removeAll(size);
                                    getView().showEmptyView();
                                } else {
                                    getView().hideEmptyView();
                                    getView().updateItems();
                                }

                                getView().showSnackBar(R.string.snackBar_message_removed);
                            });
            subscriptions.add(subscription);
        }
    }

    private int sortByLatest(long value1, long value2) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return Long.compare(value2, value1);
        } else {
            return Long.valueOf(value2).compareTo(value1);
        }
    }
}
