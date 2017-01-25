package com.lakeel.altla.vision.nearby.presentation.view.fragment.passing;

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
import com.lakeel.altla.vision.nearby.presentation.awareness.UserActivity;
import com.lakeel.altla.vision.nearby.presentation.awareness.WeatherCondition;
import com.lakeel.altla.vision.nearby.presentation.bundle.FragmentBundle;
import com.lakeel.altla.vision.nearby.presentation.color.AppColor;
import com.lakeel.altla.vision.nearby.presentation.map.Radius;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.UserPassingModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.user.PassingUserPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.PassingUserView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.date.DateFormatter;
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

public final class PassingUserFragment extends Fragment implements PassingUserView, OnMapReadyCallback {

    @Inject
    PassingUserPresenter presenter;

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

    public static PassingUserFragment newInstance(String historyId) {
        Bundle args = new Bundle();
        args.putString(FragmentBundle.HISTORY_ID.name(), historyId);

        PassingUserFragment fragment = new PassingUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_passing_user, container, false);

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

        FragmentManager fragmentManager = getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.locationMap);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.locationLayout, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);

        Bundle bundle = getArguments();
        String historyId = (String) bundle.get(FragmentBundle.HISTORY_ID.name());
        presenter.setHistoryId(historyId);

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
    public void showTitle(String title) {
        getActivity().setTitle(title);
    }

    @Override
    public void showProfile(UserPassingModel model) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(model.imageUri, userImageView);

        profileLayout.userNameTextView.setText(model.userName);
        profileLayout.emailTextView.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        profileLayout.emailTextView.setText(model.email);
    }

    @Override
    public void showTimes(long times) {
        passingLayout.timesTextView.setText(String.valueOf(times));
    }

    @Override
    public void showPassingData(UserPassingModel model) {
        DateFormatter dateFormatter = new DateFormatter(model.passingTime);
        passingLayout.passingTimeTextView.setText(dateFormatter.format());

        if (model.conditions == null || model.conditions.length == 0) {
            int resId = WeatherCondition.UNKNOWN.getResValue();
            passingLayout.weatherTextView.setText(getContext().getString(resId));
        } else {
            BigDecimal temperature = new BigDecimal(model.temperature);
            BigDecimal roundUppedTemperature = temperature.setScale(0, BigDecimal.ROUND_HALF_UP);
            int humidity = model.humidity;
            int conditions[] = model.conditions;

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

            passingLayout.weatherTextView.setText(builder.toString());
        }

        int resId = UserActivity.toUserActivity(model.userActivity).getResValue();
        passingLayout.userActivityTextView.setText(getContext().getString(resId));
    }

    @Override
    public void showPresence(UserPassingModel model) {
        int resId;
        if (model.isConnected) {
            resId = R.string.textView_connected;
        } else {
            resId = R.string.textView_disconnected;
        }
        presenceLayout.presenceTextView.setText(resId);

        DateFormatter dateFormatter = new DateFormatter(model.lastOnlineTime);
        presenceLayout.lastOnlineTextView.setText(dateFormatter.format());
    }

    @Override
    public void showLocationMap(String latitude, String longitude) {
        passingLayout.locationLayout.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.addButton)
    public void onAdd() {
        presenter.onAdd();
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
        snsLayout.lineUrlTextView.setAutoLinkMask(Linkify.WEB_URLS);
        snsLayout.lineUrlTextView.setText(url);
    }

    @Override
    public void showSnackBar(int resId) {
        Snackbar.make(mainLayout, resId, Snackbar.LENGTH_SHORT).show();
    }
}
