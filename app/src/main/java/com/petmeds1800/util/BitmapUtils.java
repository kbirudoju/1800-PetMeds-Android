package com.petmeds1800.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for Bitmap related methods.
 *
 *
 */
public abstract class BitmapUtils {

    private static String LOG_TAG = "BitmapUtils";

    public static Bitmap getScaledBitmap(File f, int maxImageSize) {
        FileInputStream fstream = null;
        FileDescriptor fd = null;
        try {
            fstream = new FileInputStream(f);
            fd = fstream.getFD();
            if (fd != null) {
                return BitmapUtils.decodeSampledBitmapFromDescriptor(fd, maxImageSize);
            } else {
                return BitmapUtils.decodeSampledBitmapFromStream(fstream, maxImageSize);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fstream != null) {
                try {
                    fstream.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public static Bitmap decodeSampledBitmapFromStream(InputStream stream, int maxImageSize) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDither = false; // Disable Dithering mode
        options.inPurgeable = true; // Tell to gc that whether it needs free
        // memory, the Bitmap can be cleared
        options.inInputShareable = true; // Which kind of reference will be used
        // to recover the Bitmap data after
        // being clear, when it will be used
        // in the future
        options.inTempStorage = new byte[32 * 1024];
        BitmapFactory.decodeStream(stream, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, maxImageSize);

        // If we're running on Honeycomb or newer, try to use inBitmap
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            addInBitmapOptions(options);
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(stream, null, options);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void addInBitmapOptions(BitmapFactory.Options options) {
        // inBitmap only works with mutable bitmaps so force the decoder to
        // return mutable bitmaps.
        options.inMutable = true;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).
            long totalPixels = halfHeight * halfWidth / inSampleSize;

            // Anything more than 2x the requested pixels we'll sample down
            // further
            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, int maxFileSize) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDither = false; // Disable Dithering mode
        options.inPurgeable = true; // Tell to gc that whether it needs free
        // memory, the Bitmap can be cleared
        options.inInputShareable = true; // Which kind of reference will be used
        // to recover the Bitmap data after
        // being clear, when it will be used
        // in the future
        options.inTempStorage = new byte[32 * 1024];
        options.inPreferredConfig = Config.ARGB_8888;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, maxFileSize);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        // If we're running on Honeycomb or newer, try to use inBitmap
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            addInBitmapOptions(options);
        }

        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int maxImageSize) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        inSampleSize = (int) Math.pow(2.0, (int) Math.round(Math.log(maxImageSize / (double) Math.max(height, width)) / Math.log(0.5)));

        return inSampleSize;
    }

    public static int getMaxSize(Context context) {
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int maxSize = Math.max((screenWidth - 75), ((int) (screenWidth * 0.7f)));
        return maxSize;
    }

    public static Bitmap getOrientatedScaledBitmap(File file, Context context) throws IOException {
        int maxImageSize = getMaxSize(context);
        Bitmap sourceBitmap = getScaledBitmap(file, maxImageSize);

        ExifInterface exif = new ExifInterface(file.getPath());
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
        }

        return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
    }
}