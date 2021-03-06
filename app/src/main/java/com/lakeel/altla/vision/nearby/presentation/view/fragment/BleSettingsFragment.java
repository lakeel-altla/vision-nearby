package com.lakeel.altla.vision.nearby.presentation.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.MenuItem;
import android.view.View;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.application.App;
import com.lakeel.altla.vision.nearby.presentation.presenter.BleSettingsPresenter;
import com.lakeel.altla.vision.nearby.presentation.service.AdvertiseService;
import com.lakeel.altla.vision.nearby.presentation.view.BleSettingsView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.intent.IntentKey;

import javax.inject.Inject;

public final class BleSettingsFragment extends PreferenceFragmentCompat implements BleSettingsView {

    @Inject
    BleSettingsPresenter presenter;

    private static final String KEY_ADVERTISE_IN_BACKGROUND = "advertiseInBackground";

    private static final String KEY_SUBSCRIBE_IN_BACKGROUND = "subscribeInBackground";

    private SwitchPreferenceCompat advertisePreference;

    public static BleSettingsFragment newInstance() {
        return new BleSettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        addPreferencesFromResource(R.xml.settings_ble);

        setHasOptionsMenu(true);

        presenter.onCreateView(this);

        MainActivity activity = ((MainActivity) getActivity());
        activity.setTitle(R.string.toolbar_title_ble_settings);
        activity.setDrawerIndicatorEnabled(false);

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
    public void disableAdvertiseSettings() {
        advertisePreference.setEnabled(false);
    }

    @Override
    public void startAdvertiseInBackground(@NonNull String beaconId) {
        Intent intent = new Intent(getContext(), AdvertiseService.class);
        intent.putExtra(IntentKey.BEACON_ID.name(), beaconId);
        getContext().startService(intent);
    }

    @Override
    public void startSubscribeInBackground() {
        App.startDetectBeaconsInBackground(this);
    }

    @Override
    public void stopSubscribeInBackground() {
        App.stopDetectBeaconsInBackground(this);
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        View view = getView();
        if (view != null) {
            Snackbar.make(view, resId, Snackbar.LENGTH_SHORT).show();
        }
    }
}
