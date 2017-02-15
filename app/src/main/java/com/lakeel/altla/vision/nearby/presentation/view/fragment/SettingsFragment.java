package com.lakeel.altla.vision.nearby.presentation.view.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;

public final class SettingsFragment extends PreferenceFragmentCompat {

    private static final String KEY_BLUETOOTH_SCREEN = "bleScreen";

    private static final String KEY_LINE_SCREEN = "lineScreen";

    private static final String KEY_TRACKING_SCREEN = "trackingScreen";

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_fragment);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        // BLE
        PreferenceScreen bleScreen = (PreferenceScreen) findPreference(KEY_BLUETOOTH_SCREEN);
        bleScreen.setOnPreferenceClickListener(preference -> {
            FragmentController controller = new FragmentController(this);
            controller.showBleSettingsFragment();
            return false;
        });

        // LINE
        PreferenceScreen lineScreen = (PreferenceScreen) findPreference(KEY_LINE_SCREEN);
        lineScreen.setOnPreferenceClickListener(preference -> {
            FragmentController controller = new FragmentController(this);
            controller.showLineSettingsFragment();
            return false;
        });

        // Tracking
        PreferenceScreen trackingScreen = (PreferenceScreen) findPreference(KEY_TRACKING_SCREEN);
        trackingScreen.setOnPreferenceClickListener(preference -> {
            FragmentController controller = new FragmentController(this);
            controller.showDeviceListFragment();
            return false;
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.toolbar_title_settings);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(true);
    }
}
