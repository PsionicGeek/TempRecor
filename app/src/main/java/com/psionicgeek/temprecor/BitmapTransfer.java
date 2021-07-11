package com.psionicgeek.temprecor;
import android.graphics.Bitmap;

public class BitmapTransfer {
    public static Bitmap bitmap;

    public BitmapTransfer(Bitmap bitmap1) {
        bitmap =bitmap1;
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap bitmap) {
        BitmapTransfer.bitmap = bitmap;
    }
}
