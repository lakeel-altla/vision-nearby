package com.lakeel.altla.vision.nearby.presentation.view.adapter;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.settings.device.DeviceListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.BeaconModel;
import com.lakeel.altla.vision.nearby.presentation.view.DeviceItemView;

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

    private DeviceListPresenter deviceListPresenter;

    public DeviceAdapter(DeviceListPresenter presenter) {
        deviceListPresenter = presenter;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.beacon_item, parent, false);
        DeviceAdapter.DeviceViewHolder viewHolder = new DeviceAdapter.DeviceViewHolder(itemView);
        deviceListPresenter.onCreateItemView(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return deviceListPresenter.getItemCount();
    }

    public final class DeviceViewHolder extends RecyclerView.ViewHolder implements DeviceItemView {

        @BindView(R.id.layout)
        LinearLayout itemLayout;

        @BindView(R.id.textName)
        TextView beaconName;

        private DeviceListPresenter.DeviceItemPresenter itemPresenter;

        DeviceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            itemPresenter.onBind(position);
        }

        @Override
        public void setItemPresenter(DeviceListPresenter.DeviceItemPresenter itemPresenter) {
            this.itemPresenter = itemPresenter;
        }

        @Override
        public void showItem(BeaconModel model) {
            beaconName.setText(model.mName);
            itemLayout.setOnClickListener(view -> itemPresenter.onClick(model));
        }
    }
}
