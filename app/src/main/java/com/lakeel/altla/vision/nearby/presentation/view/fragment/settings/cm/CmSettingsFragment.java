package com.lakeel.altla.vision.nearby.presentation.view.fragment.settings.cm;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.MenuItem;
import android.view.View;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.CmLinksModel;
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

    private EditTextPreference cmApiPreference;

    private EditTextPreference cmSecretPreference;

    private EditTextPreference cmJidPreference;

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

        cmApiPreference = (EditTextPreference) findPreference(KEY_CM_API_KEY);
        cmApiPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String apiKey = (String) newValue;
            presenter.onSaveCmApiKey(apiKey);
            return false;
        });

        cmSecretPreference = (EditTextPreference) findPreference(KEY_CM_SECRET_KEY);
        cmSecretPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String secretKey = (String) newValue;
            presenter.onSaveCmSecretKey(secretKey);
            return false;
        });

        cmJidPreference = (EditTextPreference) findPreference(KEY_CM_JID);
        cmJidPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String jid = (String) newValue;
            presenter.onSaveCmJid(jid);
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
    public void showCmPreferences(CmLinksModel model) {
        cmApiPreference.setSummary(model.mApiKey);
        cmSecretPreference.setSummary(model.mSecretKey);
        cmJidPreference.setSummary(model.mJid);
    }

    @Override
    public void updateCmApiKeyPreference(String apiKey) {
        cmApiPreference.setSummary(apiKey);
    }

    @Override
    public void updateCmSecretKeyPreference(String secretKey) {
        cmSecretPreference.setSummary(secretKey);
    }

    @Override
    public void updateCmJidPreference(String jid) {
        cmJidPreference.setSummary(jid);
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        View view = getView();
        if (view != null) {
            Snackbar.make(getView(), resId, Snackbar.LENGTH_SHORT).show();
        }
    }
}
