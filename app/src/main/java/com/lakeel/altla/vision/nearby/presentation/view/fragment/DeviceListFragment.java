package com.lakeel.altla.vision.nearby.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.DeviceListPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.DeviceListView;
import com.lakeel.altla.vision.nearby.presentation.view.EmptySupportedRecyclerView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.DeviceAdapter;
import com.lakeel.altla.vision.nearby.presentation.view.divider.DividerItemDecoration;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.TrackingBeacon;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class DeviceListFragment extends Fragment implements DeviceListView {

    @Inject
    DeviceListPresenter presenter;

    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;

    @BindView(R.id.recyclerView)
    EmptySupportedRecyclerView recyclerView;

    @BindView(R.id.textViewEmpty)
    View emptyView;

    public static DeviceListFragment newInstance() {
        return new DeviceListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        activity.setDrawerIndicatorEnabled(false);
        activity.setTitle(R.string.toolbar_title_devices);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setEmptyView(emptyView);

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
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void removeAll(int size) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        adapter.notifyItemRangeRemoved(0, size);
    }

    @Override
    public void showTrackingFragment(@NonNull String beaconId, @NonNull String name) {
        FragmentController controller = new FragmentController(this);
        controller.showTrackingFragment(new TrackingBeacon(beaconId, name));
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        Snackbar.make(mainLayout, resId, Snackbar.LENGTH_SHORT).show();
    }
}
