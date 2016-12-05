package com.lakeel.altla.vision.nearby.presentation.view.fragment.settings.cm;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.MenuItem;
import android.view.View;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.CMLinksModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.settings.cm.CmSettingsPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.CmSettingsView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;

import javax.inject.Inject;

public final class CmSettingsFragment extends PreferenceFragmentCompat implements CmSettingsView {

    @Inject
    CmSettingsPresenter presenter;

    private static final String KEY_CM_API_KEY = "cmApiKey";

    private static final String KEY_CM_SECRET_KEY = "cmSecretKey";

    private static final String KEY_CM_JID = "cmJidKey";

    private EditTextPreference CMApiPreference;

    private EditTextPreference CMSecretPreference;

    private EditTextPreference CMJidPreference;

    public static CmSettingsFragment newInstance() {
        return new CmSettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_cm);

        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.title_cm_settings);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        CMApiPreference = (EditTextPreference) findPreference(KEY_CM_API_KEY);
        CMApiPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String apiKey = (String) newValue;
            presenter.onSaveCMApiKey(apiKey);
            return false;
        });

        CMSecretPreference = (EditTextPreference) findPreference(KEY_CM_SECRET_KEY);
        CMSecretPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String secretKey = (String) newValue;
            presenter.onSaveCMSecretKey(secretKey);
            return false;
        });

        CMJidPreference = (EditTextPreference) findPreference(KEY_CM_JID);
        CMJidPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String jid = (String) newValue;
            presenter.onSaveCMJid(jid);
            return false;
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
    public void showCMPreferences(CMLinksModel model) {
        CMApiPreference.setSummary(model.mApiKey);
        CMSecretPreference.setSummary(model.mSecretKey);
        CMJidPreference.setSummary(model.mJid);
    }

    @Override
    public void updateCMApiKeyPreference(String apiKey) {
        CMApiPreference.setSummary(apiKey);
    }

    @Override
    public void updateCMSecretKeyPreference(String secretKey) {
        CMSecretPreference.setSummary(secretKey);
    }

    @Override
    public void updateCMJidPreference(String jid) {
        CMJidPreference.setSummary(jid);
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        View view = getView();
        if (view != null) {
            Snackbar.make(getView(), resId, Snackbar.LENGTH_SHORT).show();
        }
    }
}
