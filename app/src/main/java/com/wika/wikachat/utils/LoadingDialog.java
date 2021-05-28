package com.wika.wikachat.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.wika.wikachat.R;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public void startDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.custom_loading_screen, null));
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

}
