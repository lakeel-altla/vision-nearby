package com.lakeel.profile.notification.presentation.view.fragment.favorites;

import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.presentation.intent.IntentExtra;
import com.lakeel.profile.notification.presentation.presenter.favorites.FavoritesListPresenter;
import com.lakeel.profile.notification.presentation.presenter.model.FavoriteModel;
import com.lakeel.profile.notification.presentation.view.FavoriteListView;
import com.lakeel.profile.notification.presentation.view.activity.FavoritesUserActivity;
import com.lakeel.profile.notification.presentation.view.activity.MainActivity;
import com.lakeel.profile.notification.presentation.view.adapter.FavoritesAdapter;
import com.lakeel.profile.notification.presentation.view.divider.DividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.layoutmanagers.ScrollSmoothLineaerLayoutManager;

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

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class FavoritesListFragment extends Fragment implements FavoriteListView {

    @BindView(R.id.layout)
    RelativeLayout mRelativeLayout;

    @BindView(R.id.recycler_view)
    UltimateRecyclerView mUltimateRecyclerView;

    @Inject
    FavoritesListPresenter mPresenter;

    public static FavoritesListFragment newInstance() {
        return new FavoritesListFragment();
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
    public void showFavoritesUserActivity(String id) {
        Intent intent = new Intent(getContext(), FavoritesUserActivity.class);
        intent.putExtra(IntentExtra.ID.name(), id);
        getContext().startActivity(intent);
    }
}
