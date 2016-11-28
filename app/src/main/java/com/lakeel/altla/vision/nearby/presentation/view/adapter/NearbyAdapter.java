package com.lakeel.altla.vision.nearby.presentation.view.adapter;

import com.amulyakhare.textdrawable.TextDrawable;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyItemModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.nearby.NearbyListPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyItemView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Color;
import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.NearbyViewHolder> {

    private NearbyListPresenter mNearbyListPresenter;

    public NearbyAdapter(NearbyListPresenter presenter) {
        mNearbyListPresenter = presenter;
    }

    @Override
    public NearbyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_item, parent, false);
        NearbyViewHolder nearbyViewHolder = new NearbyViewHolder(itemView);
        mNearbyListPresenter.onCreateItemView(nearbyViewHolder);
        return nearbyViewHolder;
    }

    @Override
    public void onBindViewHolder(NearbyViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mNearbyListPresenter.getItemCount();
    }

    final class NearbyViewHolder extends RecyclerView.ViewHolder implements NearbyItemView {

        private NearbyListPresenter.NearbyItemPresenter mNearbyItemPresenter;

        @BindView(R.id.layout)
        LinearLayout mLayout;

        @BindView(R.id.textView_user_item)
        TextView mUserName;

        @BindView(R.id.imageView_user_profile)
        ImageView mImageView;

        NearbyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setItemPresenter(NearbyListPresenter.NearbyItemPresenter nearbyItemPresenter) {
            mNearbyItemPresenter = nearbyItemPresenter;
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            mNearbyItemPresenter.onBind(position);
        }

        @Override
        public void showItem(NearbyItemModel model) {
            String displayName = model.mName;
            mUserName.setText(displayName);

            if (StringUtils.isEmpty(model.mImageUri)) {
                String initial = displayName.substring(0, 1);
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(initial, Color.RED);
                mImageView.post(() -> mImageView.setImageDrawable(drawable));
            } else {
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(model.mImageUri, mImageView);
            }

            mLayout.setOnLongClickListener(view -> {
                boolean isAlreadyChecked = model.mChecked;

                if (isAlreadyChecked) {
                    if (StringUtils.isEmpty(model.mImageUri)) {
                        String initial = displayName.substring(0, 1);
                        TextDrawable drawable = TextDrawable.builder()
                                .buildRound(initial, Color.RED);
                        mImageView.post(() -> mImageView.setImageDrawable(drawable));
                    } else {
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        imageLoader.displayImage(model.mImageUri, mImageView);
                    }
                } else {
                    mImageView.setImageResource(R.drawable.ic_check);
                }

                mNearbyItemPresenter.onCheck(model);

                return false;
            });
        }
    }
}
