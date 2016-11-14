package com.lakeel.profile.notification.presentation.view.fragment.settings;

import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.presentation.intent.IntentKey;
import com.lakeel.profile.notification.presentation.presenter.model.BeaconModel;
import com.lakeel.profile.notification.presentation.presenter.model.CMLinksModel;
import com.lakeel.profile.notification.presentation.presenter.settings.SettingsPresenter;
import com.lakeel.profile.notification.presentation.service.PublishService;
import com.lakeel.profile.notification.presentation.view.SettingsView;
import com.lakeel.profile.notification.presentation.view.activity.MainActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import static android.content.Context.ACTIVITY_SERVICE;

public final class SettingsFragment extends PreferenceFragmentCompat implements SettingsView {

    private static final String KEY_PREFERENCE_SCREEN = "preferenceScreen";

    private static final String KEY_PUBLISH_IN_BACKGROUND = "publishInBackground";

    private static final String KEY_SUBSCRIBE_IN_BACKGROUND = "subscribeInBackground";

    private static final String KEY_LINE_URL = "lineUrl";

    private static final String KEY_CM_CATEGORY = "cmCategory";

    private static final String KEY_CM_API_KEY = "cmApiKey";

    private static final String KEY_CM_SECRET_KEY = "cmSecretKey";

    private static final String KEY_CM_JID = "cmJid";

    private PreferenceScreen mPreferenceScreen;

    private SwitchPreferenceCompat mPublishPreference;

    private EditTextPreference mLINEUrlPreference;

    private PreferenceCategory mCMPrefernceCategory;

    private EditTextPreference mCMApiPreference;

    private EditTextPreference mCMSecretPreference;

    private EditTextPreference mCMJidPreference;

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

        mPublishPreference = (SwitchPreferenceCompat) findPreference(KEY_PUBLISH_IN_BACKGROUND);
        mPublishPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            Boolean booleanValue = (Boolean) newValue;
            if (booleanValue) {
                mPresenter.onPublish();
            } else {
                // Stop background service
                ActivityManager am = (ActivityManager) getContext().getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningServiceInfo> listServiceInfo = am.getRunningServices(Integer.MAX_VALUE);
                for (ActivityManager.RunningServiceInfo runningServiceInfo : listServiceInfo) {
                    if (runningServiceInfo.service.getClassName().equals(PublishService.class.getName())) {
                        Intent intent = new Intent(getContext(), PublishService.class);
                        getContext().stopService(intent);
                    }
                }
            }
            return true;
        });

        SwitchPreferenceCompat subscribePreference = (SwitchPreferenceCompat) findPreference(KEY_SUBSCRIBE_IN_BACKGROUND);
        subscribePreference.setOnPreferenceChangeListener((preference, newValue) -> {
            Boolean booleanValue = (Boolean) newValue;
            if (booleanValue) {
                ((MainActivity) getActivity()).onSubscribe();
            } else {
                ((MainActivity) getActivity()).onUnSubscribe();
            }
            return true;
        });

        mLINEUrlPreference = (EditTextPreference) findPreference(KEY_LINE_URL);
        mLINEUrlPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String lineUrl = (String) newValue;
            mPresenter.onSaveLineUrl(lineUrl);
            return false;
        });

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

        // Once, hide the menu of COMPANY Messenger.
        mCMPrefernceCategory = (PreferenceCategory) findPreference(KEY_CM_CATEGORY);
        mCMPrefernceCategory.removeAll();

        mPreferenceScreen = (PreferenceScreen) findPreference(KEY_PREFERENCE_SCREEN);
        mPreferenceScreen.removePreference(mCMPrefernceCategory);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
    public void showTitle(@StringRes int resId) {
        getActivity().setTitle(resId);
    }

    @Override
    public void disablePublishSettings() {
        mPublishPreference.setEnabled(false);
    }

    @Override
    public void startPublishInService(BeaconModel model) {
        // Start publish service in background.
        Intent intent = new Intent(getContext(), PublishService.class);
        intent.putExtra(IntentKey.NAMESPACE_ID.name(), model.mNamespaceId);
        intent.putExtra(IntentKey.INSTANCE_ID.name(), model.mInstanceId);
        getContext().startService(intent);
    }

    @Override
    public void showLINEUrl(String url) {
        mLINEUrlPreference.setText(url);
        mLINEUrlPreference.setSummary(url);
    }

    @Override
    public void showCMPreferences(CMLinksModel model) {
        mCMApiPreference.setSummary(model.mApiKey);
        mCMApiPreference.setText(model.mApiKey);

        mCMSecretPreference.setSummary(model.mSecretKey);
        mCMSecretPreference.setText(model.mSecretKey);

        mCMJidPreference.setSummary(model.mJid);
        mCMJidPreference.setText(model.mJid);

        mCMPrefernceCategory.addPreference(mCMApiPreference);
        mCMPrefernceCategory.addPreference(mCMSecretPreference);
        mCMPrefernceCategory.addPreference(mCMJidPreference);

        mPreferenceScreen.addPreference(mCMPrefernceCategory);
    }

    @Override
    public void updateCMApiKeyPreference(String apiKey) {
        mCMApiPreference.setSummary(apiKey);
        mCMApiPreference.setText(apiKey);
    }

    @Override
    public void updateCMSecretKeyPreference(String secretKey) {
        mCMSecretPreference.setSummary(secretKey);
        mCMSecretPreference.setText(secretKey);
    }

    @Override
    public void updateCMJidPreference(String jid) {
        mCMJidPreference.setSummary(jid);
        mCMJidPreference.setText(jid);
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        View view = getView();
        if (view != null) {
            Snackbar.make(getView(), resId, Snackbar.LENGTH_SHORT).show();
        }
    }
}
