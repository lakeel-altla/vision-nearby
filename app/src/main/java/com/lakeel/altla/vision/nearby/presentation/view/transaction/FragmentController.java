package com.lakeel.altla.vision.nearby.presentation.view.transaction;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.view.bundle.HistoryBundle;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.estimation.DistanceEstimationFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.favorite.FavoriteFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.favorite.FavoriteListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.history.HistoryFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.history.HistoryListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.nearby.NearbyListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.SettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.bluetooth.BleSettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.cm.CmSettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.device.DeviceListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.line.LineSettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.signin.SignInFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.tracking.TrackingFragment;

import java.util.ArrayList;

public final class FragmentController {

    private final String SIGN_IN_FRAGMENT_TAG = SignInFragment.class.getSimpleName();

    private final String USER_LIST_FRAGMENT_TAG = FavoriteListFragment.class.getSimpleName();

    private final String HISTORY_LIST_FRAGMENT_TAG = HistoryListFragment.class.getSimpleName();

    private final String HISTORY_FRAGMENT_TAG = HistoryFragment.class.getSimpleName();

    private final String TRACKING_FRAGMENT_TAG = TrackingFragment.class.getSimpleName();

    private final String DEVICE_LIST_FRAGMENT_TAG = DeviceListFragment.class.getSimpleName();

    private final String DISTANCE_ESTIMATION_FRAGMENT_TAG = DistanceEstimationFragment.class.getSimpleName();

    private final String PROFILE_FRAGMENT_TAG = FavoriteFragment.class.getSimpleName();

    private final String SETTINGS_FRAGMENT_TAG = SettingsFragment.class.getSimpleName();

    private final String SETTINGS_BLE_FRAGMENT_TAG = BleSettingsFragment.class.getSimpleName();

    private final String SETTINGS_LINE_FRAGMENT_TAG = LineSettingsFragment.class.getSimpleName();

    private final String SETTINGS_CM_FRAGMENT_TAG = CmSettingsFragment.class.getSimpleName();

    private FragmentManager fragmentManager;

    public FragmentController(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void showSignInFragment() {
        SignInFragment fragment = SignInFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SIGN_IN_FRAGMENT_TAG);
    }

    public void showFavoriteListFragment() {
        FavoriteListFragment fragment = FavoriteListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, USER_LIST_FRAGMENT_TAG);
    }

    public void showNearbyListFragment() {
        NearbyListFragment fragment = NearbyListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, USER_LIST_FRAGMENT_TAG);
    }

    public void showRecentlyListFragment() {
        HistoryListFragment fragment = HistoryListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, HISTORY_LIST_FRAGMENT_TAG);
    }

    public void showHistoryFragment(HistoryBundle data) {
        HistoryFragment fragment = HistoryFragment.newInstance(data);
        replaceFragment(R.id.fragmentPlaceholder, fragment, HISTORY_FRAGMENT_TAG);
    }

    public void showTrackingFragment(String id, String beaconName) {
        TrackingFragment fragment = TrackingFragment.newInstance(id, beaconName);
        replaceFragment(R.id.fragmentPlaceholder, fragment, TRACKING_FRAGMENT_TAG);
    }

    public void showDeviceListFragment() {
        DeviceListFragment fragment = DeviceListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, DEVICE_LIST_FRAGMENT_TAG);
    }

    public void showDistanceEstimationFragment(ArrayList<String> beaconIds, String targetName) {
        DistanceEstimationFragment fragment = DistanceEstimationFragment.newInstance(beaconIds, targetName);
        replaceFragment(R.id.fragmentPlaceholder, fragment, DISTANCE_ESTIMATION_FRAGMENT_TAG);
    }

    public void showProfileFragment(String userId, String userName) {
        FavoriteFragment fragment = FavoriteFragment.newInstance(userId, userName);
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
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }
}
