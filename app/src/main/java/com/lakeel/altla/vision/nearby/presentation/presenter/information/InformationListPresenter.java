package com.lakeel.altla.vision.nearby.presentation.presenter.information;

import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindAllInformationUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.InformationModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.InformationModel;
import com.lakeel.altla.vision.nearby.presentation.view.InformationItemView;
import com.lakeel.altla.vision.nearby.presentation.view.InformationListView;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.InformationAdapter;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class InformationListPresenter extends BasePresenter<InformationListView> {

    @Inject
    FindAllInformationUseCase findAllInformationUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(InformationListPresenter.class);

    private final ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    private List<InformationModel> viewModels = new ArrayList<>();

    @Inject
    InformationListPresenter() {
    }

    public void onActivityCreated() {
        Subscription subscription = findAllInformationUseCase.execute()
                .map(InformationModelMapper::map)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {
                    Collections.reverse(models);

                    this.viewModels.clear();
                    this.viewModels.addAll(models);

                    getView().updateItems();
                }, e -> {
                    LOGGER.error("Failed.", e);
                    getView().showSnackBar(R.string.snackBar_error_failed);
                });
        subscriptions.add(subscription);
    }

    public void onStop() {
        subscriptions.unSubscribe();
    }

    public void onCreateItemView(InformationAdapter.InformationItemViewHolder viewHolder) {
        InformationItemPresenter itemPresenter = new InformationListPresenter.InformationItemPresenter();
        itemPresenter.onCreateItemView(viewHolder);
        viewHolder.setItemPresenter(itemPresenter);
    }

    public int getItemCount() {
        return viewModels.size();
    }

    public final class InformationItemPresenter extends BaseItemPresenter<InformationItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(viewModels.get(position));
        }

        public void onClick(String informationId) {
            getView().showInformationFragment(informationId);
        }
    }
}
