package com.lakeel.altla.vision.nearby.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputType;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lakeel.altla.vision.nearby.R;

public final class SendMessageDialog {

    private final MaterialDialog.Builder builder;

    public SendMessageDialog(@NonNull Context context, MaterialDialog.InputCallback callback) {
        builder = new MaterialDialog.Builder(context);
        builder.title(R.string.title_send_message);
        builder.positiveText(R.string.dialog_button_send);
        builder.negativeText(R.string.dialog_button_cancel);
        builder.inputType(InputType.TYPE_CLASS_TEXT);
        builder.input(0, 0, callback);
    }

    public void show() {
        builder.show();
    }
}
