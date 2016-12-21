package com.lakeel.altla.vision.nearby.presentation.view.fragment.nearby;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.google.android.gms.common.api.Status;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.nearby.NearbyListPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.GridShareSheet;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyListView;
import com.lakeel.altla.vision.nearby.presentation.view.actionBar.ActionBarColor;
import com.lakeel.altla.vision.nearby.presentation.view.actionBar.BarColor;
import com.lakeel.altla.vision.nearby.presentation.view.actionBar.ToolBarColor;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.NearbyAdapter;
import com.lakeel.altla.vision.nearby.presentation.view.divider.DividerItemDecoration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.lakeel.altla.vision.nearby.R.id.share;

public final class NearbyListFragment extends Fragment implements NearbyListView {

    @Inject
    NearbyListPresenter presenter;

    @BindView(R.id.shareSheet)
    BottomSheetLayout shareSheet;

    @BindView(R.id.layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private static final Logger LOGGER = LoggerFactory.getLogger(NearbyListFragment.class);

    private static final int REQUEST_CODE_SUBSCRIBE_RESULT = 1;

    public static NearbyListFragment newInstance() {
        return new NearbyListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);
        ButterKnife.bind(this, view);

        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        hideOptionMenu();
        drawNormalActionBarColor();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.title_nearby);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(true);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 10000);
            presenter.onRefresh();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        NearbyAdapter adapter = new NearbyAdapter(presenter);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_nearby, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case share:
                presenter.onShareSelected();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_SUBSCRIBE_RESULT == requestCode && resultCode == RESULT_OK) {
            presenter.onSubscribe();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void updateItems() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showSnackBar(int resId) {
        Snackbar.make(recyclerView, resId, Snackbar.LENGTH_SHORT).show();
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
        BarColor barColor;
        if (Build.VERSION.SDK_INT >= 21) {
            barColor = new ToolBarColor(getActivity(), R.color.darkgray, R.color.darkgray);
        } else {
            barColor = new ActionBarColor(getActivity(), R.color.darkgray);
        }
        barColor.draw();
    }

    @Override
    public void drawNormalActionBarColor() {
        BarColor barColor;
        if (Build.VERSION.SDK_INT >= 21) {
            barColor = new ToolBarColor(getActivity(), R.color.colorPrimary, R.color.colorPrimaryDark);
        } else {
            barColor = new ActionBarColor(getActivity(), R.color.colorPrimary);
        }
        barColor.draw();
    }

    @Override
    public void showOptionMenu() {
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void hideOptionMenu() {
        setHasOptionsMenu(false);
    }

    @Override
    public void showShareSheet() {
        GridShareSheet shareSheet = new GridShareSheet(getContext(), this.shareSheet, R.menu.menu_share);
        shareSheet.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_cm_favorites) {
                presenter.onAddToCmFavorite();
            }
            return true;
        });

        if (!presenter.isCmLinkEnabled()) {
            shareSheet.hideMenuItem(R.id.menu_cm_favorites);
        }

        shareSheet.show();
    }

    @Override
    public void showResolutionSystemDialog(Status status) {
        try {
            status.startResolutionForResult(getActivity(), REQUEST_CODE_SUBSCRIBE_RESULT);
        } catch (IntentSender.SendIntentException e) {
            LOGGER.error("Failed to show resolution dialog for nearby.", e);
        }
    }
}
