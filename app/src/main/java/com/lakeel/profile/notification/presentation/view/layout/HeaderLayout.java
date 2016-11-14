package com.lakeel.profile.notification.presentation.view.layout;

import com.lakeel.profile.notification.R;

import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;

public class HeaderLayout {

    @BindView(R.id.imageView_header_profile)
    public ImageView mProfile;

    @BindView(R.id.textView_header_userName)
    public TextView mUserName;

    @BindView(R.id.textView_header_user_address)
    public TextView mUserAddress;
}

