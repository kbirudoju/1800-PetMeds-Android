package com.petmeds1800.util;

import android.os.Environment;
import com.petmeds1800.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by laetitia on 1/5/16.
 */
public class FileUtils {

    private static final String TEMPLATE_FOLDER = "GG_Template";
    private static final String FILE_EXTENSION = ".txt";

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public File getDirectory() throws IOException {
        final File storageDir = new File(Environment.getExternalStorageDirectory(), TEMPLATE_FOLDER);
        if (!storageDir.exists() && !storageDir.mkdirs()) {
            throw new IOException("Fail to create the " + TEMPLATE_FOLDER + " folder.");
        }
        return storageDir;
    }

    public boolean saveFile(String name, String content) {
        if (isExternalStorageWritable()) {
            try {
                File file = new File(getDirectory(), name + FILE_EXTENSION);
                FileOutputStream os = new FileOutputStream(file);
                os.write(content.getBytes());
                os.close();
            } catch (IOException e) {
                Log.e(FileUtils.class.getSimpleName(), e.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }
}
