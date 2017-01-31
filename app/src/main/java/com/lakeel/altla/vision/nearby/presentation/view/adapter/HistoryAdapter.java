package com.lakeel.altla.vision.nearby.presentation.view.adapter;

import android.support.annotation.IntRange;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.presentation.presenter.history.HistoryListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.HistoryModel;
import com.lakeel.altla.vision.nearby.presentation.view.HistoryItemView;
import com.lakeel.altla.vision.nearby.presentation.view.date.DateFormatter;
import com.lakeel.altla.vision.nearby.presentation.view.drawable.UserInitial;
import com.marshalchen.ultimaterecyclerview.SwipeableUltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class HistoryAdapter extends SwipeableUltimateViewAdapter<HistoryModel> {

    private HistoryListPresenter historyListPresenter;

    public HistoryAdapter(HistoryListPresenter historyListPresenter) {
        super(new ArrayList<>());
        this.historyListPresenter = historyListPresenter;
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.item_history;
    }

    @Override
    protected UltimateRecyclerviewViewHolder newViewHolder(View view) {
        HistoryItemViewHolder viewHolder = new HistoryItemViewHolder(view, true);
        historyListPresenter.onCreateItemView(viewHolder);
        return viewHolder;
    }

    @Override
    protected void withBindHolder(UltimateRecyclerviewViewHolder bindHolder, HistoryModel model, int position) {
        HistoryItemViewHolder holder = (HistoryItemViewHolder) bindHolder;
        holder.onBind(position);
    }

    static class HistoryItemViewHolder extends UltimateRecyclerviewViewHolder implements HistoryItemView {

        @BindView(R.id.layoutItem)
        LinearLayout itemLayout;

        @BindView(R.id.textViewUserName)
        TextView userNameTextView;

        @BindView(R.id.imageViewUser)
        ImageView userImageView;

        @BindView(R.id.textViewPassingTime)
        TextView passingTimeTextView;

        @BindView(R.id.buttonRemove)
        Button removeButton;

        @BindView(R.id.swipeLayout)
        SwipeLayout swipeLayout;

        private HistoryListPresenter.HistoryItemPresenter itemPresenter;

        HistoryItemViewHolder(View itemView, boolean bind) {
            super(itemView);
            if (bind) {
                ButterKnife.bind(this, itemView);
                swipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);
                swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            }
        }

        @Override
        public void setItemPresenter(HistoryListPresenter.HistoryItemPresenter itemPresenter) {
            this.itemPresenter = itemPresenter;
        }

        @Override
        public void showItem(HistoryModel model) {
            String userName = model.userName;
            String imageUri = model.imageUri;

            userNameTextView.setText(userName);

            if (StringUtils.isEmpty(imageUri)) {
                userImageView.post(() -> {
                    UserInitial initial = new UserInitial(userName);
                    userImageView.setImageDrawable(initial.getDrawable());
                });
            } else {
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(imageUri, userImageView);
            }

            DateFormatter dateFormatter = new DateFormatter(model.passingTime);
            passingTimeTextView.setText(dateFormatter.format());

            itemLayout.setOnClickListener(view -> itemPresenter.onClick(model));
            removeButton.setOnClickListener(v -> itemPresenter.onRemove(model));
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            itemPresenter.onBind(position);
        }
    }
}
