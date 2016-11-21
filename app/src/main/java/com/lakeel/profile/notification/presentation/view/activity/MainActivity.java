package com.lakeel.profile.notification.presentation.view.activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;

import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.android.ConfirmDialog;
import com.lakeel.profile.notification.presentation.application.App;
import com.lakeel.profile.notification.presentation.di.component.UserComponent;
import com.lakeel.profile.notification.presentation.di.module.ActivityModule;
import com.lakeel.profile.notification.presentation.intent.IntentKey;
import com.lakeel.profile.notification.presentation.presenter.activity.ActivityPresenter;
import com.lakeel.profile.notification.presentation.presenter.model.PreferenceModel;
import com.lakeel.profile.notification.presentation.service.PublishService;
import com.lakeel.profile.notification.presentation.view.ActivityView;
import com.lakeel.profile.notification.presentation.view.layout.HeaderLayout;
import com.lakeel.profile.notification.presentation.view.transaction.FragmentController;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActivityView {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);

    private static final int REQUEST_CODE_RESOLVE_CONNECTION = 1;

    private static final int REQUEST_CODE_ENABLE_BLE = 2;

    private static final int REQUEST_CODE_SUBSCRIBE_RESULT = 3;

    private static final int REQUEST_CODE_ACCESS_LOCATION = 5;

    private HeaderLayout mHeaderLayout = new HeaderLayout();

    private UserComponent mUserComponent;

    private ActionBarDrawerToggle mToggle;

    private DrawerLayout drawerLayout;

    private RelativeLayout mFragmentLayout;

    @Inject
    ActivityPresenter mPresenter;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Memo:
        // By the management of the memory by the Android OS, re-construction of Fragment is carried out automatically.
        // But once you create an instance of the Dagger after super#onCreate(),
        // it will be done to rebuild the Fragment in the processing of the super#onCreate.
        // because the NullPointerException occurs, create an instance of the Dagger before super#onCreate().

        // Dagger
        mUserComponent = App.getApplicationComponent(this)
                .userComponent(new ActivityModule(this));
        mUserComponent.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentLayout = ButterKnife.findById(this, R.id.fragmentPlaceholder);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mToggle.syncState();
        drawerLayout.addDrawerListener(mToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        ButterKnife.bind(mHeaderLayout, headerView);

        mPresenter.onCreateView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (REQUEST_CODE_RESOLVE_CONNECTION == requestCode && resultCode == RESULT_OK) {
            if (mPresenter.isAccessLocationGranted()) {
                mPresenter.onConnect();
            } else {
                LOGGER.warn("Can not connect to nearby. Not granted for location permission.");
            }
        } else if (REQUEST_CODE_SUBSCRIBE_RESULT == requestCode && resultCode == RESULT_OK) {
            mPresenter.onSubscribeInBackground();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (REQUEST_CODE_ACCESS_LOCATION == requestCode && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            LOGGER.info("Location permission is granted.");
            mPresenter.onAccessLocationGranted();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_favorites: {
                FragmentController controller = new FragmentController(getSupportFragmentManager());
                controller.showFavoritesListFragment();
                break;
            }
            case R.id.nav_recently: {
                FragmentController controller = new FragmentController(getSupportFragmentManager());
                controller.showRecentlyFragment();
                break;
            }
            case R.id.nav_nearby: {
                FragmentController controller = new FragmentController(getSupportFragmentManager());
                controller.showNearbyListFragment();
                break;
            }
            case R.id.nav_settings: {
                FragmentController controller = new FragmentController(getSupportFragmentManager());
                controller.showSettingsFragment();
                break;
            }
            case R.id.nav_sign_out:
                mPresenter.onSignOut(this);
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showConnectedResolutionSystemDialog(ConnectionResult connectionResult) {
        try {
            connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_CONNECTION);
        } catch (IntentSender.SendIntentException e) {
            LOGGER.error("Could not resolve to connect nearby");
        }
    }

    @Override
    public void showSignInFragment() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        FragmentController fragmentController = new FragmentController(getSupportFragmentManager());
        fragmentController.showSignInFragment();
    }

    @Override
    public void showProfile(String displayName, String email, String imageUri) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imageUri, mHeaderLayout.mProfile);

        mHeaderLayout.mUserName.setText(displayName);
        mHeaderLayout.mUserAddress.setText(email);
    }

    @Override
    public void showSnackBar(int resId) {
        Snackbar.make(mFragmentLayout, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showPublishDisableDialog() {
        ConfirmDialog dialog = new ConfirmDialog(MainActivity.this, R.string.message_advertise_disable);
        dialog.show();
    }

    @Override
    public void showBleEnabledActivity() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BLE);
    }

    @Override
    public void startPublishService(PreferenceModel model) {
        // Start publish service in background.
        Intent intent = new Intent(getApplicationContext(), PublishService.class);
        intent.putExtra(IntentKey.NAMESPACE_ID.name(), model.mNamespaceId);
        intent.putExtra(IntentKey.INSTANCE_ID.name(), model.mInstanceId);
        getApplicationContext().startService(intent);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void showAccessFineLocationPermissionSystemDialog() {
        MainActivity.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ACCESS_LOCATION);
    }

    @Override
    public void showResolutionSystemDialog(Status status) {
        try {
            status.startResolutionForResult(MainActivity.this, REQUEST_CODE_SUBSCRIBE_RESULT);
        } catch (IntentSender.SendIntentException e) {
            LOGGER.error("Failed to show resolution dialog for nearby.", e);
        }
    }

    @Override
    public void showFavoritesListFragment() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        FragmentController fragmentController = new FragmentController(getSupportFragmentManager());
        fragmentController.showFavoritesListFragment();
    }

    public void setDrawerIndicatorEnabled(boolean enabled) {
        mToggle.setDrawerIndicatorEnabled(enabled);
    }

    public void onSubscribe() {
        mPresenter.onSubscribeInBackground();
    }

    public void onUnSubscribe() {
        mPresenter.onUnSubscribe();
    }

    public static UserComponent getUserComponent(@NonNull Fragment fragment) {
        return ((MainActivity) fragment.getActivity()).mUserComponent;
    }

    public void onSignedIn() {
        mPresenter.onSignedIn();
    }

    public void showTrackingFragment(String id, String beaconName) {
        FragmentController fragmentController = new FragmentController(getSupportFragmentManager());
        fragmentController.showTrackingFragment(id, beaconName);
    }

    public void showFindNearbyDeviceFragment(String beaconId, String beaconName) {
        FragmentController fragmentController = new FragmentController(getSupportFragmentManager());
        fragmentController.showDistanceEstimationFragment(beaconId, beaconName);
    }

    public void showDeviceListFragment() {
        FragmentController fragmentController = new FragmentController(getSupportFragmentManager());
        fragmentController.showDeviceListFragment();
    }

    public void showBleSettingsFragment() {
        FragmentController fragmentController = new FragmentController(getSupportFragmentManager());
        fragmentController.showBleSettingsFragment();
    }

    public void showLineSettingsFragment() {
        FragmentController fragmentController = new FragmentController(getSupportFragmentManager());
        fragmentController.showLineSettingsFragment();
    }

    public void showCmSettingsFragment() {
        FragmentController fragmentController = new FragmentController(getSupportFragmentManager());
        fragmentController.showCmSettingsFragment();
    }
}
