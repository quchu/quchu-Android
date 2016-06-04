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

import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.finalteam.toolsfinal.ActivityManager;
import cn.finalteam.toolsfinal.FileUtils;
import cn.finalteam.toolsfinal.Logger;
import co.quchu.quchu.R;
import co.quchu.quchu.gallery.adapter.PhotoEditListAdapter;
import co.quchu.quchu.gallery.model.PhotoInfo;
import co.quchu.quchu.gallery.utils.ImageUtils;
import co.quchu.quchu.gallery.utils.RecycleViewBitmapUtils;
import co.quchu.quchu.gallery.utils.Utils;
import co.quchu.quchu.gallery.widget.FloatingActionButton;
import co.quchu.quchu.gallery.widget.HorizontalListView;
import co.quchu.quchu.gallery.widget.crop.CropImageActivity;
import co.quchu.quchu.gallery.widget.crop.CropImageView;

/**
 * Desction:图片裁剪
 * Author:pengjianbo
 * Date:15/10/10 下午5:40
 */
public class PhotoEditActivity extends CropImageActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    static final String CROP_PHOTO_ACTION = "crop_photo_action";
    static final String TAKE_PHOTO_ACTION = "take_photo_action";
    static final String EDIT_PHOTO_ACTION = "edit_photo_action";

    static final String SELECT_MAP = "select_map";
    private final int CROP_SUC = 1;//裁剪成功
    private final int CROP_FAIL = 2;//裁剪失败
    private final int UPDATE_PATH = 3;//更新path

    private ImageView mIvBack;
    private TextView mTvTitle;
    private CropImageView mIvCropPhoto;
    private SimpleDraweeView mIvSourcePhoto;
    private TextView mTvEmptyView;
    private FloatingActionButton mFabCrop;
    private HorizontalListView mLvGallery;
    private LinearLayout mLlGallery;
    private ArrayList<PhotoInfo> mPhotoList;
    private PhotoEditListAdapter mPhotoEditListAdapter;
    private int mSelectIndex = 0;
    private boolean mCropState;

    private FunctionConfig mFunctionConfig;
    private HashMap<String, PhotoInfo> mSelectPhotoMap;
    private Map<Integer, PhotoTempModel> mPhotoTempMap;
    private File mEditPhotoCacheFile;

    private boolean mCropPhotoAction;//裁剪图片动作

    private android.os.Handler mHanlder = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == CROP_SUC) {
                String path = (String) msg.obj;
                PhotoInfo photoInfo = mPhotoList.get(mSelectIndex);
                try {
                    for (Map.Entry<Integer, PhotoTempModel> entry : mPhotoTempMap.entrySet()) {
                        if (entry.getKey() == photoInfo.getPhotoId()) {
                            PhotoTempModel tempModel = entry.getValue();
                            tempModel.setSourcePath(path);
                            tempModel.setOrientation(0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                toast(getString(R.string.crop_suc));

                Message message = mHanlder.obtainMessage();
                message.what = UPDATE_PATH;
                message.obj = path;
                mHanlder.sendMessage(message);

            } else if (msg.what == CROP_FAIL) {
                toast(getString(R.string.crop_fail));
            } else if (msg.what == UPDATE_PATH) {
                if (mPhotoList.get(mSelectIndex) != null) {
                    PhotoInfo photoInfo = mPhotoList.get(mSelectIndex);
                    String path = (String) msg.obj;
                    //photoInfo.setThumbPath(path);
                    try {
                        for (Map.Entry<String, PhotoInfo> entry : mSelectPhotoMap.entrySet()) {
                            if (entry.getValue() != null && entry.getValue().getPhotoId() == photoInfo.getPhotoId()) {
                                PhotoInfo pi = entry.getValue();
                                pi.setPhotoPath(path);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    photoInfo.setPhotoPath(path);

                    loadImage(photoInfo);
                    mPhotoEditListAdapter.notifyDataSetChanged();
                }

                if (mFunctionConfig.isForceCrop() && !mFunctionConfig.isForceCropEdit()) {
                    resultAction();
                }
            }
            corpPageState(false);
            mCropState = false;
            mTvTitle.setText(R.string.photo_edit);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeConfig mThemeConfig = GalleryFinal.getGalleryTheme();
        mFunctionConfig = GalleryFinal.getFunctionConfig();
        if (mFunctionConfig == null || mThemeConfig == null) {
            resultFailure(getString(R.string.please_reopen_gf), true);
        } else {
            setContentView(R.layout.gf_activity_photo_edit);

            mSelectPhotoMap = (HashMap<String, PhotoInfo>) getIntent().getSerializableExtra(SELECT_MAP);
            boolean mTakePhotoAction = this.getIntent().getBooleanExtra(TAKE_PHOTO_ACTION, false);
            mCropPhotoAction = this.getIntent().getBooleanExtra(CROP_PHOTO_ACTION, false);

            if (mSelectPhotoMap == null) {
                mSelectPhotoMap = new HashMap<>();
            }
            if (mFunctionConfig.getSelectedPhoto() != null) {
                for (PhotoInfo item : mFunctionConfig.getSelectedPhoto()) {
                    if (!item.getPhotoPath().startsWith("res:///")) {
                        mSelectPhotoMap.put(Uri.parse(item.getPhotoPath()).getPath(), item);
                    }
                }
            }
            mPhotoTempMap = new HashMap<>();
            mPhotoList = new ArrayList<>(mSelectPhotoMap.values());

            mEditPhotoCacheFile = GalleryFinal.getCoreConfig().getEditPhotoCacheFolder();

            if (mPhotoList == null) {
                mPhotoList = new ArrayList<>();
            }

            for (PhotoInfo info : mPhotoList) {
                mPhotoTempMap.put(info.getPhotoId(), new PhotoTempModel(info.getPhotoPath()));
            }

            findViews();
            setListener();
            //     setTheme();

            mPhotoEditListAdapter = new PhotoEditListAdapter(this, mPhotoList, mScreenWidth);
            mLvGallery.setAdapter(mPhotoEditListAdapter);

            try {
                File nomediaFile = new File(mEditPhotoCacheFile, ".nomedia");
                if (!nomediaFile.exists()) {
                    nomediaFile.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            initCrop(mIvCropPhoto, mFunctionConfig.isCropSquare(), mFunctionConfig.getCropWidth(), mFunctionConfig.getCropHeight());
            if (mPhotoList.size() > 0 && !mTakePhotoAction) {
                loadImage(mPhotoList.get(0));
            }

            if (mTakePhotoAction) {
                //打开相机
                takePhotoAction();
            }

        }
    }


    private void findViews() {
        mIvCropPhoto = (CropImageView) findViewById(R.id.iv_crop_photo);
        mIvSourcePhoto = (SimpleDraweeView) findViewById(R.id.iv_source_photo);
        mLvGallery = (HorizontalListView) findViewById(R.id.lv_gallery);
        mLlGallery = (LinearLayout) findViewById(R.id.ll_gallery);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvEmptyView = (TextView) findViewById(R.id.tv_empty_view);
        mFabCrop = (FloatingActionButton) findViewById(R.id.fab_crop);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
    }

    private void setListener() {
        mIvBack.setOnClickListener(this);
        mLvGallery.setOnItemClickListener(this);
        mFabCrop.setOnClickListener(this);
    }

    @Override
    protected void takeResult(PhotoInfo info) {
//        if (!mFunctionConfig.isMutiSelect()) {
//            mPhotoList.clear();
//            mSelectPhotoMap.clear();
//        }
        mPhotoList.add(info);

        mSelectPhotoMap.put(info.getPhotoPath(), info);
        mPhotoTempMap.put(info.getPhotoId(), new PhotoTempModel(info.getPhotoPath()));
        mPhotoEditListAdapter.notifyDataSetChanged();

        PhotoSelectActivity activity = (PhotoSelectActivity) ActivityManager.getActivityManager().getActivity(PhotoSelectActivity.class.getName());
        if (activity != null) {
            activity.takeRefreshGallery(info, true);
        }
        loadImage(info);

    }

    private void loadImage(PhotoInfo photo) {
        mTvEmptyView.setVisibility(View.GONE);
        mIvSourcePhoto.setVisibility(View.VISIBLE);
        mIvCropPhoto.setVisibility(View.GONE);

        String path = "";
        if (photo != null) {
            path = photo.getPhotoPath();
        }
        if (mFunctionConfig.isCrop()) {
            setSourceUri(Uri.fromFile(new File(path)));
        }
        // TODO: 2016/3/25
//        GalleryFinal.getCoreConfig().getImageLoader().displayImage(this, path,
// mIvSourcePhoto, mDefaultDrawable, mScreenWidth, mScreenHeight);
//        mIvSourcePhoto.setImageURI(Uri.fromFile(new File(path)));
        ImageUtils.ShowImage(Uri.fromFile(new File(path)), mIvSourcePhoto, mScreenWidth, mScreenHeight);
    }

    public void deleteIndex(int position, PhotoInfo dPhoto) {
        if (dPhoto != null) {
            PhotoSelectActivity activity = (PhotoSelectActivity) ActivityManager.getActivityManager().getActivity(PhotoSelectActivity.class.getName());
            if (activity != null) {
                activity.deleteSelect(dPhoto.getPhotoId());
            }

            try {
                Iterator<Map.Entry<String, PhotoInfo>> entries = mSelectPhotoMap.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String, PhotoInfo> entry = entries.next();
                    if (entry.getValue() != null && entry.getValue().getPhotoId() == dPhoto.getPhotoId()) {
                        entries.remove();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mPhotoList.size() == 0) {
            mSelectIndex = 0;
            mTvEmptyView.setText(R.string.no_photo);
            mTvEmptyView.setVisibility(View.VISIBLE);
            mIvSourcePhoto.setVisibility(View.GONE);
            mIvCropPhoto.setVisibility(View.GONE);
        } else {
            if (position == 0) {
                mSelectIndex = 0;
            } else if (position == mPhotoList.size()) {
                mSelectIndex = position - 1;
            } else {
                mSelectIndex = position;
            }

            PhotoInfo photoInfo = mPhotoList.get(mSelectIndex);
            loadImage(photoInfo);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mSelectIndex = i;
        PhotoInfo photoInfo = mPhotoList.get(i);
        loadImage(photoInfo);
    }

    @Override
    public void setCropSaveSuccess(final File file) {
        Message message = mHanlder.obtainMessage();
        message.what = CROP_SUC;
        message.obj = file.getAbsolutePath();
        mHanlder.sendMessage(message);
    }

    @Override
    public void setCropSaveException(Throwable throwable) {
        mHanlder.sendEmptyMessage(CROP_FAIL);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fab_crop) {
            if (mPhotoList.size() == 0) {
                return;
            }
            if (mCropState) {
                System.gc();
                PhotoInfo photoInfo = mPhotoList.get(mSelectIndex);
                try {
                    String ext = FileUtils.getFileExtension(photoInfo.getPhotoPath());
                    File toFile;
                    if (mFunctionConfig.isCropReplaceSource()) {
                        toFile = new File(photoInfo.getPhotoPath());
                    } else {
                        toFile = new File(mEditPhotoCacheFile, Utils.getFileName(photoInfo.getPhotoPath()) + "_crop." + ext);
                    }

                    FileUtils.makeFolders(toFile);
                    onSaveClicked(toFile);//保存裁剪
                } catch (Exception e) {
                    Logger.e(e);
                }
            } else { //完成选择
                resultAction();
            }
        } else if (id == R.id.iv_back) {
            if (mCropState && !(mCropPhotoAction && !mFunctionConfig.isRotate() && !mFunctionConfig.isCamera())) {
                if ((mFunctionConfig.isForceCrop() && mFunctionConfig.isForceCropEdit())) {
                    return;
                }
            }
            finish();
        }
    }

    private void resultAction() {
        ArrayList<PhotoInfo> photoList = new ArrayList<>(mSelectPhotoMap.values());
        resultData(photoList);
    }


    private void corpPageState(boolean crop) {
        if (crop) {
            mIvSourcePhoto.setVisibility(View.GONE);
            mIvCropPhoto.setVisibility(View.VISIBLE);
            mLlGallery.setVisibility(View.GONE);

        } else {
            mIvSourcePhoto.setVisibility(View.VISIBLE);
            mIvCropPhoto.setVisibility(View.GONE);
            if (mFunctionConfig.isMutiSelect()) {
                mLlGallery.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RecycleViewBitmapUtils.recycleImageView(mIvCropPhoto);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mCropState && !(mCropPhotoAction && !mFunctionConfig.isRotate() && !mFunctionConfig.isCamera())) {
                if ((mFunctionConfig.isForceCrop() && mFunctionConfig.isForceCropEdit())) {
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class PhotoTempModel {

        public PhotoTempModel(String path) {
            sourcePath = path;

        }

        private int orientation;
        private String sourcePath;

        public int getOrientation() {
            return orientation;
        }

        public void setOrientation(int orientation) {
            this.orientation = orientation;
        }


        public void setSourcePath(String sourcePath) {
            this.sourcePath = sourcePath;
        }
    }
}
