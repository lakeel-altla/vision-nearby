package com.lakeel.profile.notification.presentation.view.fragment.tracking;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.firebase.geofire.GeoLocation;
import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.presentation.presenter.tracking.TrackingPresenter;
import com.lakeel.profile.notification.presentation.view.TrackingView;
import com.lakeel.profile.notification.presentation.view.activity.MainActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

public final class TrackingFragment extends Fragment implements TrackingView, OnMapReadyCallback {

    private static final String BUNDLE_KEY_ID = "id";

    public static TrackingFragment newInstance(String id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_KEY_ID, id);

        TrackingFragment fragment = new TrackingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Inject
    TrackingPresenter mPresenter;

    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracking, container, false);

        MainActivity.getUserComponent(this).inject(this);

        mPresenter.onCreateView(this);

        setHasOptionsMenu(true);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.tracking_map);
        supportMapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        activity.setDrawerIndicatorEnabled(false);

        Bundle bundle = getArguments();
        String id = (String) bundle.get(BUNDLE_KEY_ID);
        mPresenter.setId(id);

        getActivity().setTitle(R.string.title_tracking);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(14);
        mMap.moveCamera(cameraUpdate);

        mPresenter.onMapReady();
    }

    @Override
    public void showLocationMap(GeoLocation location) {
        LatLng latLng = new LatLng(location.latitude, location.longitude);
        mMap.addMarker(new MarkerOptions()
                .position(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
