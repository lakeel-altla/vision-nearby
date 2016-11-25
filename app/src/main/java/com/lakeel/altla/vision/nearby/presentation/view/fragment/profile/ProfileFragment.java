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
import com.lakeel.altla.vision.nearby.presentation.view.layout.EmailLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.LastOnlineLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.LineUrlLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.NameLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.PresenceHeaderLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.PresenceLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.ProfileHeaderLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.SNSHeaderLayout;
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

    @BindView(R.id.profileLayout)
    LinearLayout mLayout;

    @BindView(R.id.shareSheet)
    BottomSheetLayout mShareSheet;

    @BindView(R.id.imageViewUser)
    ImageView mUserImageView;

    private PresenceHeaderLayout mPresenceHeaderLayout = new PresenceHeaderLayout();

    private PresenceLayout mPresenceLayout = new PresenceLayout();

    private LastOnlineLayout mLastOnlineLayout = new LastOnlineLayout();

    private ProfileHeaderLayout mProfileHeaderLayout = new ProfileHeaderLayout();

    private NameLayout mNameLayout = new NameLayout();

    private EmailLayout mEmailLayout = new EmailLayout();

    private SNSHeaderLayout mSNSHeaderLayout = new SNSHeaderLayout();

    private LineUrlLayout mLineUrlLayout = new LineUrlLayout();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);

        ButterKnife.bind(mPresenceHeaderLayout, view.findViewById(R.id.presence_header));
        ButterKnife.bind(mPresenceLayout, view.findViewById(R.id.presence));
        ButterKnife.bind(mLastOnlineLayout, view.findViewById(R.id.last_online));
        ButterKnife.bind(mProfileHeaderLayout, view.findViewById(R.id.profile_header));
        ButterKnife.bind(mNameLayout, view.findViewById(R.id.name));
        ButterKnife.bind(mEmailLayout, view.findViewById(R.id.email));
        ButterKnife.bind(mSNSHeaderLayout, view.findViewById(R.id.sns_header));
        ButterKnife.bind(mLineUrlLayout, view.findViewById(R.id.lineUrl));

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

        mPresenceHeaderLayout.mTitle.setText(R.string.textView_presence);
        mPresenceLayout.mTitle.setText(R.string.textView_state);
        mLastOnlineLayout.mTitle.setText(R.string.textView_last_online);
        mProfileHeaderLayout.mTitle.setText(R.string.textView_profile);
        mNameLayout.mTitle.setText(R.string.textView_name);
        mEmailLayout.mTitle.setText(R.string.textView_email);
        mSNSHeaderLayout.mTitle.setText(R.string.textView_sns);
        mLineUrlLayout.mTitle.setText(R.string.textView_line_url);
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
        if (model.mConnected) {
            mPresenceLayout.mBody.setText(R.string.textView_connected);
        } else {
            mPresenceLayout.mBody.setText(R.string.textView_disconnected);
        }

        DateFormatter dateFormatter = new DateFormatter(model.mLastOnline);
        mLastOnlineLayout.mBody.setText(dateFormatter.format());
    }

    @Override
    public void showProfile(ItemModel model) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(model.mImageUri, mUserImageView);

        mNameLayout.mBody.setText(model.mName);
        mEmailLayout.mBody.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        mEmailLayout.mBody.setText(model.mEmail);
    }

    @Override
    public void showShareSheet() {
        GridShareSheet shareSheet = new GridShareSheet(getContext(), mShareSheet, R.menu.menu_share);
        shareSheet.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_cm_favorites) {
                mPresenter.onAddToCmFavorites();
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
        mLineUrlLayout.mBody.setAutoLinkMask(Linkify.WEB_URLS);
        mLineUrlLayout.mBody.setText(url);
    }

    @Override
    public void showFindNearbyDeviceFragment(ArrayList<String> beaconIds, String targetName) {
        FragmentController controller = new FragmentController(getActivity().getSupportFragmentManager());
        controller.showDeviceDistanceEstimationFragment(beaconIds, targetName);
    }
}
