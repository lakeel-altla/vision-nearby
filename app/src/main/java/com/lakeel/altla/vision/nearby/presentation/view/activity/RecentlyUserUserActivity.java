package com.lakeel.altla.vision.nearby.presentation.view.activity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.application.App;
import com.lakeel.altla.vision.nearby.presentation.constants.ActivityType;
import com.lakeel.altla.vision.nearby.presentation.constants.Colors;
import com.lakeel.altla.vision.nearby.presentation.constants.Radius;
import com.lakeel.altla.vision.nearby.presentation.constants.WeatherType;
import com.lakeel.altla.vision.nearby.presentation.di.component.UserComponent;
import com.lakeel.altla.vision.nearby.presentation.di.module.ActivityModule;
import com.lakeel.altla.vision.nearby.presentation.intent.IntentExtra;
import com.lakeel.altla.vision.nearby.presentation.intent.RecentlyIntentData;
import com.lakeel.altla.vision.nearby.presentation.intent.RecentlyIntentData.Weather;
import com.lakeel.altla.vision.nearby.presentation.presenter.activity.RecentlyUserActivityPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.ItemModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PresenceModel;
import com.lakeel.altla.vision.nearby.presentation.view.DateFormatter;
import com.lakeel.altla.vision.nearby.presentation.view.GridShareSheet;
import com.lakeel.altla.vision.nearby.presentation.view.RecentlyUserActivityView;
import com.lakeel.altla.vision.nearby.presentation.view.layout.DateLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.EmailLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.LastOnlineLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.LineUrlLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.LocationLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.NameLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.PassingHeaderLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.PresenceHeaderLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.PresenceLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.ProfileHeaderLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.SNSHeaderLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.TimesLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.UserActivityLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.WeatherLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.math.BigDecimal;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lakeel.altla.vision.nearby.R.id.share;

public final class RecentlyUserUserActivity extends AppCompatActivity implements RecentlyUserActivityView, OnMapReadyCallback {

