package com.lakeel.profile.notification.presentation.view.adapter;

import com.amulyakhare.textdrawable.TextDrawable;
import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.core.StringUtils;
import com.lakeel.profile.notification.presentation.presenter.model.ItemModel;
import com.lakeel.profile.notification.presentation.presenter.search.SearchPresenter;
import com.lakeel.profile.notification.presentation.view.SearchItemView;
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

public final class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private SearchPresenter mSearchPresenter;

    public SearchAdapter(SearchPresenter presenter) {
        mSearchPresenter = presenter;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
        SearchViewHolder searchViewHolder = new SearchViewHolder(itemView);
        mSearchPresenter.onCreateItemView(searchViewHolder);
        return searchViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mSearchPresenter.getItemCount();
    }

    public final class SearchViewHolder extends RecyclerView.ViewHolder implements SearchItemView {

        @BindView(R.id.layout_row)
        LinearLayout mLinearLayout;

        @BindView(R.id.textView_user_item)
        TextView mUserName;

        @BindView(R.id.imageView_user_profile)
        ImageView mImageView;

        private SearchPresenter.SearchItemPresenter mSearchItemPresenter;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setItemPresenter(SearchPresenter.SearchItemPresenter itemPresenter) {
            mSearchItemPresenter = itemPresenter;
        }

        @Override
        public void showItem(ItemModel model) {
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

            mLinearLayout.setOnClickListener(view -> mSearchItemPresenter.onClick(model));
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            mSearchItemPresenter.onBind(position);
        }
    }
}