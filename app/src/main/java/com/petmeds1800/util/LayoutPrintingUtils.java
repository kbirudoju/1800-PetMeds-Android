package com.petmeds1800.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ScrollView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Digvijay on 9/30/2016.
 */
public class LayoutPrintingUtils {

    private final static String saveDirectoryName = "petmeds";

    private Bitmap printViewToBitmap(ScrollView rootView, Context context, String bitmapFileName)
            throws FileNotFoundException, DocumentException {

        Bitmap bitmap = getBitmapFromView(rootView);

        //Save bitmap
        File jpgDir = new File(Environment.getExternalStorageDirectory(), saveDirectoryName);
        if (!jpgDir.exists()) {
            if (jpgDir.mkdir()) {
                File myPath = new File(jpgDir, bitmapFileName + ".jpg");
                try {
                    FileOutputStream fos = new FileOutputStream(myPath);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Screen", "screen");
                    return bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Bitmap getBitmapFromView(ScrollView rootView) {

        int totalHeight = rootView.getChildAt(0).getHeight();
        int totalWidth = rootView.getChildAt(0).getWidth();

        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = rootView.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        rootView.draw(canvas);
        return returnedBitmap;
    }

    public File printViewToPdf(String pdfFileName, Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.setAbsolutePosition(0, 0);
            Document document = new Document(image);
            //Create a directory for your PDF
            final File pdfDir = new File(Environment.getExternalStorageDirectory(), saveDirectoryName);
            if (!pdfDir.exists()) {
                if (!pdfDir.mkdir()) {
                    return null;
                }
            }
            File pdfFile = new File(pdfDir, pdfFileName + ".pdf");
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            document.add(image);
            document.close();
            return pdfDir;
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public File createTempPdfFileFromBitmap(Context context, Bitmap bitmap, String pdfFileName){
        File outputDir = context.getCacheDir(); // context being the Activity
        File outputFile = null;
        try {
            outputFile = File.createTempFile(pdfFileName, ".pdf", outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFile;
    }
}
