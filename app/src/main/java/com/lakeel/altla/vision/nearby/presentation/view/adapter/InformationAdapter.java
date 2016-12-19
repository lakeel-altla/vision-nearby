package com.lakeel.altla.vision.nearby.presentation.view.adapter;

import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.information.InformationPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.InformationModel;
import com.lakeel.altla.vision.nearby.presentation.view.DateFormatter;
import com.lakeel.altla.vision.nearby.presentation.view.InformationItemView;
import com.lakeel.altla.vision.nearby.presentation.view.animation.ExpandAnimation;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.InformationViewHolder> {

    public InformationPresenter presenter;

    public InformationAdapter(InformationPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public InformationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.information_item, parent, false);
        InformationAdapter.InformationViewHolder viewHolder = new InformationAdapter.InformationViewHolder(itemView);
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

    public final class InformationViewHolder extends RecyclerView.ViewHolder implements InformationItemView, View.OnClickListener {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.postTime)
        TextView postTime;

        @BindView(R.id.subItem)
        LinearLayout subView;

        @BindView(R.id.message)
        TextView message;

        private ExpandAnimation expandAnimation;

        private boolean isViewExpanded = false;

        private InformationPresenter.InformationItemPresenter itemPresenter;

        public InformationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);

            expandAnimation = new ExpandAnimation(subView);
        }

        @Override
        public void setItemPresenter(InformationPresenter.InformationItemPresenter itemPresenter) {
            this.itemPresenter = itemPresenter;
        }

        @Override
        public void showItem(InformationModel model) {
            title.setText(model.title);

            DateFormatter formatter = new DateFormatter(model.postTime);
            String postTimeText = formatter.format();
            postTime.setText(postTimeText);

            message.setText(model.message);
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            itemPresenter.onBind(position);
        }

        @Override
        public void onClick(View view) {
            if (isViewExpanded) {
                expandAnimation.close();
            } else {
                expandAnimation.show();
            }
            isViewExpanded = !isViewExpanded;
        }
    }
}
