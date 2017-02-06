package com.lakeel.altla.vision.nearby.presentation.view.fragment.distance;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.estimation.DistanceEstimationPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.DistanceEstimationView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.BundleKey;
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

    @BindView(R.id.textViewDistance)
    TextView distanceTextView;

    @BindView(R.id.imageViewUser)
    ImageView userImageView;

    @BindView(R.id.imageViewCircle)
    ImageView circleImageView;

    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceEstimationFragment.class);

    private static final int REQUEST_CODE_ENABLE_BLE = 111;

    private static final int REQUEST_CODE_ACCESS_FINE_LOCATION = 222;

    public static DistanceEstimationFragment newInstance(ArrayList<String> beaconIds, String targetName) {
        Bundle args = new Bundle();
        args.putStringArrayList(BundleKey.BEACON_IDS.name(), beaconIds);
        args.putSerializable(BundleKey.TARGET_NAME.name(), targetName);

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

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(MyUser.getUserProfile().imageUri, userImageView);

        Bundle bundle = getArguments();
        List<String> beaconIds = bundle.getStringArrayList(BundleKey.BEACON_IDS.name());
        String targetName = bundle.getString(BundleKey.TARGET_NAME.name());

        String message = getResources().getString(R.string.toolbar_title_finding_for_format, targetName);
        getActivity().setTitle(message);

        distanceTextView.setText(getResources().getString(R.string.textView_finding));

        presenter.setBeaconIds(beaconIds);
        presenter.onActivityCreated();
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
                Snackbar.make(mainLayout, R.string.snackBar_error_not_enable_ble, Snackbar.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.onAccessFineLocationGranted();
            } else {
                LOGGER.warn("Access fine location permission is denied.");
                Snackbar.make(mainLayout, R.string.snackBar_error_not_find, Snackbar.LENGTH_SHORT).show();
            }
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
    public void startAnimation() {
        circleImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.circle, null));

        ScaleAnimation animation = new ScaleAnimation(1, 4.0f, 1, 4.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        animation.setRepeatCount(INFINITE);

        circleImageView.startAnimation(animation);
    }

    @Override
    public void showDistance(String meters) {
        String message = getResources().getString(R.string.snackBar_message_device_distance_format, meters);
        distanceTextView.setText(message);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void requestAccessFineLocationPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ACCESS_FINE_LOCATION);
    }

    @Override
    public void showBleEnabledActivity(Intent intent) {
        startActivityForResult(intent, REQUEST_CODE_ENABLE_BLE);
    }
}
