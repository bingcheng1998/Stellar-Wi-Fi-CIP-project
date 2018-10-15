package com.sansi.stellarWiFi.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.DrawableRes;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/14 16:04
 *          类说明
 */
public class BitmapUtils {
    public static Bitmap tintBitmap(Bitmap inBitmap, int tintColor) {
        if (inBitmap == null) {
            return null;
        }
        Bitmap outBitmap = Bitmap.createBitmap(inBitmap.getWidth(), inBitmap.getHeight(), inBitmap.getConfig());
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(inBitmap, 0, 0, paint);
        return outBitmap;
    }

    public static Bitmap tintBitmap(Context context, @DrawableRes int id, int tintColor) {
        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), id);
        return tintBitmap(decodeResource, tintColor);
    }

}
