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
import com.lakeel.altla.vision.nearby.presentation.presenter.FavoriteListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteListView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.FavoriteAdapter;
import com.lakeel.altla.vision.nearby.presentation.view.divider.DividerItemDecoration;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.FavoriteUser;
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

        MainActivity activity = ((MainActivity) getActivity());
        activity.setDrawerIndicatorEnabled(true);

        RecyclerView.LayoutManager layoutManager = new ScrollSmoothLineaerLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false, 500);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(presenter);
        recyclerView.setAdapter(favoriteAdapter);

        presenter.onActivityCreated();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void updateItems(@NonNull List<FavoriteModel> models) {
        FavoriteAdapter adapter = ((FavoriteAdapter) recyclerView.getAdapter());
        adapter.removeAll();
        adapter.insert(models);
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
    public void showSnackBar(int resId) {
        Snackbar.make(mainLayout, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showFavoriteUserFragment(@NonNull FavoriteModel model) {
        FragmentController controller = new FragmentController(this);
        controller.showFavoriteUserFragment(new FavoriteUser(model.userId, model.userName));
    }
}
