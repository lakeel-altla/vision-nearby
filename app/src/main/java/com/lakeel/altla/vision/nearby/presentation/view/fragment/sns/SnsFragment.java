package com.lakeel.altla.vision.nearby.presentation.view.fragment.sns;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.constants.BundleKey;
import com.lakeel.altla.vision.nearby.presentation.presenter.sns.SnsPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.SnsView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class SnsFragment extends Fragment implements SnsView {

    public static SnsFragment newInstance(String userId) {
        Bundle args = new Bundle();
        args.getString(BundleKey.USER_ID.getValue(), userId);

        SnsFragment fragment = new SnsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    SnsPresenter presenter;

    @BindView(R.id.lineUrlText)
    TextView textViewLineUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sns, container, false);

        ButterKnife.bind(this, view);

        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        String userId = bundle.getString(BundleKey.USER_ID.getValue());
        presenter.setUserId(userId);

        presenter.onActivityCreated();
    }

    @Override
    public void showLineUrl(String url) {
        textViewLineUrl.setText(url);
    }
}
