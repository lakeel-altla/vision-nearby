package com.lakeel.altla.vision.nearby.presentation.view.fragment.device;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.setting.device.DeviceListPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.DeviceListView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.DeviceAdapter;
import com.lakeel.altla.vision.nearby.presentation.view.divider.DividerItemDecoration;
import com.lakeel.altla.vision.nearby.presentation.view.transaction.FragmentController;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class DeviceListFragment extends Fragment implements DeviceListView {

    @Inject
    DeviceListPresenter presenter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static DeviceListFragment newInstance() {
        return new DeviceListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        setHasOptionsMenu(true);

        ButterKnife.bind(this, view);

        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        activity.setDrawerIndicatorEnabled(false);

        getActivity().setTitle(R.string.title_devices);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        DeviceAdapter adapter = new DeviceAdapter(presenter);
        recyclerView.setAdapter(adapter);

        presenter.onActivityCreated();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
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
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showTrackingFragment(String beaconId, String beaconName) {
        FragmentController controller = new FragmentController(getActivity().getSupportFragmentManager());
        controller.showTrackingFragment(beaconId, beaconName);
    }

    @Override
    public void removeAll(int size) {
        recyclerView.getAdapter().notifyItemRangeRemoved(0, size);
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        Snackbar.make(recyclerView, resId, Snackbar.LENGTH_SHORT).show();
    }
}
