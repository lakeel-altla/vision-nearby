package com.lakeel.altla.vision.nearby.presentation.view.fragment.tracking;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.firebase.geofire.GeoLocation;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.constants.BundleKey;
import com.lakeel.altla.vision.nearby.presentation.constants.Colors;
import com.lakeel.altla.vision.nearby.presentation.constants.Radius;
import com.lakeel.altla.vision.nearby.presentation.intent.GoogleMapIntent;
import com.lakeel.altla.vision.nearby.presentation.presenter.tracking.TrackingPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.TrackingView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.transaction.FragmentController;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class TrackingFragment extends Fragment implements TrackingView, OnMapReadyCallback {

    public static TrackingFragment newInstance(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BundleKey.BEACON_ID.getValue(), id);
        bundle.putSerializable(BundleKey.TARGET_NAME.getValue(), name);

        TrackingFragment fragment = new TrackingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Inject
    TrackingPresenter mPresenter;

    @BindView(R.id.trackingLayout)
    LinearLayout mLayout;

    @BindView(R.id.textView_detected_date)
    TextView mDetectedDateText;

    private View mMapView;

    private GoogleMap mMap;

    private SupportMapFragment mSupportMapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracking, container, false);

        setHasOptionsMenu(true);

        ButterKnife.bind(this, view);

        MainActivity.getUserComponent(this).inject(this);

        mPresenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.title_cloud_tracking);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);

        FragmentManager fm = getChildFragmentManager();
        mSupportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.tracking_map_layout);
        if (mSupportMapFragment == null) {
            mSupportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.tracking_map_layout, mSupportMapFragment).commit();
        }
        mSupportMapFragment.getMapAsync(this);

        Bundle bundle = getArguments();
        String beaconId = (String) bundle.get(BundleKey.BEACON_ID.getValue());
        String beaconName = (String) bundle.get(BundleKey.TARGET_NAME.getValue());
        mPresenter.setBeaconData(beaconId, beaconName);
    }

    @Override
    public void onResume() {
        super.onResume();

        mMapView = mSupportMapFragment.getView();
        if (mMapView != null) {
            mMapView.setVisibility(View.INVISIBLE);
        }

        mPresenter.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        boolean isMenuEnabled = false;
        if (mPresenter.isMenuEnabled()) {
            isMenuEnabled = true;
        }

        MenuItem findItem = menu.findItem(R.id.find);
        findItem.setVisible(isMenuEnabled);

        MenuItem directionsItem = menu.findItem(R.id.directions);
        directionsItem.setVisible(isMenuEnabled);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tracking, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            case R.id.find:
                mPresenter.onFindNearbyDeviceMenuClicked();
                break;
            case R.id.directions:
                mPresenter.onDirectionMenuClicked();
                break;
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
    public void showEmptyView() {
        mLayout.removeAllViews();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View emptyView = inflater.inflate(R.layout.not_detected_view, mLayout, false);

        mLayout.addView(emptyView);
    }

    @Override
    public void showDetectedDate(String detectedDate) {
        String time = getContext().getResources().getString(R.string.message_detected_date_format, detectedDate);
        mDetectedDateText.setText(time);
    }

    @Override
    public void launchGoogleMapApp(String latitude, String longitude) {
        GoogleMapIntent mapIntent = new GoogleMapIntent(latitude, longitude);
        Intent intent = mapIntent.create();
        startActivity(intent);
    }

    @Override
    public void showOptionMenu() {
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void showFindNearbyDeviceFragment(ArrayList<String> beaconIds, String beaconName) {
        FragmentController controller = new FragmentController(getActivity().getSupportFragmentManager());
        controller.showDeviceDistanceEstimationFragment(beaconIds, beaconName);
    }
}
