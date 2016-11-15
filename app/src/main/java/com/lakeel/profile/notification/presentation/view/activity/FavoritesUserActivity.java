package com.lakeel.profile.notification.presentation.view.activity;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.presentation.application.App;
import com.lakeel.profile.notification.presentation.di.component.UserComponent;
import com.lakeel.profile.notification.presentation.di.module.ActivityModule;
import com.lakeel.profile.notification.presentation.intent.IntentExtra;
import com.lakeel.profile.notification.presentation.presenter.activity.FavoritesUserActivityPresenter;
import com.lakeel.profile.notification.presentation.presenter.model.ItemModel;
import com.lakeel.profile.notification.presentation.presenter.model.PresenceModel;
import com.lakeel.profile.notification.presentation.view.DateFormatter;
import com.lakeel.profile.notification.presentation.view.FavoritesUserActivityView;
import com.lakeel.profile.notification.presentation.view.GridShareSheet;
import com.lakeel.profile.notification.presentation.view.layout.EmailLayout;
import com.lakeel.profile.notification.presentation.view.layout.LastOnlineLayout;
import com.lakeel.profile.notification.presentation.view.layout.LineUrlLayout;
import com.lakeel.profile.notification.presentation.view.layout.NameLayout;
import com.lakeel.profile.notification.presentation.view.layout.PresenceHeaderLayout;
import com.lakeel.profile.notification.presentation.view.layout.PresenceLayout;
import com.lakeel.profile.notification.presentation.view.layout.ProfileHeaderLayout;
import com.lakeel.profile.notification.presentation.view.layout.SNSHeaderLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lakeel.profile.notification.R.id.share;

public final class FavoritesUserActivity extends AppCompatActivity implements FavoritesUserActivityView {

    @BindView(R.id.main_layout)
    CoordinatorLayout mMainLayout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.imageView_profile)
    ImageView mImageView;

    @BindView(R.id.shareSheet)
    BottomSheetLayout mShareSheet;

    @Inject
    FavoritesUserActivityPresenter mPresenter;

    private PresenceHeaderLayout mPresenceHeaderLayout = new PresenceHeaderLayout();

    private PresenceLayout mPresenceLayout = new PresenceLayout();

    private LastOnlineLayout mLastOnlineLayout = new LastOnlineLayout();

    private ProfileHeaderLayout mProfileHeaderLayout = new ProfileHeaderLayout();

    private NameLayout mNameLayout = new NameLayout();

    private EmailLayout mEmailLayout = new EmailLayout();

    private SNSHeaderLayout mSNSHeaderLayout = new SNSHeaderLayout();

    private LineUrlLayout mLineUrlLayout = new LineUrlLayout();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_user);

        setTitle(null);

        ButterKnife.bind(this);

        ButterKnife.bind(mPresenceHeaderLayout, findViewById(R.id.presence_header));
        ButterKnife.bind(mPresenceLayout, findViewById(R.id.presence));
        ButterKnife.bind(mLastOnlineLayout, findViewById(R.id.last_online));
        ButterKnife.bind(mProfileHeaderLayout, findViewById(R.id.profile_header));
        ButterKnife.bind(mNameLayout, findViewById(R.id.name));
        ButterKnife.bind(mEmailLayout, findViewById(R.id.email));
        ButterKnife.bind(mSNSHeaderLayout, findViewById(R.id.sns_header));
        ButterKnife.bind(mLineUrlLayout, findViewById(R.id.lineUrl));

        // Dagger。
        UserComponent userComponent = App.getApplicationComponent(this)
                .userComponent(new ActivityModule(this));
        userComponent.inject(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPresenter.onCreateView(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Handle an intent.
        Intent intent = getIntent();
        String id = intent.getStringExtra(IntentExtra.ID.name());
        mPresenter.setId(id);

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
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        boolean isCmLinkEnabled = mPresenter.isCmLinkEnabled();

        if (!isCmLinkEnabled) {
            menu.setGroupVisible(0, false);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case share:
                mPresenter.onShare();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        Snackbar.make(mMainLayout, resId, Snackbar.LENGTH_SHORT).show();
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
        imageLoader.displayImage(model.mImageUri, mImageView);

        mNameLayout.mBody.setText(model.mName);
        mEmailLayout.mBody.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        mEmailLayout.mBody.setText(model.mEmail);
    }

    @Override
    public void showTitle(String title) {
        mCollapsingToolbarLayout.setTitle(title);
    }

    @Override
    public void showShareSheet() {
        GridShareSheet shareSheet = new GridShareSheet(getApplicationContext(), mShareSheet, R.menu.menu_share);
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
        invalidateOptionsMenu();
    }

    @Override
    public void showLineUrl(String url) {
        mLineUrlLayout.mBody.setAutoLinkMask(Linkify.WEB_URLS);
        mLineUrlLayout.mBody.setText(url);
    }
}
