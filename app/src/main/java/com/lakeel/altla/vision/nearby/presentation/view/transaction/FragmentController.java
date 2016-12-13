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
import com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.SettingFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.bluetooth.BleSettingFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.cm.CmSettingFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.device.DeviceListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.line.LineSettingFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.signin.SignInFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.tracking.TrackingFragment;

import java.util.ArrayList;

public final class FragmentController {

    private final String SIGN_IN_FRAGMENT_TAG = SignInFragment.class.getSimpleName();

    private final String FAVORITE_LIST_FRAGMENT_TAG = FavoriteListFragment.class.getSimpleName();

    private final String HISTORY_LIST_FRAGMENT_TAG = HistoryListFragment.class.getSimpleName();

    private final String HISTORY_FRAGMENT_TAG = HistoryFragment.class.getSimpleName();

    private final String NEARBY_LIST_FRAGMENT = NearbyListFragment.class.getSimpleName();

    private final String TRACKING_FRAGMENT_TAG = TrackingFragment.class.getSimpleName();

    private final String DEVICE_LIST_FRAGMENT_TAG = DeviceListFragment.class.getSimpleName();

    private final String DISTANCE_ESTIMATION_FRAGMENT_TAG = DistanceEstimationFragment.class.getSimpleName();

    private final String PROFILE_FRAGMENT_TAG = FavoriteFragment.class.getSimpleName();

    private final String SETTING_FRAGMENT_TAG = SettingFragment.class.getSimpleName();

    private final String SETTING_BLE_FRAGMENT_TAG = BleSettingFragment.class.getSimpleName();

    private final String SETTING_LINE_FRAGMENT_TAG = LineSettingFragment.class.getSimpleName();

    private final String SETTING_CM_FRAGMENT_TAG = CmSettingFragment.class.getSimpleName();

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
        replaceFragment(R.id.fragmentPlaceholder, fragment, FAVORITE_LIST_FRAGMENT_TAG);
    }

    public void showNearbyListFragment() {
        NearbyListFragment fragment = NearbyListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, NEARBY_LIST_FRAGMENT);
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
        BleSettingFragment fragment = BleSettingFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SETTING_BLE_FRAGMENT_TAG);
    }

    public void showLineSettingsFragment() {
        LineSettingFragment fragment = LineSettingFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SETTING_LINE_FRAGMENT_TAG);
    }

    public void showCmSettingsFragment() {
        CmSettingFragment fragment = CmSettingFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SETTING_CM_FRAGMENT_TAG);
    }

    public void showSettingsFragment() {
        SettingFragment fragment = SettingFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SETTING_FRAGMENT_TAG);
    }

    private void replaceFragment(@IdRes int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }
}
