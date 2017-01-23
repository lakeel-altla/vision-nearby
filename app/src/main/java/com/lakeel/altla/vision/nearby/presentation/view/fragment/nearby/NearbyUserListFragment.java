package com.lakeel.altla.vision.nearby.presentation.view.fragment.nearby;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.nearby.NearbyUserListPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyUserListView;
import com.lakeel.altla.vision.nearby.presentation.view.actionBar.BarColor;
import com.lakeel.altla.vision.nearby.presentation.view.actionBar.DefaultBarColorFactory;
import com.lakeel.altla.vision.nearby.presentation.view.actionBar.EditableBarColorFactory;
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

    @BindView(R.id.shareSheet)
    BottomSheetLayout shareSheet;

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private static final Logger LOGGER = LoggerFactory.getLogger(NearbyUserListFragment.class);

    private static final int REQUEST_CODE_ENABLE_BLE = 1;

    public static NearbyUserListFragment newInstance() {
        return new NearbyUserListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);
        ButterKnife.bind(this, view);

        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        drawDefaultActionBarColor();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.title_nearby_user);

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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_nearby, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
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
                presenter.subscribe();
            } else {
                LOGGER.error("Failed to enable BLE.");
                showSnackBar(R.string.error_not_enable_ble);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void showBleEnabledActivity(Intent intent) {
        startActivityForResult(intent, REQUEST_CODE_ENABLE_BLE);
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
        BarColor barColor = EditableBarColorFactory.create(this);
        barColor.draw();
    }

    @Override
    public void drawDefaultActionBarColor() {
        BarColor barColor = DefaultBarColorFactory.create(this);
        barColor.draw();
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