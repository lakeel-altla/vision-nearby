package com.lakeel.profile.notification.presentation.view.fragment.tracking;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.firebase.geofire.GeoLocation;
import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.presentation.constants.Colors;
import com.lakeel.profile.notification.presentation.constants.Radius;
import com.lakeel.profile.notification.presentation.presenter.tracking.TrackingPresenter;
import com.lakeel.profile.notification.presentation.view.TrackingView;
import com.lakeel.profile.notification.presentation.view.activity.MainActivity;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class TrackingFragment extends Fragment implements TrackingView, OnMapReadyCallback {

    private static final String BUNDLE_KEY_BEACON_ID = "beaconId";

    public static TrackingFragment newInstance(String id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_KEY_BEACON_ID, id);

        TrackingFragment fragment = new TrackingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Inject
    TrackingPresenter mPresenter;

    @BindView(R.id.layout)
    LinearLayout mLayout;

    @BindView(R.id.textView_detected_time)
    TextView mDetectedTimeText;

    private View mMapView;

    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracking, container, false);

        ButterKnife.bind(this, view);

        MainActivity.getUserComponent(this).inject(this);

        mPresenter.onCreateView(this);

        setHasOptionsMenu(true);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.tracking_map);
        supportMapFragment.getMapAsync(this);

        mMapView = supportMapFragment.getView();
        if (mMapView != null) {
            mMapView.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        activity.setDrawerIndicatorEnabled(false);

        Bundle bundle = getArguments();
        String beaconId = (String) bundle.get(BUNDLE_KEY_BEACON_ID);
        mPresenter.setBeaconId(beaconId);

        getActivity().setTitle(R.string.title_cloud_tracking);

        mPresenter.onActivityCreated();
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
        mMapView.setVisibility(View.VISIBLE);

        LatLng latLng = new LatLng(location.latitude, location.longitude);
        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .strokeColor(Colors.PRIMARY)
                .radius(Radius.GOOGLE_MAP);

        mMap.addMarker(new MarkerOptions()
                .position(latLng));
        mMap.addCircle(circleOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        mLayout.removeAllViews();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View emptyView = inflater.inflate(R.layout.not_detected_view, mLayout, false);

        mLayout.addView(emptyView);
    }

    @Override
    public void showDetectedTime(String detectedTime) {
        String time = getContext().getResources().getString(R.string.message_detected_time_format, detectedTime);
        mDetectedTimeText.setText(time);
    }
}
