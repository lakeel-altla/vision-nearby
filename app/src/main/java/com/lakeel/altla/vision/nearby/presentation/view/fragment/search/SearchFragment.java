package com.lakeel.altla.vision.nearby.presentation.view.fragment.search;


import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.search.SearchPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.SearchView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.SearchAdapter;
import com.lakeel.altla.vision.nearby.presentation.view.divider.DividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.layoutmanagers.ScrollSmoothLineaerLayoutManager;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public final class SearchFragment extends Fragment implements SearchView {

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Inject
    SearchPresenter mPresenter;

    private MaterialSearchView mSearchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        setHasOptionsMenu(true);

        ButterKnife.bind(this, view);

        MainActivity.getUserComponent(this).inject(this);

//        mSearchView = (MaterialSearchView) getActivity().findViewById(R.id.search_view);
//        mSearchView.setEllipsize(true);
//        mSearchView.setVoiceSearch(true);
//        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                mPresenter.onSearch(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });

        mPresenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        activity.setDrawerIndicatorEnabled(true);

        getActivity().setTitle(R.string.title_search);

        RecyclerView.LayoutManager mLayoutManager = new ScrollSmoothLineaerLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false, 500);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        SearchAdapter searchAdapter = new SearchAdapter(mPresenter);
        mRecyclerView.setAdapter(searchAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(menuItem);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    mSearchView.setQuery(searchWrd, false);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // TODO
    private boolean isVoiceAvailable() {
        PackageManager pm = getContext().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return activities.size() == 0;
    }

    @Override
    public void showTrackingFragment(String id) {
//        ((MainActivity) getActivity()).showTrackingFragment(id);
    }
}
