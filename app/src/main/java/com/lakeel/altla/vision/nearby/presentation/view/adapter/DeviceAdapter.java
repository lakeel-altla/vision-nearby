package com.lakeel.altla.vision.nearby.presentation.view.adapter;

import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.DeviceModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.device.DeviceListPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.date.DateFormatter;
import com.lakeel.altla.vision.nearby.presentation.view.DeviceItemView;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    private DeviceListPresenter deviceListPresenter;

    public DeviceAdapter(DeviceListPresenter presenter) {
        deviceListPresenter = presenter;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false);
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

        @BindView(R.id.swipeLayout)
        RelativeLayout itemLayout;

        @BindView(R.id.buttonLost)
        Button lostButton;

        @BindView(R.id.buttonRemove)
        Button removeButton;

        @BindView(R.id.textViewDeviceName)
        TextView deviceNameTextView;

        @BindView(R.id.textViewLastUsedTime)
        TextView lastUsedTimeTextView;

        @BindView(R.id.imageViewLost)
        ImageView lostImageView;

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
        public void showItem(DeviceModel model) {
            deviceNameTextView.setText(model.name);
            itemLayout.setOnClickListener(view -> itemPresenter.onClick(model));

            DateFormatter formatter = new DateFormatter(model.lastUsedTime);
            String lastUsedTimeText = formatter.format();
            lastUsedTimeTextView.setText(lastUsedTimeText);

            removeButton.setOnClickListener(view -> itemPresenter.onRemove(model));

            if (model.isLost) {
                lostButton.setText(R.string.button_found);
                lostButton.setOnClickListener(view -> itemPresenter.onFound(model));

                lostImageView.setVisibility(View.VISIBLE);
                lostImageView.setImageResource(R.drawable.ic_search);
            } else {
                lostButton.setText(R.string.button_lost);
                lostButton.setOnClickListener(view -> itemPresenter.onLost(model));

                lostImageView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
