package com.lakeel.altla.vision.nearby.presentation.view.fragment.favorite;

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

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.constants.BundleKey;
import com.lakeel.altla.vision.nearby.presentation.presenter.favorite.FavoritePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PresenceModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.UserModel;
import com.lakeel.altla.vision.nearby.presentation.view.DateFormatter;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteView;
import com.lakeel.altla.vision.nearby.presentation.view.GridShareSheet;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.layout.PresenceLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.ProfileLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.SnsLayout;
import com.lakeel.altla.vision.nearby.presentation.view.transaction.FragmentController;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lakeel.altla.vision.nearby.R.id.find;
import static com.lakeel.altla.vision.nearby.R.id.share;

public final class FavoriteFragment extends Fragment implements FavoriteView {

    public static FavoriteFragment newInstance(String userId, String userName) {
        Bundle args = new Bundle();
        args.putString(BundleKey.USER_ID.getValue(), userId);
        args.putString(BundleKey.USER_NAME.getValue(), userName);

        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    FavoritePresenter presenter;

    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;

    @BindView(R.id.shareSheet)
    BottomSheetLayout shareSheet;

    @BindView(R.id.imageViewUser)
    ImageView userImageView;

    private PresenceLayout presenceLayout = new PresenceLayout();

    private ProfileLayout profileLayout = new ProfileLayout();

    private SnsLayout snsLayout = new SnsLayout();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        ButterKnife.bind(this, view);

        ButterKnife.bind(presenceLayout, view.findViewById(R.id.presenceLayout));
        ButterKnife.bind(profileLayout, view.findViewById(R.id.profileLayout));
        ButterKnife.bind(snsLayout, view.findViewById(R.id.snsLayout));

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

        Bundle bundle = getArguments();
        String userId = bundle.getString(BundleKey.USER_ID.getValue());
        String userName = bundle.getString(BundleKey.USER_NAME.getValue());

        getActivity().setTitle(userName);

        presenter.setUserData(userId, userName);

        presenter.onActivityCreated();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        boolean isCmLinkEnabled = presenter.isCmLinkEnabled();
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
                presenter.onShare();
                break;
            case find:
                presenter.onNearbyDeviceMenuClicked();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        Snackbar.make(mainLayout, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showPresence(PresenceModel model) {
        int resId;
        if (model.isConnected) {
            resId = R.string.textView_connected;
        } else {
            resId = R.string.textView_disconnected;
        }
        presenceLayout.textViewPresence.setText(resId);

        DateFormatter dateFormatter = new DateFormatter(model.lastOnlineTime);
        presenceLayout.textViewLastOnline.setText(dateFormatter.format());
    }

    @Override
    public void showProfile(UserModel model) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(model.imageUri, userImageView);

        profileLayout.textViewName.setText(model.name);
        profileLayout.textViewEmail.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        profileLayout.textViewEmail.setText(model.email);
    }

    @Override
    public void showShareSheet() {
        GridShareSheet shareSheet = new GridShareSheet(getContext(), this.shareSheet, R.menu.menu_share);
        shareSheet.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_cm_favorites) {
                presenter.onCmMenuClicked();
            }
            return true;
        });

        if (!presenter.isCmLinkEnabled()) {
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
        snsLayout.textViewLineUrl.setAutoLinkMask(Linkify.WEB_URLS);
        snsLayout.textViewLineUrl.setText(url);
    }

    @Override
    public void showFindNearbyDeviceFragment(ArrayList<String> beaconIds, String targetName) {
        FragmentController controller = new FragmentController(getActivity().getSupportFragmentManager());
        controller.showDeviceDistanceEstimationFragment(beaconIds, targetName);
    }
}
