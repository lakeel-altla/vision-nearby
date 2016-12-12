package com.lakeel.altla.vision.nearby.presentation.view.fragment.estimation;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.constants.BundleKey;
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

    public static DistanceEstimationFragment newInstance(ArrayList<String> beaconIds, String targetName) {

        Bundle args = new Bundle();
        args.putStringArrayList(BundleKey.BEACON_IDS.getValue(), beaconIds);
        args.putSerializable(BundleKey.TARGET_NAME.getValue(), targetName);

        DistanceEstimationFragment fragment = new DistanceEstimationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    DistanceEstimationPresenter presenter;

    @BindView(R.id.textViewDescription)
    TextView distanceDescriptionText;

    @BindView(R.id.textViewDistance)
    TextView distanceText;

    @BindView(R.id.imageView_user)
    ImageView userImageView;

    @BindView(R.id.imageViewCircle)
    ImageView circleImage;

    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceEstimationFragment.class);

    private static final int REQUEST_CODE_SUBSCRIBE_RESULT = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_nearby_device, container, false);
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
        List<String> beaconIds = bundle.getStringArrayList(BundleKey.BEACON_IDS.getValue());
        String beaconName = bundle.getString(BundleKey.TARGET_NAME.getValue());

        String message = getResources().getString(R.string.message_finding_for_nearby_device_format, beaconName);
        distanceDescriptionText.setText(message);

        presenter.buildSubscriber(beaconIds);
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.onResume();

        circleImage.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.circle, null));

        ScaleAnimation animation = new ScaleAnimation(1, 3.0f, 1, 3.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        animation.setRepeatCount(INFINITE);

        circleImage.startAnimation(animation);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
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
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_SUBSCRIBE_RESULT == requestCode && resultCode == RESULT_OK) {
            presenter.onSubscribe();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void showDistance(String meters) {
        if (isResumed()) {
            String message = getResources().getString(R.string.message_device_distance_format, meters);
            distanceText.setText(message);
        }
    }

    @Override
    public void showResolutionSystemDialog(Status status) {
        try {
            status.startResolutionForResult(getActivity(), REQUEST_CODE_SUBSCRIBE_RESULT);
        } catch (IntentSender.SendIntentException e) {
            LOGGER.error("Failed to show resolution dialog for nearby.", e);
        }
    }
}
