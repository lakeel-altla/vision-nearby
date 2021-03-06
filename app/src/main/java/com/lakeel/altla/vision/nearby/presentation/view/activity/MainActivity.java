package com.lakeel.altla.vision.nearby.presentation.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.application.App;
import com.lakeel.altla.vision.nearby.presentation.di.component.ViewComponent;
import com.lakeel.altla.vision.nearby.presentation.presenter.ActivityPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.ActivityModel;
import com.lakeel.altla.vision.nearby.presentation.service.AdvertiseService;
import com.lakeel.altla.vision.nearby.presentation.view.ActivityView;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.FragmentController;
import com.lakeel.altla.vision.nearby.presentation.view.intent.IntentKey;
import com.lakeel.altla.vision.nearby.presentation.view.layout.DrawerHeaderLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActivityView {

    @Inject
    ActivityPresenter presenter;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);

    private static final int REQUEST_CODE_ENABLE_BLE = 1;

    private static final int REQUEST_CODE_ACCESS_FINE_LOCATION = 2;

    private DrawerHeaderLayout drawerHeaderLayout = new DrawerHeaderLayout();

    private ViewComponent viewComponent;

    private ActionBarDrawerToggle toggle;

    private DrawerLayout drawerLayout;

    private RelativeLayout mainLayout;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Memo:
        // By the management of the memory by the Android OS, re-construction of Fragment is carried out automatically.
        // But once you create an instance of the Dagger after super#onCreate(),
        // it will be done token rebuild the Fragment in the processing of the super#onCreate.
        // because the NullPointerException occurs, create an instance of the Dagger before super#onCreate().

        // Dagger
        viewComponent = App.getApplicationComponent(this).viewComponent();
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.inflateHeaderView(R.layout.navigation_header);
        ButterKnife.bind(drawerHeaderLayout, headerView);

        presenter.onCreateView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_ENABLE_BLE == requestCode) {
            if (RESULT_OK == resultCode) {
                presenter.onBleEnabled();
            } else {
                LOGGER.error("Failed token enable BLE.");
                showSnackBar(R.string.snackBar_error_not_enable_ble);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.onAccessFineLocationGranted();
            } else {
                presenter.onAccessFineLocationDenied();
                LOGGER.warn("Access fine location permission is denied.");
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
            case R.id.favorites: {
                FragmentController controller = new FragmentController(this);
                controller.showFavoriteListFragment();
                break;
            }
            case R.id.history: {
                FragmentController controller = new FragmentController(this);
                controller.showNearbyHistoryListFragment();
                break;
            }
            case R.id.nearby: {
                FragmentController controller = new FragmentController(this);
                controller.showNearbyUserListFragment();
                break;
            }
            case R.id.information: {
                FragmentController controller = new FragmentController(this);
                controller.showInformationListFragment();
                break;
            }
            case R.id.settings: {
                FragmentController controller = new FragmentController(this);
                controller.showSettingsFragment();
                break;
            }
            case R.id.signOut:
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
    public void showSignInFragment() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        FragmentController fragmentController = new FragmentController(this);
        fragmentController.showSignInFragment();
    }

    @Override
    public void updateDrawerProfile(@NonNull ActivityModel model) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(model.imageUri, drawerHeaderLayout.userImageView);

        drawerHeaderLayout.userNameTextView.setText(model.userName);
        drawerHeaderLayout.emailTextView.setText(model.email);
    }

    @Override
    public void showAdvertiseDisableConfirmDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(MainActivity.this);
        builder.title(R.string.dialog_title_confirm);
        builder.positiveText(R.string.dialog_button_positive);
        builder.content(R.string.snackBar_message_advertise_disable);
        builder.show();
    }

    @Override
    public void showBleEnabledActivity() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, REQUEST_CODE_ENABLE_BLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void requestAccessFineLocationPermission() {
        MainActivity.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ACCESS_FINE_LOCATION);
    }

    @Override
    public void startDetectBeaconsInBackground() {
        App.startDetectBeaconsInBackground(this);
    }

    @Override
    public void stopDetectBeaconsInBackground() {
        App.stopDetectBeaconsInBackground(this);
    }

    @Override
    public void startAdvertiseInBackground(@NonNull String beaconId) {
        Intent intent = new Intent(getApplicationContext(), AdvertiseService.class);
        intent.putExtra(IntentKey.BEACON_ID.name(), beaconId);
        getApplicationContext().startService(intent);
    }

    @Override
    public void finishActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }

    @Override
    public void showFavoriteListFragment() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FragmentController fragmentController = new FragmentController(this);
        fragmentController.showFavoriteListFragment();
    }

    @Override
    public void showSnackBar(int resId) {
        Snackbar.make(mainLayout, resId, Snackbar.LENGTH_SHORT).show();
    }

    public static ViewComponent getUserComponent(@NonNull Fragment fragment) {
        return ((MainActivity) fragment.getActivity()).viewComponent;
    }

    public void setDrawerIndicatorEnabled(boolean enabled) {
        toggle.setDrawerIndicatorEnabled(enabled);
    }

    public void onSignedIn() {
        presenter.onSignedIn();
    }
}
