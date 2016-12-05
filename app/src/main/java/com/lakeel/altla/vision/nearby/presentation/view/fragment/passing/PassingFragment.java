package com.lakeel.altla.vision.nearby.presentation.view.fragment.passing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.lakeel.altla.vision.nearby.presentation.constants.DetectedActivityType;
import com.lakeel.altla.vision.nearby.presentation.constants.Radius;
import com.lakeel.altla.vision.nearby.presentation.constants.WeatherCondition;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PassingModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.WeatherModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.passing.PassingPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.DateFormatter;
import com.lakeel.altla.vision.nearby.presentation.view.PassingView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;

import java.math.BigDecimal;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class PassingFragment extends Fragment implements PassingView, OnMapReadyCallback {

    public static PassingFragment newInstance(String userId, String uniqueKey) {

        Bundle args = new Bundle();
        args.putString(BundleKey.USER_ID.getValue(), userId);
        args.putString(BundleKey.JSON_UNIQUE_KEY.getValue(), uniqueKey);

        PassingFragment fragment = new PassingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    PassingPresenter presenter;

    @BindView(R.id.timesText)
    public TextView textViewTimes;

    @BindView(R.id.dateText)
    public TextView textViewDate;

    @BindView(R.id.weatherText)
    public TextView textViewWeather;

    @BindView(R.id.detectedActivityText)
    public TextView textViewDetectedActivity;

    @BindView(R.id.locationLayout)
    public LinearLayout locationLayout;

    private SupportMapFragment supportMapFragment;

    private View mapView;

    private GoogleMap map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_passing, container, false);

        ButterKnife.bind(this, view);

        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fm = getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.locationMap);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.locationLayout, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);

        Bundle bundle = getArguments();
        String otherUserId = bundle.getString(BundleKey.USER_ID.getValue());
        String uniqueKey = bundle.getString(BundleKey.JSON_UNIQUE_KEY.getValue());

        presenter.setOtherUserId(otherUserId);
        presenter.setRecentlyUniqueKey(uniqueKey);

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
    public void showPassingData(PassingModel model) {
        DateFormatter dateFormatter = new DateFormatter(model.passingTime);
        textViewDate.setText(dateFormatter.format());

        if (model.weather == null) {
            textViewWeather.setText(WeatherCondition.UNKNOWN.getWeather());
        } else {
            WeatherModel weather = model.weather;
            BigDecimal temperature = new BigDecimal(weather.temperature);
            BigDecimal roundUppedTemperature = temperature.setScale(0, BigDecimal.ROUND_HALF_UP);
            int humidity = weather.humidity;
            int conditions[] = weather.conditions;

            StringBuilder builder = new StringBuilder();
            for (int value : conditions) {
                WeatherCondition type = WeatherCondition.toType(value);
                builder.append(type.getWeather());
                builder.append("  ");
            }
            builder.append(getString(R.string.message_temperature_format, String.valueOf(roundUppedTemperature)));
            builder.append("  ");
            builder.append(getString(R.string.message_humidity_format, String.valueOf(humidity)));

            textViewWeather.setText(builder.toString());
        }

        if (model.detectedActivity == null) {
            textViewDetectedActivity.setText(DetectedActivityType.UNKNOWN.getValue());
        } else {
            textViewDetectedActivity.setText(DetectedActivityType.toUserActivity(model.detectedActivity).getValue());
        }
    }

    @Override
    public void showTimes(long times) {
        textViewTimes.setText(String.valueOf(times));
    }

    @Override
    public void hideLocation() {
        locationLayout.setVisibility(View.GONE);
    }

    @Override
    public void showLocationMap(String latitude, String longitude) {
        mapView.setVisibility(View.VISIBLE);

        LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
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
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(14);
        map.moveCamera(cameraUpdate);

        presenter.onMapReady();
    }
}
