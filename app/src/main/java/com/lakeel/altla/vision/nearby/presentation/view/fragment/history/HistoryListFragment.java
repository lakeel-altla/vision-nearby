package com.lakeel.altla.vision.nearby.presentation.view.fragment.history;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.history.HistoryListPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.HistoryListView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.RecentlyAdapter;
import com.lakeel.altla.vision.nearby.presentation.view.bundle.HistoryBundle;
import com.lakeel.altla.vision.nearby.presentation.view.divider.DividerItemDecoration;
import com.lakeel.altla.vision.nearby.presentation.view.transaction.FragmentController;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.layoutmanagers.ScrollSmoothLineaerLayoutManager;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeItemManagerInterface;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class HistoryListFragment extends Fragment implements HistoryListView {

    @Inject
    HistoryListPresenter presenter;

    @BindView(R.id.layout)
    RelativeLayout mainLayout;

    @BindView(R.id.recycler_view)
    UltimateRecyclerView recyclerView;

    public static HistoryListFragment newInstance() {
        return new HistoryListFragment();
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

        getActivity().setTitle(R.string.title_recently);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(true);

        RecyclerView.LayoutManager mLayoutManager = new ScrollSmoothLineaerLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false, 500);
        recyclerView.setLayoutManager(mLayoutManager);

        RecentlyAdapter adapter = new RecentlyAdapter(presenter);
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
        RecentlyAdapter adapter = ((RecentlyAdapter) recyclerView.getAdapter());
        adapter.removeAll();
        adapter.insert(presenter.getItems());
    }

    @Override
    public void showSnackBar(int resId) {
        Snackbar.make(mainLayout, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showHistoryFragment(HistoryBundle data) {
        FragmentController controller = new FragmentController(getActivity().getSupportFragmentManager());
        controller.showHistoryFragment(data);
    }
}
