package com.lakeel.altla.vision.nearby.presentation.presenter.information;

import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.domain.usecase.FindInformationListUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.InformationModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.InformationModel;
import com.lakeel.altla.vision.nearby.presentation.view.InformationItemView;
import com.lakeel.altla.vision.nearby.presentation.view.InformationListView;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.InformationAdapter;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class InformationListPresenter extends BasePresenter<InformationListView> {

    @Inject
    FindInformationListUseCase findInformationListUseCase;

    private InformationModelMapper modelMapper = new InformationModelMapper();

    private List<InformationModel> models = new ArrayList<>();

    @Inject
    InformationListPresenter() {
    }

    public void onActivityCreated() {
        Subscription subscription = findInformationListUseCase.execute()
                .map(information -> modelMapper.map(information))
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {
                    Collections.reverse(models);

                    this.models.clear();
                    this.models.addAll(models);

                    getView().updateItems();
                }, new ErrorAction<>());
        subscriptions.add(subscription);
    }

    public void onCreateItemView(InformationAdapter.InformationItemViewHolder viewHolder) {
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
