package com.lakeel.altla.vision.nearby.presentation.view.fragment.tracking;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
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
import com.lakeel.altla.vision.nearby.presentation.presenter.tracking.TrackingPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.TrackingView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.color.AppColor;
import com.lakeel.altla.vision.nearby.presentation.view.date.DateFormatter;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.FragmentController;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.EstimationTarget;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.TrackingBeacon;
import com.lakeel.altla.vision.nearby.presentation.view.intent.GoogleMapIntent;
import com.lakeel.altla.vision.nearby.presentation.view.map.Radius;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class TrackingFragment extends Fragment implements TrackingView, OnMapReadyCallback {

    @Inject
    TrackingPresenter presenter;

    @BindView(R.id.trackingLayout)
    LinearLayout mainLayout;

    @BindView(R.id.textViewFoundDate)
    TextView foundDateTextView;

    private static final String BUNDLE_TRACKING_BEACON = "trackingBeacon";

    private View mapView;

    private GoogleMap map;

    private SupportMapFragment supportMapFragment;

    public static TrackingFragment newInstance(TrackingBeacon trackingBeacon) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_TRACKING_BEACON, trackingBeacon);

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

        presenter.onCreateView(this, getArguments());

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.toolbar_title_tracking);

        MainActivity activity = ((MainActivity) getActivity());
        activity.setDrawerIndicatorEnabled(false);

        FragmentManager fm = getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.layoutTrackingMap);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.layoutTrackingMap, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);
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
                presenter.onDistanceEstimationMenuClick();
                break;
            case R.id.directions:
                presenter.onDirectionMenuClick();
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
                .strokeColor(AppColor.PRIMARY.getValue())
                .radius(Radius.GOOGLE_MAP.getValue());

        map.addMarker(new MarkerOptions()
                .position(latLng));
        map.addCircle(circleOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void showEmptyView() {
        mainLayout.removeAllViews();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View emptyView = inflater.inflate(R.layout.not_found_view, mainLayout, false);

        mainLayout.addView(emptyView);
    }

    @Override
    public void showFoundDate(long foundTime) {
        DateFormatter formatter = new DateFormatter(foundTime);
        String formattedDate = formatter.format();
        String time = getContext().getResources().getString(R.string.snackBar_message_found_date_format, formattedDate);
        foundDateTextView.setText(time);
    }

    @Override
    public void launchGoogleMapApp(GoogleMapIntent intent) {
        startActivity(intent);
    }

    @Override
    public void showOptionMenu() {
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void showDistanceEstimationFragment(EstimationTarget target) {
        FragmentController controller = new FragmentController(this);
        controller.showDistanceEstimationFragment(target);
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        Snackbar.make(mainLayout, resId, Snackbar.LENGTH_SHORT).show();
    }
}
