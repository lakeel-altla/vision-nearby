package com.lakeel.altla.vision.nearby.presentation.view.adapter;

import android.graphics.Color;
import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyUserModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.NearbyUserListPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyUserItemView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class NearbyUserAdapter extends RecyclerView.Adapter<NearbyUserAdapter.NearbyUserItemViewHolder> {

    private NearbyUserListPresenter nearbyUserListPresenter;

    public NearbyUserAdapter(NearbyUserListPresenter presenter) {
        nearbyUserListPresenter = presenter;
    }

    @Override
    public NearbyUserItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nearby_user, parent, false);
        NearbyUserItemViewHolder nearbyViewHolder = new NearbyUserItemViewHolder(itemView);
        nearbyUserListPresenter.onCreateItemView(nearbyViewHolder);
        return nearbyViewHolder;
    }

    @Override
    public void onBindViewHolder(NearbyUserItemViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return nearbyUserListPresenter.getItemCount();
    }

    final class NearbyUserItemViewHolder extends RecyclerView.ViewHolder implements NearbyUserItemView {

        private NearbyUserListPresenter.NearbyUserItemPresenter itemPresenter;

        @BindView(R.id.layoutItem)
        LinearLayout itemLayout;

        @BindView(R.id.textViewUserName)
        TextView userNameTextView;

        @BindView(R.id.imageViewUser)
        ImageView userImageView;

        NearbyUserItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setItemPresenter(NearbyUserListPresenter.NearbyUserItemPresenter nearbyUserItemPresenter) {
            itemPresenter = nearbyUserItemPresenter;
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            itemPresenter.onBind(position);
        }

        @Override
        public void showItem(NearbyUserModel model) {
            String userName = model.userName;
            userNameTextView.setText(model.userName);

            if (StringUtils.isEmpty(model.imageUri)) {
                String initial = userName.substring(0, 1);
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(initial, Color.RED);
                userImageView.post(() -> userImageView.setImageDrawable(drawable));
            } else {
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(model.imageUri, userImageView);
            }
        }
    }
}
