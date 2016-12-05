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
import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyItemModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.nearby.NearbyListPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyItemView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.NearbyViewHolder> {

    private NearbyListPresenter nearbyListPresenter;

    public NearbyAdapter(NearbyListPresenter presenter) {
        nearbyListPresenter = presenter;
    }

    @Override
    public NearbyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_item, parent, false);
        NearbyViewHolder nearbyViewHolder = new NearbyViewHolder(itemView);
        nearbyListPresenter.onCreateItemView(nearbyViewHolder);
        return nearbyViewHolder;
    }

    @Override
    public void onBindViewHolder(NearbyViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return nearbyListPresenter.getItemCount();
    }

    final class NearbyViewHolder extends RecyclerView.ViewHolder implements NearbyItemView {

        private NearbyListPresenter.NearbyItemPresenter itemPresenter;

        @BindView(R.id.layout)
        LinearLayout itemLayout;

        @BindView(R.id.textView_user_item)
        TextView userName;

        @BindView(R.id.imageView_user_profile)
        ImageView userImage;

        NearbyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setItemPresenter(NearbyListPresenter.NearbyItemPresenter nearbyItemPresenter) {
            itemPresenter = nearbyItemPresenter;
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            itemPresenter.onBind(position);
        }

        @Override
        public void showItem(NearbyItemModel model) {
            String displayName = model.name;
            userName.setText(displayName);

            if (StringUtils.isEmpty(model.imageUri)) {
                String initial = displayName.substring(0, 1);
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(initial, Color.RED);
                userImage.post(() -> userImage.setImageDrawable(drawable));
            } else {
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(model.imageUri, userImage);
            }

            itemLayout.setOnLongClickListener(view -> {
                boolean isAlreadyChecked = model.isChecked;

                if (isAlreadyChecked) {
                    if (StringUtils.isEmpty(model.imageUri)) {
                        String initial = displayName.substring(0, 1);
                        TextDrawable drawable = TextDrawable.builder()
                                .buildRound(initial, Color.RED);
                        userImage.post(() -> userImage.setImageDrawable(drawable));
                    } else {
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        imageLoader.displayImage(model.imageUri, userImage);
                    }
                } else {
                    userImage.setImageResource(R.drawable.ic_check);
                }

                itemPresenter.onCheck(model);

                return false;
            });
        }
    }
}
