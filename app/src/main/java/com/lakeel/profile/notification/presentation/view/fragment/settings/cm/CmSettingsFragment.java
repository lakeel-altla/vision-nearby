package com.lakeel.profile.notification.presentation.view.fragment.settings.cm;

import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.presentation.presenter.cm.CmSettingsPresenter;
import com.lakeel.profile.notification.presentation.presenter.model.CMLinksModel;
import com.lakeel.profile.notification.presentation.view.CmSettingsView;
import com.lakeel.profile.notification.presentation.view.activity.MainActivity;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

public final class CmSettingsFragment extends PreferenceFragmentCompat implements CmSettingsView {

    public static CmSettingsFragment newInstance() {
        return new CmSettingsFragment();
    }

    @Inject
    CmSettingsPresenter mPresenter;

    private static final String KEY_CM_API_KEY = "cmApiKey";

    private static final String KEY_CM_SECRET_KEY = "cmSecretKey";

    private static final String KEY_CM_JID = "cmJidKey";

    private EditTextPreference mCMApiPreference;

    private EditTextPreference mCMSecretPreference;

    private EditTextPreference mCMJidPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_cm);

        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.title_cm_settings);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        mPresenter.onCreateView(this);

        mCMApiPreference = (EditTextPreference) findPreference(KEY_CM_API_KEY);
        mCMApiPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String apiKey = (String) newValue;
            mPresenter.onSaveCMApiKey(apiKey);
            return false;
        });

        mCMSecretPreference = (EditTextPreference) findPreference(KEY_CM_SECRET_KEY);
        mCMSecretPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String secretKey = (String) newValue;
            mPresenter.onSaveCMSecretKey(secretKey);
            return false;
        });

        mCMJidPreference = (EditTextPreference) findPreference(KEY_CM_JID);
        mCMJidPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String jid = (String) newValue;
            mPresenter.onSaveCMJid(jid);
            return false;
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.onActivityCreated();
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
    public void showCMPreferences(CMLinksModel model) {
        mCMApiPreference.setSummary(model.mApiKey);
        mCMSecretPreference.setSummary(model.mSecretKey);
        mCMJidPreference.setSummary(model.mJid);
    }

    @Override
    public void updateCMApiKeyPreference(String apiKey) {
        mCMApiPreference.setSummary(apiKey);
    }

    @Override
    public void updateCMSecretKeyPreference(String secretKey) {
        mCMSecretPreference.setSummary(secretKey);
    }

    @Override
    public void updateCMJidPreference(String jid) {
        mCMJidPreference.setSummary(jid);
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        View view = getView();
        if (view != null) {
            Snackbar.make(getView(), resId, Snackbar.LENGTH_SHORT).show();
        }
    }
}
