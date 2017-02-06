package com.lakeel.altla.vision.nearby.presentation.view.fragment.favorite;

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
import com.lakeel.altla.vision.nearby.presentation.presenter.favorite.FavoriteListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteListView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.FavoriteAdapter;
import com.lakeel.altla.vision.nearby.presentation.view.divider.DividerItemDecoration;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.FragmentController;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.layoutmanagers.ScrollSmoothLineaerLayoutManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class FavoriteListFragment extends Fragment implements FavoriteListView {

    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;

    @BindView(R.id.recyclerView)
    UltimateRecyclerView recyclerView;

    @Inject
    FavoriteListPresenter presenter;

    public static FavoriteListFragment newInstance() {
        return new FavoriteListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipe_list, container, false);
        ButterKnife.bind(this, view);

        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.toolbar_title_favorites);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(true);

        RecyclerView.LayoutManager mLayoutManager = new ScrollSmoothLineaerLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false, 500);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        FavoriteAdapter favoritesRecyclerAdapter = new FavoriteAdapter(presenter);
        recyclerView.setAdapter(favoritesRecyclerAdapter);

        presenter.onActivityCreated();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void updateItems(List<FavoriteModel> models) {
        FavoriteAdapter adapter = ((FavoriteAdapter) recyclerView.getAdapter());
        adapter.removeAll();
        adapter.insert(models);
    }

    @Override
    public void removeAll(int size) {
        recyclerView.getAdapter().notifyItemRangeRemoved(0, size);
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
    public void showSnackBar(int resId) {
        Snackbar.make(mainLayout, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showFavoriteUserFragment(FavoriteModel model) {
        FragmentController controller = new FragmentController(this);
        controller.showFavoriteUserFragment(model.userId, model.userName);
    }
}
