package com.lakeel.altla.vision.nearby.presentation.view.fragment.information;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.bundle.FragmentBundle;
import com.lakeel.altla.vision.nearby.presentation.presenter.information.InformationPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.InformationModel;
import com.lakeel.altla.vision.nearby.presentation.view.InformationView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class InformationFragment extends Fragment implements InformationView {

    public static InformationFragment newInstance(String informationId) {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentBundle.INFORMATION_ID.name(), informationId);

        InformationFragment fragment = new InformationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Inject
    InformationPresenter presenter;

    @BindView(R.id.textViewInformationTitle)
    TextView titleTextView;

    @BindView(R.id.textViewInformationBody)
    TextView bodyTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infromation, container, false);

        ButterKnife.bind(this, view);

        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.title_information);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);

        Bundle bundle = getArguments();
        String informationId = (String) bundle.get(FragmentBundle.INFORMATION_ID.name());

        presenter.onActivityCreated(informationId);
    }

    @Override
    public void showInformation(InformationModel model) {
        titleTextView.setText(model.title);
        bodyTextView.setText(model.body);
    }
}
