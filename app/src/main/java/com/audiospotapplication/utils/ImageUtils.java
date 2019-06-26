package com.audiospotapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();

    public static void setImageFromUrlIntoImageViewUsingPicasso(String url, Context context, ImageView imageView, boolean withPreLoadingImage) {
        setImageFromUrlIntoImageViewUsingPicasso(url, context, imageView, withPreLoadingImage, null);
    }

    public static void setImageFromUrlIntoImageViewUsingPicasso(String url, Context context, ImageView imageView) {
        setImageFromUrlIntoImageViewUsingPicasso(url, context, imageView, true, null);
    }

    public static void setImageFromUrlIntoImageViewUsingPicasso(String url, Context context, ImageView imageView, boolean withPreLoadingImage, Callback cb) {
        if (url.isEmpty()) {
            return;
        }
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(url).memoryPolicy(MemoryPolicy.NO_STORE).into(imageView, cb);
    }

    public static void setImageBase64IntoImageView(String base64, View parentView, int targetImageViewID) {
        byte[] decodedbytes = Base64.decode(base64, 0);
        Bitmap bpm = BitmapFactory.decodeByteArray(decodedbytes, 0, decodedbytes.length);
        ((ImageView) parentView.findViewById(targetImageViewID)).setImageBitmap(bpm);

        return;
    }

    public static void setImageBase64IntoImageView(String base64, ImageView targetImageView) {
        byte[] decodedbytes = Base64.decode(base64, 0);
        Bitmap bpm = BitmapFactory.decodeByteArray(decodedbytes, 0, decodedbytes.length);
        targetImageView.setImageBitmap(bpm);

        return;
    }
}
