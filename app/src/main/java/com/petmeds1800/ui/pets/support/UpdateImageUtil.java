package com.petmeds1800.ui.pets.support;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.dialog.NoTitleOkDialogFragment;
import com.petmeds1800.util.BitmapUtils;
import com.petmeds1800.util.DocumentUtils;
import com.petmeds1800.util.StorageHelper;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by pooja on 3/9/2016.
 */
public class UpdateImageUtil {

    private Uri fileUri;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int GALLERY_CAPTURE_IMAGE_REQUEST_CODE = 101;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "PetMeds";
    private Object mParent;

    /**
     * @param parent parent should be an activity or a fragment
     * @return instance of fragment
     */
    public static UpdateImageUtil getInstance(Object parent) {
        UpdateImageUtil obj = new UpdateImageUtil();
        obj.mParent = parent;
        return obj;
    }

    private Context getContext() {
        if (mParent instanceof Activity) {
            return ((Context) mParent);
        } else {
            return ((Context) ((Fragment) mParent).getActivity());
        }
    }

    public void updateProfilePic(int what) {

        if (!isDeviceSupportCamera()) {
            Toast.makeText(getContext().getApplicationContext(),
                    "Sorry!! Your device doesn't support Camera",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (StorageHelper.isExternalStorageAvailableAndWriteable()) {
            if (what == CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
                captureImage();
            else if (what == GALLERY_CAPTURE_IMAGE_REQUEST_CODE)
                selectImage();
        } else {
            Toast.makeText(getContext(), "Sorry!! Your device doesn't support SD Card", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isDeviceSupportCamera() {
        // this device has a camera
        // no camera on this device
        return getContext().getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    private void captureImage() {


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissionList = new ArrayList<String>();
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(android.Manifest.permission.CAMERA);
            }
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (!permissionList.isEmpty()) {
                final String requestPermission[] = (String[]) permissionList.toArray(new String[permissionList.size()]);
                ((AbstractFragment) mParent).checkRequiredPermission(requestPermission, new AbstractFragment.PermissionRequested() {
                    @Override
                    public void onPermissionGranted() {


                        openCamera();
                    }

                    @Override
                    public void onPermissionDenied(HashMap<String, Boolean> deniedPermissions) {
                        NoTitleOkDialogFragment permissionDialogue = null;
                        for (HashMap.Entry<String, Boolean> entry : deniedPermissions.entrySet()) {
                            if (entry.getValue()) {//check if true means user has revoked permission permanently
                                switch (entry.getKey()) {
                                    case android.Manifest.permission.CAMERA:
                                        permissionDialogue =  NoTitleOkDialogFragment.newInstance("To re-enable, please go to Settings>Permissions and enable Camera Service for this app.");
                                        break;

                                    case android.Manifest.permission.READ_EXTERNAL_STORAGE:
                                        permissionDialogue =  NoTitleOkDialogFragment.newInstance("To re-enable, please go to Settings>Permissions and enable Storage Service for this app.");
                                        break;
                                }
                            } else {
                                //Toast.makeText(getActivity(), "Permission Denied :" + entry.getKey(), Toast.LENGTH_LONG).show();
                            }
                        }
                        if (permissionDialogue != null) {
                            permissionDialogue.show(((AbstractFragment) mParent).getActivity().getSupportFragmentManager());
                        }

                    }
                });
            } else {
                openCamera();
            }
        } else {//in case device does not support marshmallow


            openCamera();
        }

    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        // start the image capture Intent
        if (mParent instanceof Activity) {
            ((Activity) mParent).startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } else {
            ((Fragment) mParent).startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    private void selectImage() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                final String requestPermission[] = {Manifest.permission.READ_EXTERNAL_STORAGE};
                ((AbstractFragment) mParent).checkRequiredPermission(requestPermission, new AbstractFragment.PermissionRequested() {
                    @Override
                    public void onPermissionGranted() {


                        getImageFromGallery();
                    }

                    @Override
                    public void onPermissionDenied(HashMap<String, Boolean> deniedPermissions) {
                        NoTitleOkDialogFragment permissionDialogue = null;
                        for (HashMap.Entry<String, Boolean> entry : deniedPermissions.entrySet()) {
                            if (entry.getValue()) {//check if true means user has revoked permission permanently
                                switch (entry.getKey()) {
                                    case android.Manifest.permission.READ_EXTERNAL_STORAGE:
                                        permissionDialogue =  NoTitleOkDialogFragment.newInstance("To re-enable, please go to Settings>Permissions and enable Storage Service for this app.");
                                        break;
                                }
                            } else {
                                //Toast.makeText(getActivity(), "Permission Denied :" + entry.getKey(), Toast.LENGTH_LONG).show();
                            }
                        }
                        if (permissionDialogue != null) {
                            permissionDialogue.show(((AbstractFragment) mParent).getActivity().getSupportFragmentManager());
                        }

                    }
                });
            } else {
                getImageFromGallery();
            }
        } else {//in case device does not support marshmallow


            getImageFromGallery();
        }


    }

    private void getImageFromGallery() {
        if (StorageHelper.isExternalStorageAvailableAndWriteable()) {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (mParent instanceof Activity) {
                ((Activity) mParent).startActivityForResult(i, GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
            } else {
                ((Fragment) mParent).startActivityForResult(i, GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
            }
        } else {
            Toast.makeText(getContext(), "Sorry!! Your device doesn't support SD Card", Toast.LENGTH_LONG).show();
        }
    }

    protected File resizePic(Uri fileUri) {
        File file, mReturnFile = null;
        Bitmap b = null;
//        float density;
//        density = getResources().getDisplayMetrics().density;
//        int THUMBSIZE = (int) (200 * density);
        String filePath = DocumentUtils.getPath(getContext().getApplicationContext(), fileUri);
        Log.e("File", "filePath: " + filePath);
        try {
            file = new File(new URI("file://" + filePath.replace(" ", "%20")));
            Log.d("Original Size:", "" + file.length());
            b = BitmapUtils.getOrientatedScaledBitmap(file, getContext().getApplicationContext());
//            b = ThumbnailUtils.extractThumbnail(BitmapUtils.getOrientatedScaledBitmap(file, getActivity().getApplicationContext()), THUMBSIZE, THUMBSIZE);
            // create a file to write bitmap data
            mReturnFile = new File(getContext().getApplicationContext().getCacheDir().getAbsolutePath(), "ScaledImage.jpeg");
            // Convert bitmap to byte array
            FileOutputStream fout;
            fout = new FileOutputStream(mReturnFile);
            b.compress(Bitmap.CompressFormat.JPEG, 75, fout);
            fout.close();
            fout.flush();
            b.recycle();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("Compressed Size:", "" + mReturnFile.length());
        return mReturnFile;
    }

    protected void performCrop(Uri picUri) {
        Uri destination = Uri.fromFile(new File(getContext().getApplicationContext().getCacheDir(), "cropped.jpg"));
        if (mParent instanceof Activity) {
            Crop.of(picUri, destination).asSquare().start(((Activity) mParent), Crop.REQUEST_CROP);
        } else {
            Crop.of(picUri, destination).asSquare().start(getContext(), ((Fragment) mParent), Crop.REQUEST_CROP);
        }
    }


    /**
     * ----------------------------- Helper Methods for Image capturing  ------------------------------------------------- *
     */


    /**
     * Creating file uri to store image/video
     */
    protected Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    protected File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    protected void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            Uri finalUri = Crop.getOutput(result);
            if (finalUri != null) {
                // EventBus.getDefault().postSticky(new UpdateImageSelectedEvent(UpdateImageSelectedEvent.IMAGE_SELECTED, finalUri));
                /*Glide.with(this).load(finalUri.toString()).asBitmap().centerCrop().into(new BitmapImageViewTarget(mPetImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        mPetImage.setImageDrawable(circularBitmapDrawable);
                    }
                });*/
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getContext(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        File mUri;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                    mUri = resizePic(fileUri);
                    performCrop(Uri.fromFile(mUri));
                    break;
                case GALLERY_CAPTURE_IMAGE_REQUEST_CODE:
                    mUri = resizePic(data.getData());
                    performCrop(Uri.fromFile(mUri));

                    break;
                default:
                    break;
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
        }
    }

}
