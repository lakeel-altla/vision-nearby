package com.lakeel.altla.vision.nearby.presentation.view.fragment.user;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import com.lakeel.altla.vision.nearby.presentation.constants.DetectedActivity;
import com.lakeel.altla.vision.nearby.presentation.constants.FragmentBundle;
import com.lakeel.altla.vision.nearby.presentation.constants.Radius;
import com.lakeel.altla.vision.nearby.presentation.constants.WeatherCondition;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PresenceModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.UserModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.user.UserPassingPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.DateFormatter;
import com.lakeel.altla.vision.nearby.presentation.view.UserPassingView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.bundle.HistoryBundle;
import com.lakeel.altla.vision.nearby.presentation.view.bundle.WeatherBundle;
import com.lakeel.altla.vision.nearby.presentation.view.layout.PassingLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.PresenceLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.ProfileLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.SnsLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class UserPassingFragment extends Fragment implements UserPassingView, OnMapReadyCallback {

    @Inject
    UserPassingPresenter presenter;

    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;

    @BindView(R.id.imageViewUser)
    ImageView userImageView;

    @BindView(R.id.addButton)
    FloatingActionButton addButton;

    private PresenceLayout presenceLayout = new PresenceLayout();

    private PassingLayout passingLayout = new PassingLayout();

    private ProfileLayout profileLayout = new ProfileLayout();

    private SnsLayout snsLayout = new SnsLayout();

    private GoogleMap map;

    private SupportMapFragment supportMapFragment;

    private View mapView;

    public static UserPassingFragment newInstance(HistoryBundle data) {
        Bundle args = new Bundle();
        args.putSerializable(FragmentBundle.HISTORY.name(), data);

        UserPassingFragment fragment = new UserPassingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_passing, container, false);

        ButterKnife.bind(this, view);
        ButterKnife.bind(presenceLayout, view.findViewById(R.id.presenceLayout));
        ButterKnife.bind(passingLayout, view.findViewById(R.id.passingLayout));
        ButterKnife.bind(profileLayout, view.findViewById(R.id.profileLayout));
        ButterKnife.bind(snsLayout, view.findViewById(R.id.snsLayout));

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);

        FragmentManager fm = getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.locationMap);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.locationLayout, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);

        Bundle bundle = getArguments();
        HistoryBundle bundleData = (HistoryBundle) bundle.getSerializable(FragmentBundle.HISTORY.name());
        if (bundleData == null) {
            throw new IllegalStateException("Bundle data is not set.");
        }

        presenter.setUserLocationData(bundleData.userId, bundleData.latitude, bundleData.longitude);

        getActivity().setTitle(bundleData.userName);

        DateFormatter dateFormatter = new DateFormatter(bundleData.timestamp);
        passingLayout.textViewDate.setText(dateFormatter.format());

        if (bundleData.weatherBundle == null) {
            int resId = WeatherCondition.UNKNOWN.getResValue();
            passingLayout.textViewWeather.setText(getContext().getString(resId));
        } else {
            WeatherBundle weatherBundle = bundleData.weatherBundle;
            BigDecimal temperature = new BigDecimal(weatherBundle.temperature);
            BigDecimal roundUppedTemperature = temperature.setScale(0, BigDecimal.ROUND_HALF_UP);

            int humidity = weatherBundle.humidity;
            int conditions[] = weatherBundle.conditions;

            StringBuilder builder = new StringBuilder();
            for (int value : conditions) {
                WeatherCondition type = WeatherCondition.toWeatherCondition(value);
                int resId = type.getResValue();
                builder.append(getContext().getString(resId));
                builder.append("  ");
            }
            builder.append(getString(R.string.message_temperature_format, String.valueOf(roundUppedTemperature)));
            builder.append("  ");
            builder.append(getString(R.string.message_humidity_format, String.valueOf(humidity)));

            passingLayout.textViewWeather.setText(builder.toString());
        }

        int resId = DetectedActivity.toDetectedActivity(bundleData.detectedActivity).getResValue();
        passingLayout.textViewDetectedActivity.setText(getContext().getString(resId));

        presenter.onActivityCreated();
    }

    @Override
    public void onResume() {
        super.onResume();

        mapView = supportMapFragment.getView();
        if (mapView != null) {
            mapView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
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
    public void showSnackBar(int resId) {
        Snackbar.make(mainLayout, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showPresence(PresenceModel model) {
        int resId;
        if (model.isConnected) {
            resId = R.string.textView_connected;
        } else {
            resId = R.string.textView_disconnected;
        }
        presenceLayout.textViewPresence.setText(resId);

        DateFormatter dateFormatter = new DateFormatter(model.lastOnlineTime);
        presenceLayout.textViewLastOnline.setText(dateFormatter.format());
    }

    @Override
    public void showLocationMap(String latitude, String longitude) {
        mapView.setVisibility(View.VISIBLE);

        LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
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
    public void hideLocation() {
        passingLayout.locationLayout.setVisibility(View.GONE);
    }

    @Override
    public void showTimes(long times) {
        passingLayout.textViewTimes.setText(String.valueOf(times));
    }

    @OnClick(R.id.addButton)
    public void onAdd() {
        presenter.onAdd();
    }

    @Override
    public void showProfile(UserModel model) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(model.imageUri, userImageView);

        profileLayout.textViewName.setText(model.name);
        profileLayout.textViewEmail.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        profileLayout.textViewEmail.setText(model.email);
    }

    @Override
    public void showAddButton() {
        addButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAddButton() {
        addButton.setVisibility(View.GONE);
    }

    @Override
    public void showLineUrl(String url) {
        snsLayout.textViewLineUrl.setAutoLinkMask(Linkify.WEB_URLS);
        snsLayout.textViewLineUrl.setText(url);
    }
}
