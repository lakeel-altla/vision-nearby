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
import com.lakeel.altla.vision.nearby.presentation.presenter.favorite.FavoriteListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteItemView;
import com.marshalchen.ultimaterecyclerview.SwipeableUltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class FavoriteAdapter extends SwipeableUltimateViewAdapter<FavoriteModel> {

    private FavoriteListPresenter favoriteListPresenter;

    public FavoriteAdapter(FavoriteListPresenter presenter) {
        super(new ArrayList<>());
        favoriteListPresenter = presenter;
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.favorite_item;
    }

    @Override
    protected UltimateRecyclerviewViewHolder newViewHolder(View view) {
        FavoritesListViewHolder viewHolder = new FavoritesListViewHolder(view, true);
        favoriteListPresenter.onCreateItemView(viewHolder);
        return viewHolder;
    }

    @Override
    protected void withBindHolder(UltimateRecyclerviewViewHolder bindHolder, FavoriteModel model, int position) {
        FavoritesListViewHolder holder = (FavoritesListViewHolder) bindHolder;
        holder.onBind(position);
    }

    public final class FavoritesListViewHolder extends UltimateRecyclerviewViewHolder implements FavoriteItemView {

        private FavoriteListPresenter.FavoritesItemPresenter itemPresenter;

        @BindView(R.id.layout_row)
        LinearLayout itemLayout;

        @BindView(R.id.textView_user_item)
        TextView userName;

        @BindView(R.id.imageView_user_profile)
        ImageView imageViewUser;

        @BindView(R.id.button_remove)
        Button buttonRemove;

        @BindView(R.id.swipe_layout)
        SwipeLayout swipeLayout;

        public FavoritesListViewHolder(View itemView, boolean bind) {
            super(itemView);
            if (bind) {
                ButterKnife.bind(this, itemView);
                swipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);
                swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            }
        }

        @Override
        public void setItemPresenter(FavoriteListPresenter.FavoritesItemPresenter itemPresenter) {
            this.itemPresenter = itemPresenter;
        }

        @Override
        public void showItem(FavoriteModel model) {
            String displayName = model.name;
            userName.setText(displayName);

            if (StringUtils.isEmpty(model.imageUri)) {
                String initial = displayName.substring(0, 1);
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(initial, Color.RED);
                imageViewUser.post(() -> imageViewUser.setImageDrawable(drawable));
            } else {
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(model.imageUri, imageViewUser);
            }

            buttonRemove.setOnClickListener(v -> itemPresenter.onRemove(model));
            itemLayout.setOnClickListener(view -> itemPresenter.onClick(model));
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            itemPresenter.onBind(position);
        }
    }
}