    @BindView(R.id.main_layout)
    CoordinatorLayout mMainLayout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.imageView_profile)
    ImageView mImageView;

    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    @BindView(R.id.shareSheet)
    BottomSheetLayout mShareSheet;

    @Inject
    RecentlyUserActivityPresenter mPresenter;

    private GoogleMap mMap;

    private PresenceHeaderLayout mPresenceHeaderLayout = new PresenceHeaderLayout();

    private PresenceLayout mPresenceLayout = new PresenceLayout();

    private LastOnlineLayout mLastOnlineLayout = new LastOnlineLayout();

    private PassingHeaderLayout mPassingHeaderLayout = new PassingHeaderLayout();

    private WeatherLayout mWeatherLayout = new WeatherLayout();

    private DateLayout mDateLayout = new DateLayout();

    private UserActivityLayout mUserActivityLayout = new UserActivityLayout();

    private TimesLayout mTimesLayout = new TimesLayout();

    private LocationLayout mLocationLayout = new LocationLayout();

    private ProfileHeaderLayout mProfileHeaderLayout = new ProfileHeaderLayout();

    private NameLayout mNameLayout = new NameLayout();

    private EmailLayout mEmailLayout = new EmailLayout();

    private SNSHeaderLayout mSNSHeaderLayout = new SNSHeaderLayout();

    private LineUrlLayout mLineUrlLayout = new LineUrlLayout();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_user);

        setTitle(null);

        ButterKnife.bind(this);
        ButterKnife.bind(mPresenceHeaderLayout, findViewById(R.id.presence_header));
        ButterKnife.bind(mPresenceLayout, findViewById(R.id.presence));
        ButterKnife.bind(mLastOnlineLayout, findViewById(R.id.last_online));
        ButterKnife.bind(mPassingHeaderLayout, findViewById(R.id.passing_header));
        ButterKnife.bind(mDateLayout, findViewById(R.id.date));
        ButterKnife.bind(mWeatherLayout, findViewById(R.id.weather));
        ButterKnife.bind(mUserActivityLayout, findViewById(R.id.detected_activity));
        ButterKnife.bind(mTimesLayout, findViewById(R.id.times));
        ButterKnife.bind(mLocationLayout, findViewById(R.id.location));
        ButterKnife.bind(mProfileHeaderLayout, findViewById(R.id.profile_header));
        ButterKnife.bind(mNameLayout, findViewById(R.id.name));
        ButterKnife.bind(mEmailLayout, findViewById(R.id.email));
        ButterKnife.bind(mSNSHeaderLayout, findViewById(R.id.sns_header));
        ButterKnife.bind(mLineUrlLayout, findViewById(R.id.lineUrl));

        // Dagger。
        UserComponent userComponent = App.getApplicationComponent(this)
                .userComponent(new ActivityModule(this));
        userComponent.inject(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPresenter.onCreateView(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        // Handle an intent.
        Intent intent = getIntent();
        RecentlyIntentData data = (RecentlyIntentData) intent.getSerializableExtra(IntentExtra.RECENTLY.name());
        mPresenter.setData(data);

        mPresenceHeaderLayout.mTitle.setText(R.string.textView_presence);
        mPresenceLayout.mTitle.setText(R.string.textView_state);
        mLastOnlineLayout.mTitle.setText(R.string.textView_last_online);
        mPassingHeaderLayout.mTitle.setText(R.string.textView_passing);
        mDateLayout.mTitle.setText(R.string.textView_date);
        mWeatherLayout.mTitle.setText(R.string.textView_weather);
        mUserActivityLayout.mTitle.setText(R.string.textView_detected_activity);
        mTimesLayout.mTitle.setText(R.string.textView_number_of_times);
        mProfileHeaderLayout.mTitle.setText(R.string.textView_profile);
        mNameLayout.mTitle.setText(R.string.textView_name);
        mEmailLayout.mTitle.setText(R.string.textView_email);
        mSNSHeaderLayout.mTitle.setText(R.string.textView_sns);
        mLineUrlLayout.mTitle.setText(R.string.textView_line_url);

        DateFormatter dateFormatter = new DateFormatter(data.mTimestamp);
        mDateLayout.mBody.setText(dateFormatter.format());

        if (data.mWeather == null) {
            mWeatherLayout.mBody.setText(WeatherType.CONDITION_UNKNOWN.getWeather());
        } else {
            Weather weather = data.mWeather;
            BigDecimal temperature = new BigDecimal(weather.mTemperature);
            BigDecimal roundUppedTemperature = temperature.setScale(0, BigDecimal.ROUND_HALF_UP);
            int humidity = weather.mHumidity;
            int conditions[] = weather.mConditions;

            StringBuilder builder = new StringBuilder();
            for (int value : conditions) {
                WeatherType type = WeatherType.toType(value);
                builder.append(type.getWeather());
                builder.append("  ");
            }
            builder.append(getString(R.string.message_temperature, String.valueOf(roundUppedTemperature)));
            builder.append("  ");
            builder.append(getString(R.string.message_humidity, String.valueOf(humidity)));

            mWeatherLayout.mBody.setText(builder.toString());
        }

        mUserActivityLayout.mBody.setText(ActivityType.toUserActivity(data.mUserActivity).getActivity());
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        boolean isCmLinkEnabled = mPresenter.isCmLinkEnabled();

        if (!isCmLinkEnabled) {
            menu.setGroupVisible(0, false);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case share:
                mPresenter.onShare();
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
    public void showTitle(String title) {
        mCollapsingToolbarLayout.setTitle(title);
    }

    @Override
    public void showSnackBar(int resId) {
        Snackbar.make(mMainLayout, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showPresence(PresenceModel model) {
        if (model.mConnected) {
            mPresenceLayout.mBody.setText(R.string.textView_connected);
        } else {
            mPresenceLayout.mBody.setText(R.string.textView_disconnected);
        }

        DateFormatter dateFormatter = new DateFormatter(model.mLastOnline);
        mLastOnlineLayout.mBody.setText(dateFormatter.format());
    }

    @Override
    public void showLocationMap(String latitude, String longitude) {
        LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
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
    public void showLocationText(String text) {
        mLocationLayout.mLocation.setText(text);
    }

    @Override
    public void hideLocation() {
        mLocationLayout.mLayout.setVisibility(View.GONE);
    }

    @Override
    public void showTimes(long times) {
        mTimesLayout.mBody.setText(String.valueOf(times));
    }

    @OnClick(R.id.fab)
    public void onAdd() {
        mPresenter.onAdd();
    }

    @Override
    public void showProfile(ItemModel model) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(model.mImageUri, mImageView);

        mNameLayout.mBody.setText(model.mName);
        mEmailLayout.mBody.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        mEmailLayout.mBody.setText(model.mEmail);
    }

    @Override
    public void showAddButton() {
        mFloatingActionButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAddButton() {
        mFloatingActionButton.setVisibility(View.GONE);
    }

    @Override
    public void initializeOptionMenu() {
        invalidateOptionsMenu();
    }

    @Override
    public void showLineUrl(String url) {
        mLineUrlLayout.mBody.setAutoLinkMask(Linkify.WEB_URLS);
        mLineUrlLayout.mBody.setText(url);
    }

    @Override
    public void showShareSheet() {
        GridShareSheet shareSheet = new GridShareSheet(getApplicationContext(), mShareSheet, R.menu.menu_share);
        shareSheet.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_cm_favorites) {
                mPresenter.onAddToCmFavorites();
            }
            return true;
        });

        if (!mPresenter.isCmLinkEnabled()) {
            shareSheet.hideMenuItem(R.id.menu_cm_favorites);
        }

        shareSheet.show();
    }
}
