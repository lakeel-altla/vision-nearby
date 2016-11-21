package com.lakeel.profile.notification.presentation.view.fragment.settings;

import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.presentation.presenter.settings.SettingsPresenter;
import com.lakeel.profile.notification.presentation.view.SettingsView;
import com.lakeel.profile.notification.presentation.view.activity.MainActivity;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.view.View;

import javax.inject.Inject;

public final class SettingsFragment extends PreferenceFragmentCompat implements SettingsView {

    private static final String KEY_SNS_CATEGORY = "snsCategory";

    private static final String KEY_BLUETOOTH_SCREEN = "bluetoothScreen";

    private static final String KEY_LINE_SCREEN = "lineScreen";

    private static final String KEY_CM_SCREEN = "cmScreen";

    private static final String KEY_TRACKING_SCREEN = "trackingScreen";

    private PreferenceCategory mSnsCategory;

    private PreferenceScreen mCmScreen;

    @Inject
    SettingsPresenter mPresenter;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_fragment);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        mPresenter.onCreateView(this);

        // BLE
        PreferenceScreen bleScreen = (PreferenceScreen) findPreference(KEY_BLUETOOTH_SCREEN);
        bleScreen.setOnPreferenceClickListener(preference -> {
            ((MainActivity) getActivity()).showBleSettingsFragment();
            return false;
        });

        // LINE
        PreferenceScreen lineScreen = (PreferenceScreen) findPreference(KEY_LINE_SCREEN);
        lineScreen.setOnPreferenceClickListener(preference -> {
            ((MainActivity) getActivity()).showLineSettingsFragment();
            return false;
        });

        // COMPANY Messenger
        mCmScreen = (PreferenceScreen) findPreference(KEY_CM_SCREEN);
        mCmScreen.setOnPreferenceClickListener(preference -> {
            ((MainActivity) getActivity()).showCmSettingsFragment();
            return false;
        });

        // Once, hide the menu of COMPANY Messenger.
        mSnsCategory = (PreferenceCategory) findPreference(KEY_SNS_CATEGORY);
        mSnsCategory.removePreference(mCmScreen);

        // Tracking
        PreferenceScreen trackingScreen = (PreferenceScreen) findPreference(KEY_TRACKING_SCREEN);
        trackingScreen.setOnPreferenceClickListener(preference -> {
            ((MainActivity) getActivity()).showDeviceListFragment();
            return false;
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.title_settings);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(true);

        MainActivity activity = (MainActivity) getActivity();
        activity.setDrawerIndicatorEnabled(true);

        mPresenter.onActivityCreated();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public void showCMPreferences() {
        mSnsCategory.addPreference(mCmScreen);
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        View view = getView();
        if (view != null) {
            Snackbar.make(getView(), resId, Snackbar.LENGTH_SHORT).show();
        }
    }
}
