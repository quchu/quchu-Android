///*
// * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// */
//
//package co.quchu.quchu.gallery;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.provider.MediaStore;
//import android.util.DisplayMetrics;
//import android.view.Window;
//import android.widget.Toast;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.finalteam.toolsfinal.ActivityManager;
//import cn.finalteam.toolsfinal.DeviceUtils;
//import co.quchu.quchu.R;
//import co.quchu.quchu.base.BaseActivity;
//import co.quchu.quchu.gallery.model.PhotoInfo;
//import co.quchu.quchu.gallery.utils.ImageUtils;
//import co.quchu.quchu.gallery.utils.MediaScanner;
//import co.quchu.quchu.gallery.utils.Utils;
//import co.quchu.quchu.utils.LogUtils;
//import pub.devrel.easypermissions.EasyPermissions;
//
///**
// * Desction:
// * Author:pengjianbo
// * Date:15/10/10 下午5:46
// */
//public abstract class PhotoBaseActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
//
//    @Override
//    protected int activitySetup() {
//        return TRANSITION_TYPE_LEFT;
//    }
//
//    protected static String mPhotoTargetFolder;
//
//    private Uri mTakePhotoUri;
//    private MediaScanner mMediaScanner;
//
//    protected int mScreenWidth = 720;
//    protected int mScreenHeight = 1280;
//
//    protected Handler mFinishHanlder = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            finishGalleryFinalPage();
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        super.onCreate(savedInstanceState);
//        ActivityManager.getActivityManager().addActivity(this);
//        mMediaScanner = new MediaScanner(this);
//        DisplayMetrics dm = DeviceUtils.getScreenPix(this);
//        mScreenWidth = dm.widthPixels;
//        mScreenHeight = dm.heightPixels;
//    }
//
//    @Override
//    protected void onDestroy() {
//        System.gc();
//        super.onDestroy();
//        mMediaScanner.unScanFile();
//        ActivityManager.getActivityManager().finishActivity(this);
//    }
//
//    public void toast(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//    }
//
//    /**
//     * 拍照
//     */
//    protected void takePhotoAction(Uri path) {
//        if (!DeviceUtils.existSDCard()) {
//            toast("没有SD卡不能拍照呢~");
//            return;
//        }
//        mTakePhotoUri = path;
////        File takePhotoFolder = null;
////        if (StringUtils.isEmpty(mPhotoTargetFolder)) {
////            takePhotoFolder = GalleryFinal.getCoreConfig().getTakePhotoFolder();
////        } else {
////            takePhotoFolder = new File(mPhotoTargetFolder);
////        }
////
////        File toFile = new File(takePhotoFolder, "IMG" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".jpg");
////        boolean suc = FileUtils.makeFolders(toFile);
////        Logger.d("create folder=" + toFile.getAbsolutePath());
////        if (suc) {
////        mTakePhotoUri = Uri.fromFile(toFile);
//        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, path);
//        LogUtils.e("拍照前:" + mTakePhotoUri);
//        startActivityForResult(captureIntent, GalleryFinal.TAKE_REQUEST_CODE);
////        } else {
////            Logger.e("create file failure");
////        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == GalleryFinal.TAKE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK && mTakePhotoUri != null) {
//
//
//                String path = mTakePhotoUri.getPath();
//                DisplayMetrics metrics = getResources().getDisplayMetrics();
//
//                Bitmap bitmap = Utils.rotateBitmap(mTakePhotoUri.getPath(), ImageUtils.readPictureDegree(path), metrics.widthPixels, metrics.heightPixels);
//
//                try {
//                    ImageUtils.saveFile(bitmap, path);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    toast(getString(R.string.take_photo_fail));
//                    finishGalleryFinalPage();
//                    return;
//                }
//
//                PhotoInfo info = new PhotoInfo();
//                info.setPhotoId(Utils.getRandom(10000, 99999));
//                info.setPhotoPath(path);
//                updateGallery(path);
//                takeResult(info);
//            } else {
//                toast(getString(R.string.take_photo_fail));
//                finish();
//            }
//        }
//    }
//
//    /**
//     * 更新相册
//     */
//    private void updateGallery(String filePath) {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(filePath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//
////        mMediaScanner.scanFile(filePath, "image/jpeg");
//    }
//
//    protected void resultData(ArrayList<PhotoInfo> photoList) {
//        GalleryFinal.OnHanlderResultCallback callback = GalleryFinal.getCallback();
//        int requestCode = GalleryFinal.getRequestCode();
//        if (callback != null) {
//            if (photoList != null && photoList.size() > 0) {
//                callback.onHanlderSuccess(requestCode, photoList);
//            } else {
//                callback.onHanlderFailure(requestCode, getString(R.string.photo_list_empty));
//            }
//        }
//        finishGalleryFinalPage();
//    }
//
//    protected void resultFailure(String errormsg, boolean delayFinish) {
//        GalleryFinal.OnHanlderResultCallback callback = GalleryFinal.getCallback();
//        int requestCode = GalleryFinal.getRequestCode();
//        if (callback != null) {
//            callback.onHanlderFailure(requestCode, errormsg);
//        }
//        if (delayFinish) {
//            mFinishHanlder.sendEmptyMessageDelayed(0, 500);
//        } else {
//            finishGalleryFinalPage();
//        }
//    }
//
//    private void finishGalleryFinalPage() {
//        ActivityManager.getActivityManager().finishActivity(PhotoEditActivity.class);
//        ActivityManager.getActivityManager().finishActivity(PhotoSelectActivity.class);
//    }
//
//    protected abstract void takeResult(PhotoInfo info);
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        // Forward results to EasyPermissions
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//    }
//
//    @Override
//    public void onPermissionsGranted(List<String> list) {
//    }
//
//    @Override
//    public void onPermissionsDenied(List<String> list) {
//    }
//}
