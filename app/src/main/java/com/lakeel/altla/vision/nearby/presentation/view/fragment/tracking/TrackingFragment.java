package com.lakeel.altla.vision.nearby.presentation.view.fragment.tracking;

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

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.constants.AppColor;
import com.lakeel.altla.vision.nearby.presentation.constants.BundleKey;
import com.lakeel.altla.vision.nearby.presentation.constants.Radius;
import com.lakeel.altla.vision.nearby.presentation.intent.GoogleMapDirectionIntent;
import com.lakeel.altla.vision.nearby.presentation.presenter.tracking.TrackingPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.DateFormatter;
import com.lakeel.altla.vision.nearby.presentation.view.TrackingView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.transaction.FragmentController;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class TrackingFragment extends Fragment implements TrackingView, OnMapReadyCallback {

    @Inject
    TrackingPresenter presenter;

    @BindView(R.id.trackingLayout)
    LinearLayout mainLayout;

    @BindView(R.id.textView_detected_date)
    TextView detectedDateText;

    private View mapView;

    private GoogleMap map;

    private SupportMapFragment supportMapFragment;

    public static TrackingFragment newInstance(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BundleKey.BEACON_ID.getValue(), id);
        bundle.putSerializable(BundleKey.TARGET_NAME.getValue(), name);

        TrackingFragment fragment = new TrackingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracking, container, false);

        setHasOptionsMenu(true);

        ButterKnife.bind(this, view);

        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.title_cloud_tracking);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);

        FragmentManager fm = getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.tracking_map_layout);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.tracking_map_layout, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);

        Bundle bundle = getArguments();
        String beaconId = (String) bundle.get(BundleKey.BEACON_ID.getValue());
        String beaconName = (String) bundle.get(BundleKey.TARGET_NAME.getValue());
        presenter.setBeaconData(beaconId, beaconName);
    }

    @Override
    public void onResume() {
        super.onResume();

        mapView = supportMapFragment.getView();
        if (mapView != null) {
            mapView.setVisibility(View.INVISIBLE);
        }

        presenter.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        boolean isMenuEnabled = false;
        if (presenter.isMenuEnabled()) {
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
                presenter.onFindNearbyDeviceMenuClicked();
                break;
            case R.id.directions:
                presenter.onDirectionMenuClicked();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(14);
        map.moveCamera(cameraUpdate);

        presenter.onMapReady();
    }

    @Override
    public void showLocationMap(GeoLocation location) {
        mapView.setVisibility(View.VISIBLE);

        LatLng latLng = new LatLng(location.latitude, location.longitude);
        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .strokeColor(AppColor.PRIMARY)
                .radius(Radius.GOOGLE_MAP);

        map.addMarker(new MarkerOptions()
                .position(latLng));
        map.addCircle(circleOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void showEmptyView() {
        mainLayout.removeAllViews();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View emptyView = inflater.inflate(R.layout.not_detected_view, mainLayout, false);

        mainLayout.addView(emptyView);
    }

    @Override
    public void showDetectedDate(long detectedTime) {
        DateFormatter formatter = new DateFormatter(detectedTime);
        String formattedDate = formatter.format();
        String time = getContext().getResources().getString(R.string.message_detected_date_format, formattedDate);
        detectedDateText.setText(time);
    }

    @Override
    public void launchGoogleMapApp(String latitude, String longitude) {
        GoogleMapDirectionIntent directionIntent = new GoogleMapDirectionIntent(latitude, longitude);
        Intent intent = directionIntent.create();
        startActivity(intent);
    }

    @Override
    public void showOptionMenu() {
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void showFindNearbyDeviceFragment(ArrayList<String> beaconIds, String beaconName) {
        FragmentController controller = new FragmentController(getActivity().getSupportFragmentManager());
        controller.showDistanceEstimationFragment(beaconIds, beaconName);
    }
}
