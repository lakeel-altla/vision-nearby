package com.lakeel.altla.vision.nearby.presentation.view.layout;

import android.widget.ImageView;
import android.widget.TextView;

import com.lakeel.altla.vision.nearby.R;

import butterknife.BindView;

public class DrawerHeaderLayout {

    @BindView(R.id.imageViewUser)
    public ImageView userImageView;

    @BindView(R.id.textViewUserName)
    public TextView userNameTextView;

    @BindView(R.id.textViewEmail)
    public TextView emailTextView;
}

