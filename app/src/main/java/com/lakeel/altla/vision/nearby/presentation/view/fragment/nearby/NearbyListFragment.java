package com.lakeel.altla.vision.nearby.presentation.view.fragment.nearby;

import com.google.android.gms.common.api.Status;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.nearby.NearbyPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.bar.ActionBarColor;
import com.lakeel.altla.vision.nearby.presentation.view.GridShareSheet;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyView;
import com.lakeel.altla.vision.nearby.presentation.view.bar.StatusBarColor;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.NearbyAdapter;
import com.lakeel.altla.vision.nearby.presentation.view.divider.DividerItemDecoration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.lakeel.altla.vision.nearby.R.id.share;

public final class NearbyListFragment extends Fragment implements NearbyView {

    @Inject
    NearbyPresenter mPresenter;

    @BindView(R.id.shareSheet)
    BottomSheetLayout mShareSheet;

    @BindView(R.id.layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

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

        mPresenter.onCreateView(this);

        hideOptionMenu();
        drawNormalActionBarColor();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.title_nearby);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(true);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000);
            mPresenter.onRefresh();
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        NearbyAdapter adapter = new NearbyAdapter(mPresenter);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case share:
                mPresenter.onShareSelected();
                break;
            default:
                break;
        }

        return false;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_SUBSCRIBE_RESULT == requestCode && resultCode == RESULT_OK) {
            mPresenter.onSubscribe();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void updateItems() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showSnackBar(int resId) {
        Snackbar.make(mRecyclerView, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showIndicator() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideIndicator() {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
    }

    @Override
    public void drawEditableActionBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            StatusBarColor statusBarColor = new StatusBarColor(getActivity(), R.color.darkgray);
            statusBarColor.draw();
        }
        ActionBarColor actionBarColor = new ActionBarColor(getActivity(), R.color.darkgray);
        actionBarColor.draw();
    }

    @Override
    public void drawNormalActionBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            StatusBarColor statusBarColor = new StatusBarColor(getActivity(), R.color.colorPrimaryDark);
            statusBarColor.draw();
        }
        ActionBarColor actionBarColor = new ActionBarColor(getActivity(), R.color.colorPrimary);
        actionBarColor.draw();
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
        GridShareSheet shareSheet = new GridShareSheet(getContext(), mShareSheet, R.menu.menu_share);
        shareSheet.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_cm_favorites) {
                mPresenter.onAddToCmFavorite();
            }
            return true;
        });

        if (!mPresenter.isCmLinkEnabled()) {
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
