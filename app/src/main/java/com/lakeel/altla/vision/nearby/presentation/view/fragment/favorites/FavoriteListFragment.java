package com.lakeel.altla.vision.nearby.presentation.view.fragment.favorites;

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
import com.lakeel.altla.vision.nearby.presentation.presenter.favorites.FavoriteListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteListView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.FavoritesAdapter;
import com.lakeel.altla.vision.nearby.presentation.view.divider.DividerItemDecoration;
import com.lakeel.altla.vision.nearby.presentation.view.transaction.FragmentController;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.layoutmanagers.ScrollSmoothLineaerLayoutManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class FavoriteListFragment extends Fragment implements FavoriteListView {

    @BindView(R.id.layout)
    RelativeLayout mRelativeLayout;

    @BindView(R.id.recycler_view)
    UltimateRecyclerView mUltimateRecyclerView;

    @Inject
    FavoriteListPresenter mPresenter;

    public static FavoriteListFragment newInstance() {
        return new FavoriteListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipe_list, container, false);
        ButterKnife.bind(this, view);

        MainActivity.getUserComponent(this).inject(this);

        mPresenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.title_favorites);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(true);

        MainActivity activity = (MainActivity) getActivity();
        activity.setDrawerIndicatorEnabled(true);

        RecyclerView.LayoutManager mLayoutManager = new ScrollSmoothLineaerLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false, 500);
        mUltimateRecyclerView.setLayoutManager(mLayoutManager);
        mUltimateRecyclerView.setHasFixedSize(false);
        mUltimateRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        FavoritesAdapter favoritesRecyclerAdapter = new FavoritesAdapter(mPresenter);
        mUltimateRecyclerView.setAdapter(favoritesRecyclerAdapter);
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
    public void updateItems(List<FavoriteModel> models) {
        FavoritesAdapter adapter = ((FavoritesAdapter) mUltimateRecyclerView.getAdapter());
        adapter.removeAll();
        adapter.insert(models);
    }

    @Override
    public void removeAll(int size) {
        mUltimateRecyclerView.getAdapter().notifyItemRangeRemoved(0, size);
    }

    @Override
    public void showSnackBar(int resId) {
        Snackbar.make(mRelativeLayout, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showFavoritesUserActivity(String userId, String userName) {
        FragmentController controller = new FragmentController(getFragmentManager());
        controller.showProfileFragment(userId, userName);
    }
}