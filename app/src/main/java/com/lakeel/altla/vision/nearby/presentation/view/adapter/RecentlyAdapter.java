package com.lakeel.altla.vision.nearby.presentation.view.adapter;

import com.amulyakhare.textdrawable.TextDrawable;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.RecentlyItemModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.recently.RecentlyListPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.DateFormatter;
import com.lakeel.altla.vision.nearby.presentation.view.RecentlyItemView;
import com.marshalchen.ultimaterecyclerview.SwipeableUltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Color;
import android.support.annotation.IntRange;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class RecentlyAdapter extends SwipeableUltimateViewAdapter<RecentlyItemModel> {

    private RecentlyListPresenter mRecentlyListPresenter;

    public RecentlyAdapter(RecentlyListPresenter recentlyListPresenter) {
        super(new ArrayList<>());
        mRecentlyListPresenter = recentlyListPresenter;
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.recently_item;
    }

    @Override
    protected UltimateRecyclerviewViewHolder newViewHolder(View view) {
        RecentlyItemsViewHolder viewHolder = new RecentlyItemsViewHolder(view, true);
        mRecentlyListPresenter.onCreateItemView(viewHolder);
        return viewHolder;
    }

    @Override
    protected void withBindHolder(UltimateRecyclerviewViewHolder bindHolder, RecentlyItemModel model, int position) {
        RecentlyItemsViewHolder holder = (RecentlyItemsViewHolder) bindHolder;
        holder.onBind(position);
    }

    public static class RecentlyItemsViewHolder extends UltimateRecyclerviewViewHolder implements RecentlyItemView {

        @BindView(R.id.layout_row)
        LinearLayout mLinearLayout;

        @BindView(R.id.textView_user_item)
        TextView mUserName;

        @BindView(R.id.imageView_user_profile)
        ImageView mImageView;

        @BindView(R.id.timestamp)
        TextView mTimestamp;

        @BindView(R.id.button_add)
        Button mAddButton;

        @BindView(R.id.swipe_layout)
        SwipeLayout mSwipeLayout;

        private RecentlyListPresenter.RecentlyItemPresenter mRecentlyItemPresenter;

        public RecentlyItemsViewHolder(View itemView, boolean bind) {
            super(itemView);
            if (bind) {
                ButterKnife.bind(this, itemView);
                mSwipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);
                mSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            }
        }

        @Override
        public void setItemPresenter(RecentlyListPresenter.RecentlyItemPresenter itemPresenter) {
            mRecentlyItemPresenter = itemPresenter;
        }

        @Override
        public void showItem(RecentlyItemModel model) {
            String id = model.userId;
            String userName = model.name;
            String imageUri = model.imageUri;

            mUserName.setText(userName);

            String initial = userName.substring(0, 1);
            if (StringUtils.isEmpty(imageUri)) {
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(initial, Color.RED);
                mImageView.post(() -> mImageView.setImageDrawable(drawable));
            } else {
                // Show profile on async.
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(imageUri, mImageView);
            }

            DateFormatter dateFormatter = new DateFormatter(model.passingTime);
            mTimestamp.setText(dateFormatter.format());

            mLinearLayout.setOnClickListener(view -> mRecentlyItemPresenter.onClick(model));
            mAddButton.setOnClickListener(v -> mRecentlyItemPresenter.onAdd(id));
        }

        @Override
        public void closeItem() {
            mSwipeLayout.close();
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            mRecentlyItemPresenter.onBind(position);
        }
    }
}
