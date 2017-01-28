package com.lakeel.altla.vision.nearby.presentation.presenter.history;

import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.CollectionUtils;
import com.lakeel.altla.vision.nearby.domain.usecase.FindHistoryListUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.RemoveHistoryUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.HistoryModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.HistoryModel;
import com.lakeel.altla.vision.nearby.presentation.view.HistoryItemView;
import com.lakeel.altla.vision.nearby.presentation.view.HistoryListView;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class HistoryListPresenter extends BasePresenter<HistoryListView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    FindHistoryListUseCase findHistoryListUseCase;

    @Inject
    RemoveHistoryUseCase removeHistoryUseCase;

    private HistoryModelMapper modelMapper = new HistoryModelMapper();

    private final List<HistoryModel> historyModels = new ArrayList<>();

    @Inject
    HistoryListPresenter() {
    }

    public void onActivityCreated() {
        Subscription subscription = findHistoryListUseCase.execute()
                .map(historyUser -> modelMapper.map(historyUser))
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(historyItemModels -> {
                    Collections.reverse(historyItemModels);

                    historyModels.clear();
                    historyModels.addAll(historyItemModels);

                    if (CollectionUtils.isEmpty(historyItemModels)) {
                        getView().showEmptyView();
                    } else {
                        getView().hideEmptyView();
                    }

                    getView().updateItems();
                }, new ErrorAction<>());
        subscriptions.add(subscription);
    }

    public void onCreateItemView(HistoryItemView historyItemView) {
        HistoryItemPresenter itemPresenter = new HistoryItemPresenter();
        itemPresenter.onCreateItemView(historyItemView);
        historyItemView.setItemPresenter(itemPresenter);
    }

    public List<HistoryModel> getItems() {
        return historyModels;
    }

    public final class HistoryItemPresenter extends BaseItemPresenter<HistoryItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(historyModels.get(position));
        }

        public void onClick(HistoryModel model) {
            getView().showPassingUserFragment(model.historyId);
        }

        public void onRemove(HistoryModel model) {
            analyticsReporter.removeHistory(model.userId, model.userName);

            Subscription subscription = removeHistoryUseCase.execute(model.historyId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ErrorAction<>(),
                            () -> {
                                int size = historyModels.size();

                                historyModels.remove(model);

                                if (CollectionUtils.isEmpty(historyModels)) {
                                    getView().removeAll(size);
                                    getView().showEmptyView();
                                } else {
                                    getView().hideEmptyView();
                                    getView().updateItems();
                                }

                                getView().showSnackBar(R.string.message_removed);
                            });
            subscriptions.add(subscription);
        }
    }
}
