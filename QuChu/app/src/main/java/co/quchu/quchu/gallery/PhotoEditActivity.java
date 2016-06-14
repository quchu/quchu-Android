/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package co.quchu.quchu.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.gallery.model.PhotoInfo;
import co.quchu.quchu.gallery.utils.ImageUtils;
import co.quchu.quchu.gallery.utils.Utils;
import co.quchu.quchu.gallery.widget.FloatingActionButton;
import co.quchu.quchu.utils.DateUtils;
import co.quchu.quchu.utils.FileUtils;
import co.quchu.quchu.utils.LogUtils;

/**
 * Desction:图片裁剪
 * Author:pengjianbo
 * Date:15/10/10 下午5:40
 */
public class PhotoEditActivity extends BaseActivity implements View.OnClickListener {

    static final String CROP_PHOTO_ACTION = "crop_photo_action";
    static final String TAKE_PHOTO_ACTION = "take_photo_action";
    static final String EDIT_PHOTO_ACTION = "edit_photo_action";

    static final String SELECT_MAP = "select_map";
    private SimpleDraweeView mIvSourcePhoto;
    private TextView mTvEmptyView;
    private FloatingActionButton mFabCrop;

    private FunctionConfig mFunctionConfig;
    private HashMap<String, PhotoInfo> mSelectPhotoMap;

    private Uri path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gf_activity_photo_edit);
        mFunctionConfig = GalleryFinal.getFunctionConfig();
//        ActivityManager.getActivityManager().addActivity(this);


        mSelectPhotoMap = (HashMap<String, PhotoInfo>) getIntent().getSerializableExtra(SELECT_MAP);
        boolean mTakePhotoAction = getIntent().getBooleanExtra(TAKE_PHOTO_ACTION, false);
        if (mSelectPhotoMap == null) {
            mSelectPhotoMap = new HashMap<>();
        }

        findViews();
        setListener();

        if (mTakePhotoAction) {
            if (path == null) {
                if (mFunctionConfig.getSelectedPhoto() != null) {
                    for (PhotoInfo item : mFunctionConfig.getSelectedPhoto()) {
                        if (!item.getPhotoPath().startsWith("res:///")) {
                            if (!item.getPhotoPath().startsWith("http://") || item.getPhotoPath().startsWith("file://"))
                                mSelectPhotoMap.put(Uri.parse(item.getPhotoPath()).getPath(), item);
                            else
                                mSelectPhotoMap.put(item.getPhotoPath(), item);
                        }
                    }
                }

                File takePhotoFolder = GalleryFinal.getCoreConfig().getTakePhotoFolder();
                File toFile = new File(takePhotoFolder, "IMG" + DateUtils.getCurrentTime("yyyyMMddHHmmss") + ".jpg");
                path = Uri.fromFile(toFile);
                takePhotoAction(path);
            }
            //打开相机
        }


    }


    private Uri mTakePhotoUri;

    protected void takePhotoAction(Uri path) {
        if (!FileUtils.existSDCard()) {
            Toast.makeText(this, "没有SD卡不能拍照呢~", Toast.LENGTH_SHORT).show();
            return;
        }
        mTakePhotoUri = path;
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, path);
        LogUtils.e("拍照前:" + mTakePhotoUri);
        startActivityForResult(captureIntent, GalleryFinal.TAKE_REQUEST_CODE);
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


    private void findViews() {
        getEnhancedToolbar();


        mIvSourcePhoto = (SimpleDraweeView) findViewById(R.id.iv_source_photo);
        mTvEmptyView = (TextView) findViewById(R.id.tv_empty_view);
        mFabCrop = (FloatingActionButton) findViewById(R.id.fab_crop);
    }

    private void setListener() {
        mFabCrop.setOnClickListener(this);
    }

    protected void takeResult(PhotoInfo info) {

        mSelectPhotoMap.put(info.getPhotoPath(), info);
//        PhotoSelectActivity activity = (PhotoSelectActivity) ActivityManager.getActivityManager().getActivity(PhotoSelectActivity.class.getName());
//        if (activity != null) {
//            activity.takeRefreshGallery(info, true);
//        }
        loadImage(info);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GalleryFinal.TAKE_REQUEST_CODE) {
            if (resultCode == RESULT_OK && mTakePhotoUri != null) {


                String path = mTakePhotoUri.getPath();
                DisplayMetrics metrics = getResources().getDisplayMetrics();

                Bitmap bitmap = Utils.rotateBitmap(mTakePhotoUri.getPath(), ImageUtils.readPictureDegree(path), metrics.widthPixels, metrics.heightPixels);

                try {
                    ImageUtils.saveFile(bitmap, path);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, getString(R.string.take_photo_fail), Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                PhotoInfo info = new PhotoInfo();
                info.setPhotoId(Utils.getRandom(10000, 99999));
                info.setPhotoPath(path);
                updateGallery(path);
                takeResult(info);
            } else {
                Toast.makeText(this, getString(R.string.take_photo_fail), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * 更新相册
     */
    private void updateGallery(String filePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(filePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void loadImage(PhotoInfo photo) {
        mTvEmptyView.setVisibility(View.GONE);
        mIvSourcePhoto.setVisibility(View.VISIBLE);

        String path = "";
        if (photo != null) {
            path = photo.getPhotoPath();
        }
        ImageUtils.ShowImage(Uri.fromFile(new File(path)), mIvSourcePhoto, (int) AppContext.Width, (int) AppContext.Height);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fab_crop) {
            resultAction();
        } else if (id == R.id.iv_back) {
            finish();
        }
    }

    private void resultAction() {
        ArrayList<PhotoInfo> photoList = new ArrayList<>(mSelectPhotoMap.values());
        resultData(photoList);
    }

    protected void resultData(ArrayList<PhotoInfo> photoList) {
        GalleryFinal.OnHanlderResultCallback callback = GalleryFinal.getCallback();

        if (callback != null) {
            int requestCode = GalleryFinal.getRequestCode();
            if (photoList != null && photoList.size() > 0) {
                callback.onHanlderSuccess(requestCode, photoList);
            } else {
                callback.onHanlderFailure(requestCode, getString(R.string.photo_list_empty));
            }
        }
        finish();
    }


}
