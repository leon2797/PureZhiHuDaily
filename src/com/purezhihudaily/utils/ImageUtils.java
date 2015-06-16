package com.purezhihudaily.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Image工具类
 * 
 */
public class ImageUtils {
	
	private ImageUtils() {
		
	}

    /**
     * Bitmap转为 byte[]
     * 
     * @param bitmap
     * @return byte[]
     */
    public static byte[] bitmapToByte(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * byte[]转为bitmap
     * 
     * @param bytes
     * @return Bitmap
     */
    public static Bitmap byteToBitmap(byte[] bytes) {
        return (bytes == null || bytes.length == 0) ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Drawable转为Bitmap
     * 
     * @param drawable
     * @return Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        return drawable == null ? null : ((BitmapDrawable)drawable).getBitmap();
    }

    /**
     * Bitmap转为Drawable
     * 
     * @param bitmap
     * @return
     */
    @SuppressWarnings("deprecation")
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(bitmap);
    }

    /**
     * Drawable转为byte[]
     * 
     * @param drawable
     * @return byte[]
     */
    public static byte[] drawableToByte(Drawable drawable) {
        return bitmapToByte(drawableToBitmap(drawable));
    }

    /**
     * byte[]转为Drawable
     * 
     * @param bytes
     * @return Drawable
     */
    public static Drawable byteToDrawable(byte[] bytes) {
        return bitmapToDrawable(byteToBitmap(bytes));
    }

    /**
     * 缩放image
     * 
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return Bitmap
     */
    public static Bitmap scaleImageTo(Bitmap bitmap, int newWidth, int newHeight) {
        return scaleImage(bitmap, (float)newWidth / bitmap.getWidth(), (float)newHeight / bitmap.getHeight());
    }

    /**
     * 缩放image
     * 
     * @param bitmap
     * @param scaleWidth sacle of width
     * @param scaleHeight scale of height
     * @return Bitmap
     */
    public static Bitmap scaleImage(Bitmap bitmap, float scaleWidth, float scaleHeight) {
        if (bitmap == null) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}
