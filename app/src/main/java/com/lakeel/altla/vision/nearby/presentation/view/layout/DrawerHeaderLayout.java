package com.lakeel.altla.vision.nearby.presentation.view.layout;

import android.widget.ImageView;
import android.widget.TextView;

import com.lakeel.altla.vision.nearby.R;

import butterknife.BindView;

public class DrawerHeaderLayout {

    @BindView(R.id.imageView_header_profile)
    public ImageView mProfile;

    @BindView(R.id.textView_header_userName)
    public TextView mUserName;

    @BindView(R.id.textView_header_user_address)
    public TextView mUserAddress;
}

