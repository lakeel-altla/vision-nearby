package com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.MenuItem;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.BeaconIdModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.settings.bluetooth.BleSettingsPresenter;
import com.lakeel.altla.vision.nearby.presentation.service.AdvertiseService;
import com.lakeel.altla.vision.nearby.presentation.view.BleSettingsView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;

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
                ((MainActivity) getActivity()).onSubscribeInBackground();
            } else {
                ((MainActivity) getActivity()).onUnSubscribeInBackground();
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
    public void startAdvertise(BeaconIdModel model) {
        Intent intent = new Intent(getContext(), AdvertiseService.class);
        intent.putExtra(IntentKey.NAMESPACE_ID.name(), model.mNamespaceId);
        intent.putExtra(IntentKey.INSTANCE_ID.name(), model.mInstanceId);
        getContext().startService(intent);
    }

    @Override
    public void disableAdvertiseSettings() {
        advertisePreference.setEnabled(false);
    }
}
