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
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Digvijay on 9/30/2016.
 */
public class LayoutPrintingUtils {

    private Bitmap returnedBitmap;

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

    public Bitmap getBitmapFromView(ViewGroup rootView) {

//        rootView.getChildAt(0).measure(View.MeasureSpec.makeMeasureSpec(rootView.getChildAt(0).getWidth(), View.MeasureSpec.EXACTLY),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

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

    public Bitmap getBitmapFromView(Context context) {

//        recyclerView.measure(View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        List<TextView> viewList = new ArrayList<>();
        TextView tx1 = new TextView(context);
        tx1.setText("aaaaa");
        viewList.add(tx1);
        TextView tx2 = new TextView(context);
        tx2.setText("bbbbb");
        viewList.add(tx2);

        for (int i = 0; i < viewList.size(); i++) {
            linearLayout.addView(viewList.get(i));
        }


//        linearLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        if (linearLayout.post(new Runnable() {
            @Override
            public void run() {
                int totalHeight, totalWidth;
                totalHeight = linearLayout.getHeight();
                totalWidth = linearLayout.getWidth();
                returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(returnedBitmap);
                Drawable bgDrawable = linearLayout.getBackground();
                if (bgDrawable != null) {
                    bgDrawable.draw(canvas);
                } else {
                    canvas.drawColor(Color.WHITE);
                }
                linearLayout.draw(canvas);
            }
        })) {
            return returnedBitmap;
        }
        return null;

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

    public File createTempPdfFileFromBitmap(Context context, Bitmap bitmap, String pdfFileName) {
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
