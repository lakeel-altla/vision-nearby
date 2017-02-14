package com.lakeel.altla.vision.nearby.presentation.view.fragment.favorite;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.presentation.awareness.UserActivity;
import com.lakeel.altla.vision.nearby.presentation.awareness.WeatherCondition;
import com.lakeel.altla.vision.nearby.presentation.presenter.favorite.FavoriteUserPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteUserModel;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteUserView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.color.AppColor;
import com.lakeel.altla.vision.nearby.presentation.view.date.DateFormatter;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.FragmentController;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.EstimationTarget;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.FavoriteUser;
import com.lakeel.altla.vision.nearby.presentation.view.layout.PassingLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.PresenceLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.ProfileLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.SnsLayout;
import com.lakeel.altla.vision.nearby.presentation.view.map.Radius;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lakeel.altla.vision.nearby.R.id.estimate;

public final class FavoriteUserFragment extends Fragment implements OnMapReadyCallback, FavoriteUserView {

    @Inject
    FavoriteUserPresenter presenter;

    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;

    @BindView(R.id.imageViewUser)
    ImageView userImageView;

    private static final String BUNDLE_FAVORITE_USER = "favoriteUser";

    private final PresenceLayout presenceLayout = new PresenceLayout();

    private final ProfileLayout profileLayout = new ProfileLayout();

    private final PassingLayout passingLayout = new PassingLayout();

    private final SnsLayout snsLayout = new SnsLayout();

    private GoogleMap map;

    private SupportMapFragment supportMapFragment;

    private View mapView;

    public static FavoriteUserFragment newInstance(FavoriteUser favoriteUser) {
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_FAVORITE_USER, favoriteUser);

        FavoriteUserFragment fragment = new FavoriteUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_user, container, false);

        ButterKnife.bind(this, view);

        ButterKnife.bind(presenceLayout, view.findViewById(R.id.presenceLayout));
        ButterKnife.bind(profileLayout, view.findViewById(R.id.profileLayout));
        ButterKnife.bind(passingLayout, view.findViewById(R.id.passingLayout));
        ButterKnife.bind(snsLayout, view.findViewById(R.id.snsLayout));

        setHasOptionsMenu(true);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this, getArguments());

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);

        FragmentManager fragmentManager = getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.layoutLocationMap);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.layoutLocationMap, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);

        presenter.onActivityCreated();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favorite_user, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case estimate:
                presenter.onEstimateDistanceMenuClick();
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
    public void updateModel(FavoriteUserModel model) {
        // State
        int connectionResId;
        if (model.isConnected) {
            connectionResId = R.string.textView_connected;
        } else {
            connectionResId = R.string.textView_disconnected;
        }
        presenceLayout.presenceTextView.setText(connectionResId);
        presenceLayout.lastOnlineTextView.setText(new DateFormatter(model.lastOnlineTime).format());

        // Profile
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(model.imageUri, userImageView);

        profileLayout.userNameTextView.setText(model.userName);

        profileLayout.emailTextView.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        profileLayout.emailTextView.setText(model.email);

        // Passing
        passingLayout.passingTimeTextView.setText(new DateFormatter(model.passingTime).format());

        if (model.weatherModel == null) {
            int weatherResId = WeatherCondition.UNKNOWN.getResValue();
            passingLayout.weatherTextView.setText(getContext().getString(weatherResId));
        } else {
            BigDecimal temperature = new BigDecimal(model.weatherModel.temperature);
            BigDecimal roundUppedTemperature = temperature.setScale(0, BigDecimal.ROUND_HALF_UP);
            int humidity = model.weatherModel.humidity;
            int conditions[] = model.weatherModel.conditions;

            StringBuilder builder = new StringBuilder();
            for (int value : conditions) {
                WeatherCondition type = WeatherCondition.toWeatherCondition(value);
                int resId = type.getResValue();
                builder.append(getContext().getString(resId));
                builder.append("  ");
            }

            builder.append(getString(R.string.textView_temperature_format, String.valueOf(roundUppedTemperature)));
            builder.append("  ");
            builder.append(getString(R.string.snackBar_message_humidity_format, String.valueOf(humidity)));

            passingLayout.weatherTextView.setText(builder.toString());
        }

        int userActivityResId = UserActivity.toUserActivity(model.userActivity).getResValue();
        passingLayout.userActivityTextView.setText(getContext().getString(userActivityResId));

        passingLayout.timesTextView.setText(String.valueOf(model.times));

        // SNS
        if (!StringUtils.isEmpty(model.lineUrl)) {
            snsLayout.lineUrlTextView.setText(model.lineUrl);
        }
    }

    @Override
    public void showLocation(String latitude, String longitude) {
        // Show unknown text.
        passingLayout.locationMapLayout.setVisibility(View.VISIBLE);
        passingLayout.unknownTextView.setVisibility(View.GONE);

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
        passingLayout.locationMapLayout.setVisibility(View.GONE);
        passingLayout.unknownTextView.setVisibility(View.VISIBLE);
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
