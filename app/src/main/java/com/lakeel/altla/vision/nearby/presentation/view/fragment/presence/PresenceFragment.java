package com.lakeel.altla.vision.nearby.presentation.view.fragment.presence;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.constants.BundleKey;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PresenceModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.presence.PresencePresenter;
import com.lakeel.altla.vision.nearby.presentation.view.DateFormatter;
import com.lakeel.altla.vision.nearby.presentation.view.PresenceView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class PresenceFragment extends Fragment implements PresenceView {

    public static PresenceFragment newInstance(String userId) {
        Bundle args = new Bundle();
        args.putString(BundleKey.USER_ID.getValue(), userId);

        PresenceFragment fragment = new PresenceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    PresencePresenter presenter;

    @BindView(R.id.presenceText)
    TextView textViewPresence;

    @BindView(R.id.lastOnlineText)
    TextView textViewLastOnline;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_state, container, false);

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
    public void showPresence(PresenceModel model) {
        int resId;
        if (model.isConnected) {
            resId = R.string.textView_connected;
        } else {
            resId = R.string.textView_disconnected;
        }
        textViewPresence.setText(resId);

        DateFormatter dateFormatter = new DateFormatter(model.lastOnlineTime);
        textViewLastOnline.setText(dateFormatter.format());
    }
}
