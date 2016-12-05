package com.lakeel.altla.vision.nearby.presentation.view.adapter;

import android.graphics.Color;
import android.support.annotation.IntRange;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.presentation.presenter.history.HistoryListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.HistoryModel;
import com.lakeel.altla.vision.nearby.presentation.view.DateFormatter;
import com.lakeel.altla.vision.nearby.presentation.view.HistoryItemView;
import com.marshalchen.ultimaterecyclerview.SwipeableUltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class RecentlyAdapter extends SwipeableUltimateViewAdapter<HistoryModel> {

    private HistoryListPresenter historyListPresenter;

    public RecentlyAdapter(HistoryListPresenter historyListPresenter) {
        super(new ArrayList<>());
        this.historyListPresenter = historyListPresenter;
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.recently_item;
    }

    @Override
    protected UltimateRecyclerviewViewHolder newViewHolder(View view) {
        HistoryItemsViewHolder viewHolder = new HistoryItemsViewHolder(view, true);
        historyListPresenter.onCreateItemView(viewHolder);
        return viewHolder;
    }

    @Override
    protected void withBindHolder(UltimateRecyclerviewViewHolder bindHolder, HistoryModel model, int position) {
        HistoryItemsViewHolder holder = (HistoryItemsViewHolder) bindHolder;
        holder.onBind(position);
    }

    public static class HistoryItemsViewHolder extends UltimateRecyclerviewViewHolder implements HistoryItemView {

        @BindView(R.id.layout_row)
        LinearLayout itemLayout;

        @BindView(R.id.textView_user_item)
        TextView userName;

        @BindView(R.id.imageView_user_profile)
        ImageView userImage;

        @BindView(R.id.timestamp)
        TextView passingTime;

        @BindView(R.id.button_add)
        Button addButton;

        @BindView(R.id.swipe_layout)
        SwipeLayout swipeLayout;

        private HistoryListPresenter.HistoryItemPresenter itemPresenter;

        public HistoryItemsViewHolder(View itemView, boolean bind) {
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
            String id = model.userId;
            String userName = model.name;
            String imageUri = model.imageUri;

            this.userName.setText(userName);

            String initial = userName.substring(0, 1);
            if (StringUtils.isEmpty(imageUri)) {
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(initial, Color.RED);
                userImage.post(() -> userImage.setImageDrawable(drawable));
            } else {
                // Show profile on async.
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(imageUri, userImage);
            }

            DateFormatter dateFormatter = new DateFormatter(model.passingTime);
            passingTime.setText(dateFormatter.format());

            itemLayout.setOnClickListener(view -> itemPresenter.onClick(model));
            addButton.setOnClickListener(v -> itemPresenter.onAdd(id));
        }

        @Override
        public void closeItem() {
            swipeLayout.close();
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            itemPresenter.onBind(position);
        }
    }
}
