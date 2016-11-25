package com.lakeel.altla.vision.nearby.presentation.view.transaction;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.estimation.DeviceDistanceEstimationFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.favorites.FavoritesListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.nearby.NearbyListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.profile.ProfileFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.recently.RecentlyFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.settings.SettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.settings.bluetooth.BleSettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.settings.cm.CmSettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.settings.device.DeviceListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.settings.line.LineSettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.signin.SignInFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.tracking.TrackingFragment;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

public final class FragmentController {

    private final String SIGN_IN_FRAGMENT_TAG = SignInFragment.class.getSimpleName();

    private final String USER_LIST_FRAGMENT_TAG = FavoritesListFragment.class.getSimpleName();

    private final String RECENTLY_FRAGMENT_TAG = RecentlyFragment.class.getSimpleName();

    private final String TRACKING_FRAGMENT_TAG = TrackingFragment.class.getSimpleName();

    private final String DEVICE_LIST_FRAGMENT_TAG = DeviceListFragment.class.getSimpleName();

    private final String DISTANCE_ESTIMATION_FRAGMENT_TAG = DeviceDistanceEstimationFragment.class.getSimpleName();

    private final String PROFILE_FRAGMENT_TAG = ProfileFragment.class.getSimpleName();

    private final String SETTINGS_FRAGMENT_TAG = SettingsFragment.class.getSimpleName();

    private final String SETTINGS_BLE_FRAGMENT_TAG = BleSettingsFragment.class.getSimpleName();

    private final String SETTINGS_LINE_FRAGMENT_TAG = LineSettingsFragment.class.getSimpleName();

    private final String SETTINGS_CM_FRAGMENT_TAG = CmSettingsFragment.class.getSimpleName();

    private FragmentManager mFragmentManager;

    public FragmentController(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void showSignInFragment() {
        SignInFragment fragment = SignInFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SIGN_IN_FRAGMENT_TAG);
    }

    public void showFavoritesListFragment() {
        FavoritesListFragment fragment = FavoritesListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, USER_LIST_FRAGMENT_TAG);
    }

    public void showNearbyListFragment() {
        NearbyListFragment fragment = NearbyListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, USER_LIST_FRAGMENT_TAG);
    }

    public void showRecentlyFragment() {
        RecentlyFragment fragment = RecentlyFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, RECENTLY_FRAGMENT_TAG);
    }

    public void showTrackingFragment(String id, String beaconName) {
        TrackingFragment fragment = TrackingFragment.newInstance(id, beaconName);
        replaceFragment(R.id.fragmentPlaceholder, fragment, TRACKING_FRAGMENT_TAG);
    }

    public void showDeviceListFragment() {
        DeviceListFragment fragment = DeviceListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, DEVICE_LIST_FRAGMENT_TAG);
    }

    public void showDeviceDistanceEstimationFragment(ArrayList<String> beaconIds, String targetName) {
        DeviceDistanceEstimationFragment fragment = DeviceDistanceEstimationFragment.newInstance(beaconIds, targetName);
        replaceFragment(R.id.fragmentPlaceholder, fragment, DISTANCE_ESTIMATION_FRAGMENT_TAG);
    }

    public void showProfileFragment(String userId, String userName) {
        ProfileFragment fragment = ProfileFragment.newInstance(userId, userName);
        replaceFragment(R.id.fragmentPlaceholder, fragment, PROFILE_FRAGMENT_TAG);
    }

    public void showBleSettingsFragment() {
        BleSettingsFragment fragment = BleSettingsFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SETTINGS_BLE_FRAGMENT_TAG);
    }

    public void showLineSettingsFragment() {
        LineSettingsFragment fragment = LineSettingsFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SETTINGS_LINE_FRAGMENT_TAG);
    }

    public void showCmSettingsFragment() {
        CmSettingsFragment fragment = CmSettingsFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SETTINGS_CM_FRAGMENT_TAG);
    }

    public void showSettingsFragment() {
        SettingsFragment fragment = SettingsFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SETTINGS_FRAGMENT_TAG);
    }

    public void replaceFragment(@IdRes int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }
}
