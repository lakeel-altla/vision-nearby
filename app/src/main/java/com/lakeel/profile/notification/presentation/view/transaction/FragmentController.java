package com.lakeel.profile.notification.presentation.view.transaction;

import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.presentation.view.fragment.device.DeviceListFragment;
import com.lakeel.profile.notification.presentation.view.fragment.favorites.FavoritesListFragment;
import com.lakeel.profile.notification.presentation.view.fragment.nearby.NearbyListFragment;
import com.lakeel.profile.notification.presentation.view.fragment.recently.RecentlyFragment;
import com.lakeel.profile.notification.presentation.view.fragment.settings.SettingsFragment;
import com.lakeel.profile.notification.presentation.view.fragment.signin.SignInFragment;
import com.lakeel.profile.notification.presentation.view.fragment.tracking.TrackingFragment;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public final class FragmentController {

    private final String SIGN_IN_FRAGMENT_TAG = SignInFragment.class.getSimpleName();

    private final String USER_LIST_FRAGMENT_TAG = FavoritesListFragment.class.getSimpleName();

    private final String RECENTLY_FRAGMENT_TAG = RecentlyFragment.class.getSimpleName();

    private final String TRACKING_FRAGMENT_TAG = TrackingFragment.class.getSimpleName();

    private final String DEVICE_LIST_FRAGMENT_TAG = DeviceListFragment.class.getSimpleName();

    private final String SETTINGS_FRAGMENT_TAG = SettingsFragment.class.getSimpleName();

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

    public void showTrackingFragment(String id) {
        TrackingFragment fragment = TrackingFragment.newInstance(id);
        replaceFragment(R.id.fragmentPlaceholder, fragment, TRACKING_FRAGMENT_TAG);
    }

    public void showDeviceListFragment() {
        DeviceListFragment fragment = DeviceListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, DEVICE_LIST_FRAGMENT_TAG);
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
