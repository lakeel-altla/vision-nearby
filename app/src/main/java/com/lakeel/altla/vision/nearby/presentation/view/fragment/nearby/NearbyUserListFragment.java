package com.lakeel.altla.vision.nearby.presentation.view.fragment.nearby;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.nearby.NearbyUserListPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyUserListView;
import com.lakeel.altla.vision.nearby.presentation.view.actionBar.BarColorContext;
import com.lakeel.altla.vision.nearby.presentation.view.actionBar.BarColorFactory;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.NearbyUserAdapter;
import com.lakeel.altla.vision.nearby.presentation.view.divider.DividerItemDecoration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public final class NearbyUserListFragment extends Fragment implements NearbyUserListView {

    @Inject
    NearbyUserListPresenter presenter;

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private static final Logger LOGGER = LoggerFactory.getLogger(NearbyUserListFragment.class);

    private static final int REQUEST_CODE_ENABLE_BLE = 11;

    private static final int REQUEST_CODE_ACCESS_FINE_LOCATION = 22;

    public static NearbyUserListFragment newInstance() {
        return new NearbyUserListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby_user, container, false);
        ButterKnife.bind(this, view);

        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        drawDefaultActionBarColor();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.toolbar_title_nearby);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(true);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 10000);
            presenter.onRefresh();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        NearbyUserAdapter adapter = new NearbyUserAdapter(presenter);
        recyclerView.setAdapter(adapter);
        presenter.onActivityCreated();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_ENABLE_BLE == requestCode) {
            if (RESULT_OK == resultCode) {
                presenter.onBleEnabled();
            } else {
                LOGGER.error("Failed to enable BLE.");
                showSnackBar(R.string.snackBar_error_not_enable_ble);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.onAccessFineLocationGranted();
            } else {
                LOGGER.warn("Access fine location permission is denied.");
                showSnackBar(R.string.snackBar_error_not_find);
            }
        }
    }

    @Override
    public void showBleEnabledActivity(Intent intent) {
        startActivityForResult(intent, REQUEST_CODE_ENABLE_BLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void requestAccessFineLocationPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ACCESS_FINE_LOCATION);
    }

    @Override
    public void updateItems() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showIndicator() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideIndicator() {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
    }

    @Override
    public void drawEditableActionBarColor() {
        BarColorContext context = new BarColorContext(BarColorFactory.createEditableColor(this));
        context.draw();
    }

    @Override
    public void drawDefaultActionBarColor() {
        BarColorContext context = new BarColorContext(BarColorFactory.createDefaultColor(this));
        context.draw();
    }

    @Override
    public void hideOptionMenu() {
        setHasOptionsMenu(false);
    }

    @Override
    public void showSnackBar(int resId) {
        Snackbar.make(recyclerView, resId, Snackbar.LENGTH_SHORT).show();
    }
}
