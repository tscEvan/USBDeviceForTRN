package com.example.usbdevicefortrn.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.usbdevicefortrn.R;

public class LoadingDialog extends Dialog {
    String mMessage;
    boolean mCancelable;


    public LoadingDialog(@NonNull Context context, int themeResId, String mMessage, boolean mCancelable) {
        super(context,themeResId);
        this.mMessage = mMessage;
        this.mCancelable = mCancelable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
        WindowManager windowManager = getWindow().getWindowManager();
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = screenWidth/3;
        attributes.height = attributes.width;
        getWindow().setAttributes(attributes);
        setCancelable(mCancelable);

        TextView textView = findViewById(R.id.loading);
        textView.setText(mMessage);
    }
}
