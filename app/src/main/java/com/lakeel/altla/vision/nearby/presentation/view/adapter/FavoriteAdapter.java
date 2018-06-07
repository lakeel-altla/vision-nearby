package com.lakeel.altla.vision.nearby.presentation.view.adapter;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.presentation.presenter.FavoriteListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteItemView;
import com.lakeel.altla.vision.nearby.presentation.view.drawable.UserInitial;
import com.marshalchen.ultimaterecyclerview.SwipeableUltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class FavoriteAdapter extends SwipeableUltimateViewAdapter<FavoriteModel> {

    private FavoriteListPresenter presenter;

    public FavoriteAdapter(@NonNull FavoriteListPresenter presenter) {
        // NOTE: Must set a mutable list.
        super(new ArrayList<>());
        this.presenter = presenter;
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.item_favorite;
    }

    @Override
    protected UltimateRecyclerviewViewHolder newViewHolder(View view) {
        FavoriteItemViewHolder viewHolder = new FavoriteItemViewHolder(view, true);
        presenter.onCreateItemView(viewHolder);
        return viewHolder;
    }

    @Override
    protected void withBindHolder(UltimateRecyclerviewViewHolder bindHolder, FavoriteModel model, int position) {
        FavoriteItemViewHolder holder = (FavoriteItemViewHolder) bindHolder;
        holder.onBind(position);
    }

    final class FavoriteItemViewHolder extends UltimateRecyclerviewViewHolder implements FavoriteItemView {

        private FavoriteListPresenter.FavoritesItemPresenter itemPresenter;

        @BindView(R.id.layoutItem)
        LinearLayout itemLayout;

        @BindView(R.id.textViewUserName)
        TextView userNameTextView;

        @BindView(R.id.imageViewUser)
        ImageView userImageView;

        @BindView(R.id.buttonRemove)
        Button removeButton;

        @BindView(R.id.layoutSwipe)
        SwipeLayout swipeLayout;

        FavoriteItemViewHolder(View itemView, boolean bind) {
            super(itemView);
            if (bind) {
                ButterKnife.bind(this, itemView);
                swipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);
                swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            }
        }

        @Override
        public void setItemPresenter(@NonNull FavoriteListPresenter.FavoritesItemPresenter itemPresenter) {
            this.itemPresenter = itemPresenter;
        }

        @Override
        public void showItem(@NonNull FavoriteModel model) {
            String userName = model.userName;
            userNameTextView.setText(userName);

            if (StringUtils.isEmpty(model.imageUri)) {
                userImageView.post(() -> {
                    UserInitial initial = new UserInitial(userName);
                    userImageView.setImageDrawable(initial.getDrawable());
                });
            } else {
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(model.imageUri, userImageView);
            }

            removeButton.setOnClickListener(v -> itemPresenter.onRemove(model));
            itemLayout.setOnClickListener(view -> itemPresenter.onClick(model));
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            itemPresenter.onBind(position);
        }
    }
}
