package com.lakeel.altla.vision.nearby.presentation.presenter.information;

import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.domain.usecase.FindInformationListUseCase;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.InformationModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.InformationModel;
import com.lakeel.altla.vision.nearby.presentation.view.InformationItemView;
import com.lakeel.altla.vision.nearby.presentation.view.InformationListView;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.InformationListAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class InformationListPresenter extends BasePresenter<InformationListView> {

    @Inject
    FindInformationListUseCase findInformationListUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(InformationListPresenter.class);

    private InformationModelMapper modelMapper = new InformationModelMapper();

    private List<InformationModel> models = new ArrayList<>();

    @Inject
    InformationListPresenter() {
    }

    public void onActivityCreated() {
        Subscription subscription = findInformationListUseCase
                .execute(MyUser.getUid())
                .map(entity -> modelMapper.map(entity))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {
                    Collections.reverse(models);

                    this.models.clear();
                    this.models.addAll(models);

                    getView().updateItems();
                }, e -> {
                    LOGGER.error("Failed to findList information.", e);
                });
        subscriptions.add(subscription);
    }

    public void onCreateItemView(InformationListAdapter.InformationViewHolder viewHolder) {
        InformationItemPresenter itemPresenter = new InformationListPresenter.InformationItemPresenter();
        itemPresenter.onCreateItemView(viewHolder);
        viewHolder.setItemPresenter(itemPresenter);
    }

    public int getItemCount() {
        return models.size();
    }

    public final class InformationItemPresenter extends BaseItemPresenter<InformationItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(models.get(position));
        }

        public void onClick(String informationId) {
            getView().showInformationFragment(informationId);
        }
    }
}
