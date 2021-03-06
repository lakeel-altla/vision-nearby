package com.lakeel.altla.vision.nearby.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.NearbyHistoryListPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.NearbyHistoryListView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.NearbyHistoryAdapter;
import com.lakeel.altla.vision.nearby.presentation.view.divider.DividerItemDecoration;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.PassingUser;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.layoutmanagers.ScrollSmoothLineaerLayoutManager;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeItemManagerInterface;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class NearbyHistoryListFragment extends Fragment implements NearbyHistoryListView {

    @Inject
    NearbyHistoryListPresenter presenter;

    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;

    @BindView(R.id.recyclerView)
    UltimateRecyclerView recyclerView;

    public static NearbyHistoryListFragment newInstance() {
        return new NearbyHistoryListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipe_list, container, false);
        ButterKnife.bind(this, view);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = ((MainActivity) getActivity());
        activity.setTitle(R.string.toolbar_title_history);
        activity.setDrawerIndicatorEnabled(true);

        RecyclerView.LayoutManager layoutManager = new ScrollSmoothLineaerLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false, 500);
        recyclerView.setLayoutManager(layoutManager);

        NearbyHistoryAdapter adapter = new NearbyHistoryAdapter(presenter);
        adapter.setMode(SwipeItemManagerInterface.Mode.Single);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setAdapter(adapter);

        presenter.onActivityCreated();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void updateItems() {
        NearbyHistoryAdapter adapter = ((NearbyHistoryAdapter) recyclerView.getAdapter());
        adapter.removeAll();
        adapter.insert(presenter.getItems());
    }

    @Override
    public void removeAll(int size) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        adapter.notifyItemRangeRemoved(0, size);
    }

    @Override
    public void showEmptyView() {
        recyclerView.setEmptyView(R.layout.empty_view, 0);
        recyclerView.showEmptyView();
    }

    @Override
    public void hideEmptyView() {
        recyclerView.hideEmptyView();
    }

    @Override
    public void showPassingUserFragment(@NonNull PassingUser passingUser) {
        FragmentController controller = new FragmentController(this);
        controller.showPassingUserFragment(passingUser);
    }

    @Override
    public void showSnackBar(int resId) {
        Snackbar.make(mainLayout, resId, Snackbar.LENGTH_SHORT).show();
    }
}
