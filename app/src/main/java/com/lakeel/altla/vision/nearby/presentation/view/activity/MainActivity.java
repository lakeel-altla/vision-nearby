package com.lakeel.altla.vision.nearby.presentation.view.activity;

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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.android.ConfirmDialog;
import com.lakeel.altla.vision.nearby.presentation.application.App;
import com.lakeel.altla.vision.nearby.presentation.di.component.ViewComponent;
import com.lakeel.altla.vision.nearby.presentation.di.module.ActivityModule;
import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;
import com.lakeel.altla.vision.nearby.presentation.presenter.activity.ActivityPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PreferenceModel;
import com.lakeel.altla.vision.nearby.presentation.service.AdvertiseService;
import com.lakeel.altla.vision.nearby.presentation.view.ActivityView;
import com.lakeel.altla.vision.nearby.presentation.view.layout.DrawerHeaderLayout;
import com.lakeel.altla.vision.nearby.presentation.view.transaction.FragmentController;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActivityView {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);

    private static final int REQUEST_CODE_RESOLVE_CONNECTION = 1;

    private static final int REQUEST_CODE_ENABLE_BLE = 2;

    private static final int REQUEST_CODE_SUBSCRIBE_RESULT = 3;

    private static final int REQUEST_CODE_ACCESS_LOCATION = 4;

    private DrawerHeaderLayout drawerHeaderLayout = new DrawerHeaderLayout();

    private ViewComponent viewComponent;

    private ActionBarDrawerToggle toggle;

    private DrawerLayout drawerLayout;

    private RelativeLayout mainLayout;

    @Inject
    ActivityPresenter presenter;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Memo:
        // By the management of the memory by the Android OS, re-construction of Fragment is carried out automatically.
        // But once you create an instance of the Dagger after super#onCreate(),
        // it will be done to rebuild the Fragment in the processing of the super#onCreate.
        // because the NullPointerException occurs, create an instance of the Dagger before super#onCreate().

        // Dagger
        viewComponent = App.getApplicationComponent(this)
                .viewComponent(new ActivityModule(this));
        viewComponent.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = ButterKnife.findById(this, R.id.fragmentPlaceholder);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        ButterKnife.bind(drawerHeaderLayout, headerView);

        presenter.onCreateView(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (REQUEST_CODE_RESOLVE_CONNECTION == requestCode && resultCode == RESULT_OK) {
            if (presenter.isAccessLocationGranted()) {
                presenter.onConnect();
            } else {
                LOGGER.warn("Can not connect to nearby. Not granted for location permission.");
            }
        } else if (REQUEST_CODE_SUBSCRIBE_RESULT == requestCode && resultCode == RESULT_OK) {
            presenter.onSubscribeInBackground();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (REQUEST_CODE_ACCESS_LOCATION == requestCode && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            LOGGER.info("Location permission is granted.");
            presenter.onAccessLocationGranted();
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
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_favorites: {
                FragmentController controller = new FragmentController(getSupportFragmentManager());
                controller.showFavoriteListFragment();
                break;
            }
            case R.id.nav_recently: {
                FragmentController controller = new FragmentController(getSupportFragmentManager());
                controller.showRecentlyListFragment();
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
                presenter.onSignOut(this);
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
        imageLoader.displayImage(imageUri, drawerHeaderLayout.userImageView);

        drawerHeaderLayout.textViewUserName.setText(displayName);
        drawerHeaderLayout.textViewEmail.setText(email);
    }

    @Override
    public void showSnackBar(int resId) {
        Snackbar.make(mainLayout, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showAdvertiseDisableConfirmDialog() {
        ConfirmDialog dialog = new ConfirmDialog(MainActivity.this, R.string.message_advertise_disable);
        dialog.show();
    }

    @Override
    public void showBleEnabledActivity() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BLE);
    }

    @Override
    public void startAdvertiseService(PreferenceModel model) {
        Intent intent = new Intent(getApplicationContext(), AdvertiseService.class);
        intent.putExtra(IntentKey.NAMESPACE_ID.name(), model.namespaceId);
        intent.putExtra(IntentKey.INSTANCE_ID.name(), model.instanceId);
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
    public void showFavoriteListFragment() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        FragmentController fragmentController = new FragmentController(getSupportFragmentManager());
        fragmentController.showFavoriteListFragment();
    }

    public static ViewComponent getUserComponent(@NonNull Fragment fragment) {
        return ((MainActivity) fragment.getActivity()).viewComponent;
    }

    public void setDrawerIndicatorEnabled(boolean enabled) {
        toggle.setDrawerIndicatorEnabled(enabled);
    }

    public void onSubscribeInBackground() {
        presenter.onSubscribeInBackground();
    }

    public void onUnSubscribeInBackground() {
        presenter.onUnSubscribeInBackground();
    }

    public void onSignedIn() {
        presenter.onSignedIn();
    }
}
