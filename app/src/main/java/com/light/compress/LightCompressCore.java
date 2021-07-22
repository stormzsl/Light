package com.light.compress;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.util.Log;

public class LightCompressCore {

    static {
        System.loadLibrary("jpegbither");
        System.loadLibrary("light");
    }

    public final static String TAG = "LightCompressCore";

    /**
     * libjpeg库中压缩方法
     */
    private static native String compressBitmap(Bitmap bit, int w, int h, int quality, byte[] fileNameBytes, boolean optimize);

    /**
     * 图片按比例缩放之后调用libjpeg库的压缩
     */
    public static void saveBitmapWithScale(Bitmap bit, int quality, String fileName, boolean optimize, float scaleSize) {
        if(bit==null){
            return;
        }
        long startTime=System.currentTimeMillis();
        Bitmap result = Bitmap.createBitmap((int) (bit.getWidth() * scaleSize), (int) (bit.getHeight() * scaleSize), Bitmap.Config.ARGB_8888);// 比例缩放
        Canvas canvas = new Canvas(result);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Rect rect = new Rect(0, 0, (int) (bit.getWidth() * scaleSize), (int) (bit.getHeight() * scaleSize));// 比例缩放
        canvas.drawBitmap(bit, null, rect, null);
        compressBitmap(result, result.getWidth(), result.getHeight(), quality, fileName.getBytes(), optimize);
        long endTime=System.currentTimeMillis();
        Log.e(TAG,String.format("saveBitmapWithScale cost:%d ms",(endTime-startTime)));
    }

    /**
     * 指定图片宽和高 进行压缩
     */
    public static void saveBitmapWithAssign(Bitmap bit, int quality, String fileName, boolean optimize, int suggestWitch, int suggestHeight) {
        if(bit==null){
            return;
        }
        long startTime=System.currentTimeMillis();
        Bitmap result = Bitmap.createBitmap(suggestWitch, suggestHeight, Bitmap.Config.ARGB_8888);// 比例缩放
        Canvas canvas = new Canvas(result);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Rect rect = new Rect(0, 0, suggestHeight, suggestHeight);
        canvas.drawBitmap(bit, null, rect, null);
        compressBitmap(result, result.getWidth(), result.getHeight(), quality, fileName.getBytes(), optimize);
        long endTime=System.currentTimeMillis();
        Log.e(TAG,String.format("saveBitmapWithScale cost:%d ms",(endTime-startTime)));

    }
}
