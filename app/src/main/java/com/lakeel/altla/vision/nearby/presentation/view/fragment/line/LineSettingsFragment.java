package com.lakeel.altla.vision.nearby.presentation.view.fragment.line;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.presentation.presenter.line.LineSettingsPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.LineSettingsView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class LineSettingsFragment extends Fragment implements LineSettingsView {

    @Inject
    LineSettingsPresenter presenter;

    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;

    @BindView(R.id.textViewLineUrl)
    TextView lineUrlTextView;

    public static LineSettingsFragment newInstance() {
        return new LineSettingsFragment();
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line_settings, container, false);
        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.toolbar_title_line_settings);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);

        mainLayout.setOnClickListener(view ->
                new MaterialDialog.Builder(getContext())
                        .title(R.string.dialog_title_line_url)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .negativeText(R.string.dialog_button_cancel)
                        .cancelable(true)
                        .input(StringUtils.EMPTY, StringUtils.EMPTY, false, (dialog1, input) ->
                                presenter.onSave(input.toString())
                        )
                        .show());

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
    public void showLineUrl(String url) {
        lineUrlTextView.setText(url);
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        Snackbar.make(mainLayout, resId, Snackbar.LENGTH_SHORT).show();
    }
}
