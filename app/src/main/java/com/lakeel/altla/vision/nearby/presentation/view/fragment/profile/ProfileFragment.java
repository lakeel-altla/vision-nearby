package com.lakeel.altla.vision.nearby.presentation.view.fragment.profile;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.constants.BundleKey;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.ItemModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PresenceModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.profile.ProfilePresenter;
import com.lakeel.altla.vision.nearby.presentation.view.DateFormatter;
import com.lakeel.altla.vision.nearby.presentation.view.GridShareSheet;
import com.lakeel.altla.vision.nearby.presentation.view.ProfileView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.layout.PresenceLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.ProfileLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.SNSLayout;
import com.lakeel.altla.vision.nearby.presentation.view.transaction.FragmentController;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lakeel.altla.vision.nearby.R.id.find;
import static com.lakeel.altla.vision.nearby.R.id.share;

public final class ProfileFragment extends Fragment implements ProfileView {

    public static ProfileFragment newInstance(String userId, String userName) {
        Bundle args = new Bundle();
        args.putString(BundleKey.USER_ID.getValue(), userId);
        args.putString(BundleKey.USER_NAME.getValue(), userName);

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    ProfilePresenter mPresenter;

    @BindView(R.id.mainLayout)
    LinearLayout mLayout;

    @BindView(R.id.shareSheet)
    BottomSheetLayout mShareSheet;

    @BindView(R.id.imageViewUser)
    ImageView mUserImageView;

    private PresenceLayout presenceLayout = new PresenceLayout();

    private ProfileLayout profileLayout = new ProfileLayout();

    private SNSLayout snsLayout = new SNSLayout();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);

        ButterKnife.bind(presenceLayout, view.findViewById(R.id.presenceLayout));
        ButterKnife.bind(profileLayout, view.findViewById(R.id.profileLayout));
        ButterKnife.bind(snsLayout, view.findViewById(R.id.snsLayout));

        setHasOptionsMenu(true);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        mPresenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);

        Bundle bundle = getArguments();
        String userId = bundle.getString(BundleKey.USER_ID.getValue());
        String userName = bundle.getString(BundleKey.USER_NAME.getValue());

        getActivity().setTitle(userName);

        mPresenter.setUserData(userId, userName);

        mPresenter.onActivityCreated();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        boolean isCmLinkEnabled = mPresenter.isCmLinkEnabled();
        MenuItem menuItem = menu.findItem(R.id.share);
        menuItem.setVisible(isCmLinkEnabled);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case share:
                mPresenter.onShare();
                break;
            case find:
                mPresenter.onFindNearbyDeviceMenuClicked();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        Snackbar.make(mLayout, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showPresence(PresenceModel model) {
        int resId;
        if (model.mConnected) {
            resId = R.string.textView_connected;
        } else {
            resId = R.string.textView_disconnected;
        }
        presenceLayout.presenceText.setText(resId);

        DateFormatter dateFormatter = new DateFormatter(model.mLastOnline);
        presenceLayout.lastOnlineText.setText(dateFormatter.format());
    }

    @Override
    public void showProfile(ItemModel model) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(model.mImageUri, mUserImageView);

        profileLayout.nameText.setText(model.mName);
        profileLayout.emailText.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        profileLayout.emailText.setText(model.mEmail);
    }

    @Override
    public void showShareSheet() {
        GridShareSheet shareSheet = new GridShareSheet(getContext(), mShareSheet, R.menu.menu_share);
        shareSheet.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_cm_favorites) {
                mPresenter.onCmMenuClicked();
            }
            return true;
        });

        if (!mPresenter.isCmLinkEnabled()) {
            shareSheet.hideMenuItem(R.id.menu_cm_favorites);
        }
        shareSheet.show();
    }

    @Override
    public void initializeOptionMenu() {
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void showLineUrl(String url) {
        snsLayout.lineUrlText.setAutoLinkMask(Linkify.WEB_URLS);
        snsLayout.lineUrlText.setText(url);
    }

    @Override
    public void showFindNearbyDeviceFragment(ArrayList<String> beaconIds, String targetName) {
        FragmentController controller = new FragmentController(getActivity().getSupportFragmentManager());
        controller.showDeviceDistanceEstimationFragment(beaconIds, targetName);
    }
}
