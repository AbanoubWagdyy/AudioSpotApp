package com.audiospotapplication.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtils {

    private static ProgressDialog progressDialog;
    private static ProgressDialog playingProgressDialog;

    public static synchronized void showProgressDialog(Context context, String message) {
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public static synchronized Boolean isProgressShowing() {
        if (progressDialog == null)
            return false;
        return progressDialog.isShowing();
    }

    public static synchronized void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public static synchronized void showDownloadingDialog(Context context, String message) {
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }


    public static synchronized void showPlayingProgressDialog(Context context) {
        if (playingProgressDialog == null || !playingProgressDialog.isShowing()) {
            playingProgressDialog = new ProgressDialog(context);
            playingProgressDialog.setMessage("Playing ...");
            playingProgressDialog.setCancelable(false);
            playingProgressDialog.show();
        }
    }

    public static synchronized Boolean isPlayingProgressShowing() {
        if (playingProgressDialog == null)
            return false;
        return playingProgressDialog.isShowing();
    }

    public static synchronized void dismissPlayingProgressDialog() {
        if (playingProgressDialog != null && playingProgressDialog.isShowing())
            playingProgressDialog.dismiss();
    }
}