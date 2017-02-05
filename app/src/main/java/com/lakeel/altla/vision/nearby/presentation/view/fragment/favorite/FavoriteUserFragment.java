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

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.view.bundle.FragmentBundle;
import com.lakeel.altla.vision.nearby.presentation.presenter.favorite.FavoriteUserPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteUserModel;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteUserView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.date.DateFormatter;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.FragmentController;
import com.lakeel.altla.vision.nearby.presentation.view.layout.PresenceLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.ProfileLayout;
import com.lakeel.altla.vision.nearby.presentation.view.layout.SnsLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lakeel.altla.vision.nearby.R.id.estimate;

public final class FavoriteUserFragment extends Fragment implements FavoriteUserView {

    @Inject
    FavoriteUserPresenter presenter;

    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;

    @BindView(R.id.imageViewUser)
    ImageView userImageView;

    private PresenceLayout presenceLayout = new PresenceLayout();

    private ProfileLayout profileLayout = new ProfileLayout();

    private SnsLayout snsLayout = new SnsLayout();

    public static FavoriteUserFragment newInstance(String userId, String userName) {
        Bundle args = new Bundle();
        args.putString(FragmentBundle.USER_ID.name(), userId);
        args.putString(FragmentBundle.USER_NAME.name(), userName);

        FavoriteUserFragment fragment = new FavoriteUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_user, container, false);

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
        String userId = bundle.getString(FragmentBundle.USER_ID.name());
        String userName = bundle.getString(FragmentBundle.USER_NAME.name());

        getActivity().setTitle(userName);

        presenter.setUserIdAndUserName(userId, userName);

        presenter.onActivityCreated();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favorite_user, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case estimate:
                presenter.onEstimateDistanceMenuClick();
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
    public void showPresence(FavoriteUserModel model) {
        int resId;
        if (model.isConnected) {
            resId = R.string.textView_connected;
        } else {
            resId = R.string.textView_disconnected;
        }
        presenceLayout.presenceTextView.setText(resId);

        DateFormatter dateFormatter = new DateFormatter(model.lastOnlineTime);
        presenceLayout.lastOnlineTextView.setText(dateFormatter.format());
    }

    @Override
    public void showProfile(FavoriteUserModel model) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(model.imageUri, userImageView);

        profileLayout.userNameTextView.setText(model.userName);

        profileLayout.emailTextView.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        profileLayout.emailTextView.setText(model.email);
    }

    @Override
    public void showLineUrl(FavoriteUserModel model) {
        snsLayout.lineUrlTextView.setAutoLinkMask(Linkify.WEB_URLS);
        snsLayout.lineUrlTextView.setText(model.lineUrl);
    }

    @Override
    public void showDistanceEstimationFragment(ArrayList<String> beaconIds, String targetName) {
        FragmentController controller = new FragmentController(this);
        controller.showDistanceEstimationFragment(beaconIds, targetName);
    }
}
