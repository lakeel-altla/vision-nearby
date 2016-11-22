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
import com.lakeel.altla.vision.nearby.android.ConfirmDialog;
import com.lakeel.altla.vision.nearby.presentation.constants.BundleKey;
import com.lakeel.altla.vision.nearby.presentation.constants.Colors;
import com.lakeel.altla.vision.nearby.presentation.constants.Radius;
import com.lakeel.altla.vision.nearby.presentation.presenter.tracking.TrackingPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.TrackingView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.transaction.FragmentController;

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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class TrackingFragment extends Fragment implements TrackingView, OnMapReadyCallback {

    public static TrackingFragment newInstance(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BundleKey.BEACON_ID.getValue(), id);
        bundle.putSerializable(BundleKey.BEACON_NAME.getValue(), name);

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

        ButterKnife.bind(this, view);

        MainActivity.getUserComponent(this).inject(this);

        mPresenter.onCreateView(this);

        setHasOptionsMenu(true);

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
        String beaconName = (String) bundle.get(BundleKey.BEACON_NAME.getValue());
        mPresenter.setBeaconData(beaconId, beaconName);

        mPresenter.onActivityCreated();
    }

    @Override
    public void onResume() {
        super.onResume();

        mMapView = mSupportMapFragment.getView();
        if (mMapView != null) {
            mMapView.setVisibility(View.INVISIBLE);
        }
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

        mMap.setOnMarkerClickListener(marker -> {
            mPresenter.onMarkerClick();
            return false;
        });

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
    public void showFindNearbyDeviceConfirmDialog() {
        ConfirmDialog dialog = new ConfirmDialog(getActivity(), R.string.dialog_message_confirm_find_nearby_devices);
        dialog.setOnPositiveListener((dialog1, which) -> mPresenter.onFindNearbyDeviceDialogClicked());
        dialog.show();
    }

    @Override
    public void showFindNearbyDeviceFragment(String beaconId, String beaconName) {
        FragmentController controller = new FragmentController(getActivity().getSupportFragmentManager());
        controller.showDeviceDistanceEstimationFragment(beaconId, beaconName);
    }
}
