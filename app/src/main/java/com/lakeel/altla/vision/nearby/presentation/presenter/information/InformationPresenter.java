package com.lakeel.altla.vision.nearby.presentation.presenter.information;

import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.domain.usecase.FindInformationUseCase;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.InformationModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.InformationModel;
import com.lakeel.altla.vision.nearby.presentation.view.InformationItemView;
import com.lakeel.altla.vision.nearby.presentation.view.InformationView;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.InformationAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class InformationPresenter extends BasePresenter<InformationView> {

    @Inject
    FindInformationUseCase findInformationUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(InformationPresenter.class);

    private InformationModelMapper modelMapper = new InformationModelMapper();

    private List<InformationModel> models = new ArrayList<>();

    @Inject
    InformationPresenter() {
    }

    public void onActivityCreated() {
        Subscription subscription = findInformationUseCase
                .execute(MyUser.getUid())
                .map(entity -> modelMapper.map(entity))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {
                    this.models.clear();
                    this.models.addAll(models);

                    getView().updateItems();
                }, e -> {
                    LOGGER.error("Failed to find information.", e);
                });
        subscriptions.add(subscription);
    }

    public void onCreateItemView(InformationAdapter.InformationViewHolder viewHolder) {
        InformationItemPresenter itemPresenter = new InformationPresenter.InformationItemPresenter();
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
    }
}
