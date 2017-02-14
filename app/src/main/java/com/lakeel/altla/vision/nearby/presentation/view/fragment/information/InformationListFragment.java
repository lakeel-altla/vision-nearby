package com.lakeel.altla.vision.nearby.presentation.view.fragment.information;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.information.InformationListPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.EmptySupportedRecyclerView;
import com.lakeel.altla.vision.nearby.presentation.view.InformationListView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.InformationAdapter;
import com.lakeel.altla.vision.nearby.presentation.view.divider.DividerItemDecoration;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.FragmentController;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class InformationListFragment extends Fragment implements InformationListView {

    @Inject
    InformationListPresenter presenter;

    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;

    @BindView(R.id.recyclerView)
    EmptySupportedRecyclerView recyclerView;

    @BindView(R.id.textViewEmpty)
    View emptyView;

    public static InformationListFragment newInstance() {
        Bundle args = new Bundle();

        InformationListFragment fragment = new InformationListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.toolbar_title_information);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setEmptyView(emptyView);

        InformationAdapter adapter = new InformationAdapter(presenter);
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
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showInformationFragment(String informationId) {
        FragmentController controller = new FragmentController(this);
        controller.showInformationFragment(informationId);
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        Snackbar.make(mainLayout, resId, Snackbar.LENGTH_SHORT).show();
    }
}
