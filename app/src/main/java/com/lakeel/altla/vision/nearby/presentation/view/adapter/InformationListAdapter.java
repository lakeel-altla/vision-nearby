package com.lakeel.altla.vision.nearby.presentation.view.adapter;

import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.information.InformationListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.InformationModel;
import com.lakeel.altla.vision.nearby.presentation.view.date.DateFormatter;
import com.lakeel.altla.vision.nearby.presentation.view.InformationItemView;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class InformationListAdapter extends RecyclerView.Adapter<InformationListAdapter.InformationViewHolder> {

    public InformationListPresenter presenter;

    public InformationListAdapter(InformationListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public InformationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.information_item, parent, false);
        InformationListAdapter.InformationViewHolder viewHolder = new InformationListAdapter.InformationViewHolder(itemView);
        presenter.onCreateItemView(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InformationViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }

    public final class InformationViewHolder extends RecyclerView.ViewHolder implements InformationItemView {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.postTime)
        TextView postTime;

        private View itemView;

        private InformationListPresenter.InformationItemPresenter itemPresenter;

        InformationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.itemView = itemView;
        }

        @Override
        public void setItemPresenter(InformationListPresenter.InformationItemPresenter itemPresenter) {
            this.itemPresenter = itemPresenter;
        }

        @Override
        public void showItem(InformationModel model) {
            title.setText(model.title);

            DateFormatter formatter = new DateFormatter(model.postTime);
            String postTimeText = formatter.format();
            postTime.setText(postTimeText);

            itemView.setOnClickListener(view -> itemPresenter.onClick(model.informationId));
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            itemPresenter.onBind(position);
        }
    }
}
