package com.lakeel.altla.vision.nearby.presentation.view.fragment.distance;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.bundle.FragmentBundle;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.estimation.DistanceEstimationPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.DistanceEstimationView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static android.view.animation.Animation.INFINITE;

public final class DistanceEstimationFragment extends Fragment implements DistanceEstimationView {

    @Inject
    DistanceEstimationPresenter presenter;

    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;

    @BindView(R.id.textViewDescription)
    TextView descriptionTextView;

    @BindView(R.id.textViewDistance)
    TextView distanceTextView;

    @BindView(R.id.imageViewUser)
    ImageView userImageView;

    @BindView(R.id.imageViewCircle)
    ImageView circleImageView;

    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceEstimationFragment.class);

    private static final int REQUEST_CODE_ENABLE_BLE = 1;

    public static DistanceEstimationFragment newInstance(ArrayList<String> beaconIds, String targetName) {
        Bundle args = new Bundle();
        args.putStringArrayList(FragmentBundle.BEACON_IDS.name(), beaconIds);
        args.putSerializable(FragmentBundle.TARGET_NAME.name(), targetName);

        DistanceEstimationFragment fragment = new DistanceEstimationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estimate_distance, container, false);
        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.title_find_nearby_device);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(MyUser.getUserData().imageUri, userImageView);

        Bundle bundle = getArguments();
        List<String> beaconIds = bundle.getStringArrayList(FragmentBundle.BEACON_IDS.name());
        String targetName = bundle.getString(FragmentBundle.TARGET_NAME.name());

        String message = getResources().getString(R.string.message_finding_for_nearby_device_format, targetName);
        descriptionTextView.setText(message);

        presenter.setBeaconIds(beaconIds);
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.onResume();

        circleImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.circle, null));

        ScaleAnimation animation = new ScaleAnimation(1, 3.0f, 1, 3.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        animation.setRepeatCount(INFINITE);

        circleImageView.startAnimation(animation);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_ENABLE_BLE == requestCode) {
            if (RESULT_OK == resultCode) {
                presenter.subscribe();
            } else {
                LOGGER.error("Failed to enable BLE.");
                Snackbar.make(mainLayout, R.string.error_not_enable_ble, Snackbar.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
        return false;
    }

    @Override
    public void showDistance(String meters) {
        if (isResumed()) {
            String message = getResources().getString(R.string.message_device_distance_format, meters);
            distanceTextView.setText(message);
        }
    }

    @Override
    public void showBleEnabledActivity(Intent intent) {
        startActivityForResult(intent, REQUEST_CODE_ENABLE_BLE);
    }
}
