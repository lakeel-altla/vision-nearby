package com.lakeel.profile.notification.presentation.view.fragment.settings.line;

import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.presentation.presenter.line.LineSettingsPresenter;
import com.lakeel.profile.notification.presentation.view.LineSettingsView;
import com.lakeel.profile.notification.presentation.view.activity.MainActivity;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

public final class LineSettingsFragment extends PreferenceFragmentCompat implements LineSettingsView {

    public static LineSettingsFragment newInstance() {
        return new LineSettingsFragment();
    }

    @Inject
    LineSettingsPresenter mPresenter;

    private static final String KEY_LINE_URL = "lineUrl";

    private EditTextPreference mLINEUrlPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_line);

        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.title_line_settings);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        mPresenter.onCreateView(this);

        mLINEUrlPreference = (EditTextPreference) findPreference(KEY_LINE_URL);
        mLINEUrlPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String lineUrl = (String) newValue;
            mPresenter.onSaveLineUrl(lineUrl);
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
    public void showLINEUrl(String url) {
        mLINEUrlPreference.setText(url);
        mLINEUrlPreference.setSummary(url);
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        View view = getView();
        if (view != null) {
            Snackbar.make(getView(), resId, Snackbar.LENGTH_SHORT).show();
        }
    }
}
