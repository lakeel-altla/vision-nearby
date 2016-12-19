package com.lakeel.altla.vision.nearby.presentation.view.animation;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public final class ExpandAnimation {

    private View subView;

    private boolean isViewExpanded = false;

    public ExpandAnimation(View subView) {
        this.subView = subView;
        this.subView.setVisibility(View.GONE);
        this.subView.setEnabled(false);
    }

    public void start() {
        if (isViewExpanded) {
            isViewExpanded = false;

            // Fade out
            Animation a = new AlphaAnimation(1.00f, 0.00f);
            a.setDuration(200);

            // Set a listener to the animation and configure onAnimationEnd
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    subView.setVisibility(View.GONE);
                    subView.setEnabled(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            // Set the animation on the custom view
            subView.startAnimation(a);
        } else {
            subView.setVisibility(View.VISIBLE);
            subView.setEnabled(true);
            isViewExpanded = true;
        }
    }
}
