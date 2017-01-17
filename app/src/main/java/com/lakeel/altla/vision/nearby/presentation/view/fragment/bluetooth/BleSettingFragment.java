package com.lakeel.altla.vision.nearby.presentation.view.fragment.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.MenuItem;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;
import com.lakeel.altla.vision.nearby.presentation.presenter.ble.BleSettingPresenter;
import com.lakeel.altla.vision.nearby.presentation.service.AdvertiseService;
import com.lakeel.altla.vision.nearby.presentation.view.BleSettingView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;

import javax.inject.Inject;

public final class BleSettingFragment extends PreferenceFragmentCompat implements BleSettingView {

    @Inject
    BleSettingPresenter presenter;

    private static final String KEY_ADVERTISE_IN_BACKGROUND = "advertiseInBackground";

    private static final String KEY_SUBSCRIBE_IN_BACKGROUND = "startMonitorBeacons";

    private SwitchPreferenceCompat advertisePreference;

    public static BleSettingFragment newInstance() {
        return new BleSettingFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_ble);

        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.title_ble_settings);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        advertisePreference = (SwitchPreferenceCompat) findPreference(KEY_ADVERTISE_IN_BACKGROUND);
        advertisePreference.setOnPreferenceChangeListener((preference, newValue) -> {
            Boolean booleanValue = (Boolean) newValue;
            if (booleanValue) {
                presenter.onStartAdvertise();
            } else {
                presenter.onStopAdvertise();
            }
            return true;
        });

        SwitchPreferenceCompat subscribePreference = (SwitchPreferenceCompat) findPreference(KEY_SUBSCRIBE_IN_BACKGROUND);
        subscribePreference.setOnPreferenceChangeListener((preference, newValue) -> {
            Boolean booleanValue = (Boolean) newValue;
            if (booleanValue) {
                presenter.onStartSubscribe();
            } else {
                presenter.onStopSubscribe();
            }
            return true;
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.onActivityCreated();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            default:
                break;
        }
        return true;
    }

    @Override
    public void startAdvertise(String beaconId) {
        Intent intent = new Intent(getContext(), AdvertiseService.class);
        intent.putExtra(IntentKey.BEACON_ID.name(), beaconId);
        getContext().startService(intent);
    }

    @Override
    public void disableAdvertiseSettings() {
        advertisePreference.setEnabled(false);
    }

    @Override
    public void startSubscribe() {
        ((MainActivity) getActivity()).startMonitorBeacons();
    }

    @Override
    public void stopSubscribe() {
        ((MainActivity) getActivity()).stopMonitorBeacons();
    }
}
