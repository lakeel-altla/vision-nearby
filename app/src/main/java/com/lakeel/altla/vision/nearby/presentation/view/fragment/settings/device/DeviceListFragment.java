package com.lakeel.altla.vision.nearby.presentation.view.fragment.settings.device;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.device.DeviceListPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.DeviceView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.DeviceAdapter;
import com.lakeel.altla.vision.nearby.presentation.view.divider.DividerItemDecoration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class DeviceListFragment extends Fragment implements DeviceView {

    public static DeviceListFragment newInstance() {
        return new DeviceListFragment();
    }

    @Inject
    DeviceListPresenter mPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        setHasOptionsMenu(true);

        ButterKnife.bind(this, view);

        MainActivity.getUserComponent(this).inject(this);

        mPresenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        activity.setDrawerIndicatorEnabled(false);

        getActivity().setTitle(R.string.title_devices);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        DeviceAdapter adapter = new DeviceAdapter(mPresenter);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            default:
                break;
        }
        return true;
    }

    @Override
    public void updateItems() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showTrackingFragment(String beaconId, String beaconName) {
        ((MainActivity) getActivity()).showTrackingFragment(beaconId, beaconName);
    }
}
