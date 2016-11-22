package com.lakeel.altla.vision.nearby.presentation.view.fragment.estimation;

import com.google.android.gms.common.api.Status;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.constants.BundleKey;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.find.DeviceDistanceEstimationPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.DeviceDistanceEstimationView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static android.view.animation.Animation.INFINITE;

public final class DeviceDistanceEstimationFragment extends Fragment implements DeviceDistanceEstimationView {

    public static DeviceDistanceEstimationFragment newInstance(String beaconId, String beaconName) {

        Bundle args = new Bundle();
        args.putString(BundleKey.BEACON_ID.getValue(), beaconId);
        args.putSerializable(BundleKey.BEACON_NAME.getValue(), beaconName);

        DeviceDistanceEstimationFragment fragment = new DeviceDistanceEstimationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    DeviceDistanceEstimationPresenter mPresenter;

    @BindView(R.id.textViewDescription)
    TextView mDistanceDescriptionText;

    @BindView(R.id.textViewDistance)
    TextView mDistanceText;

    @BindView(R.id.imageView_user)
    ImageView mUserImageView;

    @BindView(R.id.imageViewCircle)
    ImageView mCircleImage;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceDistanceEstimationFragment.class);

    private static final int REQUEST_CODE_SUBSCRIBE_RESULT = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_nearby_device, container, false);
        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        mPresenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPresenter.onActivityCreated();

        getActivity().setTitle(R.string.title_find_nearby_device);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(MyUser.getUserData().mImageUri, mUserImageView);
        mUserImageView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale));

        Bundle bundle = getArguments();
        String beaconId = bundle.getString(BundleKey.BEACON_ID.getValue());
        String beaconName = bundle.getString(BundleKey.BEACON_NAME.getValue());

        String message = getResources().getString(R.string.message_finding_for_your_device_format, beaconName);
        mDistanceDescriptionText.setText(message);

        mPresenter.setSubscriber(beaconId);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();

        mCircleImage.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.circle, null));
        ScaleAnimation animation = new ScaleAnimation(1, 3.0f, 1, 3.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        animation.setRepeatCount(INFINITE);
        mCircleImage.startAnimation(animation);
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
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_SUBSCRIBE_RESULT == requestCode && resultCode == RESULT_OK) {
            mPresenter.onSubscribe();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void showDistance(String meters) {
        if (isResumed()) {
            String message = getResources().getString(R.string.message_device_distance_format, meters);
            mDistanceText.setText(message);
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
