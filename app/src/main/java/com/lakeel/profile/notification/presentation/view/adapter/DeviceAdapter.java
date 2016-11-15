package com.lakeel.profile.notification.presentation.view.adapter;

import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.presentation.presenter.device.DeviceListPresenter;
import com.lakeel.profile.notification.presentation.presenter.model.DeviceModel;
import com.lakeel.profile.notification.presentation.view.DateFormatter;
import com.lakeel.profile.notification.presentation.view.DeviceItemView;

import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    private DeviceListPresenter mDeviceListPresenter;

    public DeviceAdapter(DeviceListPresenter presenter) {
        mDeviceListPresenter = presenter;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.beacon_item, parent, false);
        DeviceAdapter.DeviceViewHolder viewHolder = new DeviceAdapter.DeviceViewHolder(itemView);
        mDeviceListPresenter.onCreateItemView(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mDeviceListPresenter.getItemCount();
    }

    public final class DeviceViewHolder extends RecyclerView.ViewHolder implements DeviceItemView {

        @BindView(R.id.layout)
        LinearLayout mLayout;

        @BindView(R.id.textName)
        TextView mBeaconName;

        private DeviceListPresenter.DeviceItemPresenter mItemPresenter;

        DeviceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            mItemPresenter.onBind(position);
        }

        @Override
        public void setItemPresenter(DeviceListPresenter.DeviceItemPresenter itemPresenter) {
            mItemPresenter = itemPresenter;
        }

        @Override
        public void showItem(DeviceModel model) {
            mBeaconName.setText(model.mName);
            mLayout.setOnClickListener(view -> mItemPresenter.onClick(model));
        }
    }
}
