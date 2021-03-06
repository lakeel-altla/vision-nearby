package com.lakeel.altla.vision.nearby.presentation.view.fragment;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.EstimationTarget;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.FavoriteUser;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.PassingUser;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.TrackingBeacon;

public final class FragmentController {

    private final String SIGN_IN_FRAGMENT_TAG = SignInFragment.class.getSimpleName();

    private final String FAVORITE_LIST_FRAGMENT_TAG = FavoriteListFragment.class.getSimpleName();

    private final String FAVORITE_USER_FRAGMENT_TAG = FavoriteUserFragment.class.getSimpleName();

    private final String NEARBY_HISTORY_LIST_FRAGMENT_TAG = NearbyHistoryListFragment.class.getSimpleName();

    private final String PASSING_USER_FRAGMENT_TAG = PassingUserFragment.class.getSimpleName();

    private final String NEARBY_USER_LIST_FRAGMENT = NearbyUserListFragment.class.getSimpleName();

    private final String DEVICE_LIST_FRAGMENT_TAG = DeviceListFragment.class.getSimpleName();

    private final String TRACKING_FRAGMENT_TAG = TrackingFragment.class.getSimpleName();

    private final String DISTANCE_ESTIMATION_FRAGMENT_TAG = DistanceEstimationFragment.class.getSimpleName();

    private final String INFORMATION_LIST_FRAGMENT_TAG = InformationListFragment.class.getSimpleName();

    private final String INFORMATION_FRAGMENT_TAG = InformationFragment.class.getSimpleName();

    private final String SETTINGS_FRAGMENT_TAG = SettingsFragment.class.getSimpleName();

    private final String SETTINGS_BLE_FRAGMENT_TAG = BleSettingsFragment.class.getSimpleName();

    private final String SETTINGS_LINE_FRAGMENT_TAG = LineSettingsFragment.class.getSimpleName();

    private FragmentManager fragmentManager;

    public FragmentController(@NonNull FragmentActivity activity) {
        this.fragmentManager = activity.getSupportFragmentManager();
    }

    FragmentController(@NonNull Fragment fragment) {
        this.fragmentManager = fragment.getActivity().getSupportFragmentManager();
    }

    public void showSignInFragment() {
        SignInFragment fragment = SignInFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SIGN_IN_FRAGMENT_TAG);
    }

    public void showFavoriteListFragment() {
        FavoriteListFragment fragment = FavoriteListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, FAVORITE_LIST_FRAGMENT_TAG);
    }

    public void showNearbyUserListFragment() {
        NearbyUserListFragment fragment = NearbyUserListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, NEARBY_USER_LIST_FRAGMENT);
    }

    public void showNearbyHistoryListFragment() {
        NearbyHistoryListFragment fragment = NearbyHistoryListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, NEARBY_HISTORY_LIST_FRAGMENT_TAG);
    }

    public void showInformationListFragment() {
        InformationListFragment fragment = InformationListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, INFORMATION_LIST_FRAGMENT_TAG);
    }

    public void showSettingsFragment() {
        SettingsFragment fragment = SettingsFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SETTINGS_FRAGMENT_TAG);
    }

    void showPassingUserFragment(PassingUser passingUser) {
        PassingUserFragment fragment = PassingUserFragment.newInstance(passingUser);
        replaceFragment(R.id.fragmentPlaceholder, fragment, PASSING_USER_FRAGMENT_TAG);
    }

    void showTrackingFragment(TrackingBeacon trackingBeacon) {
        TrackingFragment fragment = TrackingFragment.newInstance(trackingBeacon);
        replaceFragment(R.id.fragmentPlaceholder, fragment, TRACKING_FRAGMENT_TAG);
    }

    void showDeviceListFragment() {
        DeviceListFragment fragment = DeviceListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, DEVICE_LIST_FRAGMENT_TAG);
    }

    void showDistanceEstimationFragment(EstimationTarget target) {
        DistanceEstimationFragment fragment = DistanceEstimationFragment.newInstance(target);
        replaceFragment(R.id.fragmentPlaceholder, fragment, DISTANCE_ESTIMATION_FRAGMENT_TAG);
    }

    void showFavoriteUserFragment(FavoriteUser favoriteUser) {
        FavoriteUserFragment fragment = FavoriteUserFragment.newInstance(favoriteUser);
        replaceFragment(R.id.fragmentPlaceholder, fragment, FAVORITE_USER_FRAGMENT_TAG);
    }

    void showBleSettingsFragment() {
        BleSettingsFragment fragment = BleSettingsFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SETTINGS_BLE_FRAGMENT_TAG);
    }

    void showLineSettingsFragment() {
        LineSettingsFragment fragment = LineSettingsFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SETTINGS_LINE_FRAGMENT_TAG);
    }

    void showInformationFragment(String informationId) {
        InformationFragment fragment = InformationFragment.newInstance(informationId);
        replaceFragment(R.id.fragmentPlaceholder, fragment, INFORMATION_FRAGMENT_TAG);
    }

    private void replaceFragment(@IdRes int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }
}
