package com.lakeel.altla.vision.nearby.presentation.view.fragment.settings.bluetooth;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;
import com.lakeel.altla.vision.nearby.presentation.presenter.settings.bluetooth.BleSettingsPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.BeaconIdModel;
import com.lakeel.altla.vision.nearby.presentation.service.PublishService;
import com.lakeel.altla.vision.nearby.presentation.view.BleSettingsView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.MenuItem;

import javax.inject.Inject;

public final class BleSettingsFragment extends PreferenceFragmentCompat implements BleSettingsView {

    public static BleSettingsFragment newInstance() {
        return new BleSettingsFragment();
    }

    @Inject
    BleSettingsPresenter mPresenter;

    private static final String KEY_PUBLISH_IN_BACKGROUND = "publishInBackground";

    private static final String KEY_SUBSCRIBE_IN_BACKGROUND = "subscribeInBackground";

    private SwitchPreferenceCompat mPublishPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_ble);

        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.title_ble_settings);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        mPresenter.onCreateView(this);

        mPublishPreference = (SwitchPreferenceCompat) findPreference(KEY_PUBLISH_IN_BACKGROUND);
        mPublishPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            Boolean booleanValue = (Boolean) newValue;
            if (booleanValue) {
                mPresenter.onStartPublish();
            } else {
                mPresenter.onStopPublishing();
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
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
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
    public void startPublish(BeaconIdModel model) {
        Intent intent = new Intent(getContext(), PublishService.class);
        intent.putExtra(IntentKey.NAMESPACE_ID.name(), model.mNamespaceId);
        intent.putExtra(IntentKey.INSTANCE_ID.name(), model.mInstanceId);
        getContext().startService(intent);
    }

    @Override
    public void disablePublishSettings() {
        mPublishPreference.setEnabled(false);
    }
}
