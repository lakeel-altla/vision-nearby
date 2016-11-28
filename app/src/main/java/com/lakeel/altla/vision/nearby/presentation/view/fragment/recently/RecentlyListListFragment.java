package com.lakeel.altla.vision.nearby.presentation.view.fragment.recently;

import android.content.Intent;
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
import com.lakeel.altla.vision.nearby.presentation.intent.IntentExtra;
import com.lakeel.altla.vision.nearby.presentation.intent.RecentlyIntentData;
import com.lakeel.altla.vision.nearby.presentation.presenter.recently.RecentlyListPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.RecentlyListView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.activity.RecentlyUserUserActivity;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.RecentlyAdapter;
import com.lakeel.altla.vision.nearby.presentation.view.divider.DividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.layoutmanagers.ScrollSmoothLineaerLayoutManager;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeItemManagerInterface;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class RecentlyListListFragment extends Fragment implements RecentlyListView {

    @Inject
    RecentlyListPresenter mPresenter;

    @BindView(R.id.layout)
    RelativeLayout mRelativeLayout;

    @BindView(R.id.recycler_view)
    UltimateRecyclerView mUltimateRecyclerView;

    public static RecentlyListListFragment newInstance() {
        return new RecentlyListListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipe_list, container, false);
        ButterKnife.bind(this, view);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        mPresenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.title_recently);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(true);

        RecyclerView.LayoutManager mLayoutManager = new ScrollSmoothLineaerLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false, 500);
        mUltimateRecyclerView.setLayoutManager(mLayoutManager);

        RecentlyAdapter adapter = new RecentlyAdapter(mPresenter);
        adapter.setMode(SwipeItemManagerInterface.Mode.Single);

        mUltimateRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        mUltimateRecyclerView.setAdapter(adapter);
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
    public void updateItems() {
        RecentlyAdapter adapter = ((RecentlyAdapter) mUltimateRecyclerView.getAdapter());
        adapter.removeAll();
        adapter.insert(mPresenter.getItems());
    }

    @Override
    public void showSnackBar(int resId) {
        Snackbar.make(mRelativeLayout, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showRecentlyUserActivity(RecentlyIntentData data) {
        Intent intent = new Intent(getContext(), RecentlyUserUserActivity.class);
        intent.putExtra(IntentExtra.RECENTLY.name(), data);
        getContext().startActivity(intent);
    }
}
