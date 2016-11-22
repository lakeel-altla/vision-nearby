package com.lakeel.altla.vision.nearby.presentation.view.adapter;

import com.amulyakhare.textdrawable.TextDrawable;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.presentation.presenter.favorites.FavoritesListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteItemView;
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

public final class FavoritesAdapter extends SwipeableUltimateViewAdapter<FavoriteModel> {

    private FavoritesListPresenter mFavoritesListPresenter;

    public FavoritesAdapter(FavoritesListPresenter presenter) {
        super(new ArrayList<>());
        mFavoritesListPresenter = presenter;
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.favorite_item;
    }

    @Override
    protected UltimateRecyclerviewViewHolder newViewHolder(View view) {
        FavoritesListViewHolder viewHolder = new FavoritesListViewHolder(view, true);
        mFavoritesListPresenter.onCreateItemView(viewHolder);
        return viewHolder;
    }

    @Override
    protected void withBindHolder(UltimateRecyclerviewViewHolder bindHolder, FavoriteModel model, int position) {
        FavoritesListViewHolder holder = (FavoritesListViewHolder) bindHolder;
        holder.onBind(position);
    }

    public final class FavoritesListViewHolder extends UltimateRecyclerviewViewHolder implements FavoriteItemView {

        private FavoritesListPresenter.FavoritesItemPresenter mFavoritesItemPresenter;

        @BindView(R.id.layout_row)
        LinearLayout mLinearLayout;

        @BindView(R.id.textView_user_item)
        TextView mUserName;

        @BindView(R.id.imageView_user_profile)
        ImageView mImageView;

        @BindView(R.id.button_remove)
        Button removeButton;

        @BindView(R.id.swipe_layout)
        SwipeLayout mSwipeLayout;

        public FavoritesListViewHolder(View itemView, boolean bind) {
            super(itemView);
            if (bind) {
                ButterKnife.bind(this, itemView);
                mSwipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);
                mSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            }
        }

        @Override
        public void setItemPresenter(FavoritesListPresenter.FavoritesItemPresenter itemPresenter) {
            mFavoritesItemPresenter = itemPresenter;
        }

        @Override
        public void showItem(FavoriteModel model) {
            String displayName = model.mName;
            mUserName.setText(displayName);

            if (StringUtils.isEmpty(model.mImageUri)) {
                String initial = displayName.substring(0, 1);
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(initial, Color.RED);
                mImageView.post(() -> mImageView.setImageDrawable(drawable));
            } else {
                // 非同期でプロフィール画像を表示。
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(model.mImageUri, mImageView);
            }

            removeButton.setOnClickListener(v -> mFavoritesItemPresenter.onRemove(model));
            mLinearLayout.setOnClickListener(view -> mFavoritesItemPresenter.onClick(model));
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            mFavoritesItemPresenter.onBind(position);
        }
    }
}
