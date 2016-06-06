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

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.finalteam.toolsfinal.FileUtils;
import co.quchu.quchu.R;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.gallery.adapter.PhotoListAdapter;
import co.quchu.quchu.gallery.model.PhotoFolderInfo;
import co.quchu.quchu.gallery.model.PhotoInfo;
import co.quchu.quchu.gallery.utils.PhotoTools;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Desction:图片选择器
 * Author:pengjianbo
 * Date:15/10/10 下午3:54
 */
public class PhotoSelectActivity extends PhotoBaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private final int HANLDER_TAKE_PHOTO_EVENT = 1000;
    private final int HANDLER_REFRESH_LIST_EVENT = 1002;
    EnhancedToolbar toolbar;

    private GridView mGvPhotoList;
    private TextView mTvChooseCount;
    private TextView mTvEmptyView;

    private List<PhotoInfo> mCurPhotoList;
    private PhotoListAdapter mPhotoListAdapter;

    private FunctionConfig mFunctionConfig;
    private ThemeConfig mThemeConfig;

    //是否需要刷新相册
    private boolean mHasRefreshGallery = false;
    private HashMap<String, PhotoInfo> mSelectPhotoMap = new HashMap<>();

    private Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HANLDER_TAKE_PHOTO_EVENT) {
                PhotoInfo photoInfo = (PhotoInfo) msg.obj;
                takeRefreshGallery(photoInfo);
                refreshSelectCount();
            } else if (msg.what == HANDLER_REFRESH_LIST_EVENT) {
                refreshSelectCount();
                mPhotoListAdapter.notifyDataSetChanged();
                mGvPhotoList.setEnabled(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFunctionConfig = GalleryFinal.getFunctionConfig();
        mThemeConfig = GalleryFinal.getGalleryTheme();

        if (mFunctionConfig == null || mThemeConfig == null) {
            resultFailure(getString(R.string.please_reopen_gf), true);
        } else {
            setContentView(R.layout.gf_activity_photo_select);
            toolbar = getEnhancedToolbar();
            findViews();
            setListener();

            if (mFunctionConfig.getSelectedPhoto() != null) {
                for (PhotoInfo item : mFunctionConfig.getSelectedPhoto()) {
                    if (!item.getPhotoPath().startsWith("res:///")) {
                        if (!item.getPhotoPath().startsWith("http://"))
                            mSelectPhotoMap.put(Uri.parse(item.getPhotoPath()).getPath(), item);
                        else
                            mSelectPhotoMap.put(item.getPhotoPath(), item);
                    }
                }
            }


            mCurPhotoList = new ArrayList<>();

            mPhotoListAdapter = new PhotoListAdapter(this, mCurPhotoList, mSelectPhotoMap, mScreenWidth);
            mGvPhotoList.setAdapter(mPhotoListAdapter);


            mGvPhotoList.setEmptyView(mTvEmptyView);
            refreshSelectCount();
            requestGalleryPermission();

            mGvPhotoList.setOnScrollListener(GalleryFinal.getCoreConfig().getPauseOnScrollListener());
        }

    }


    private void findViews() {

        mGvPhotoList = (GridView) findViewById(R.id.gv_photo_list);
        mTvChooseCount = toolbar.getTitleTv();
        mTvEmptyView = (TextView) findViewById(R.id.tv_empty_view);
        TextView rightTv = toolbar.getRightTv();
        rightTv.setText("确定");
        rightTv.setOnClickListener(this);


    }

    private void setListener() {

        mGvPhotoList.setOnItemClickListener(this);
    }

    protected void deleteSelect(int photoId) {
        try {
            Iterator<Map.Entry<String, PhotoInfo>> entries = mSelectPhotoMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, PhotoInfo> entry = entries.next();
                if (entry.getValue() != null && entry.getValue().getPhotoId() == photoId) {
                    entries.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        refreshAdapter();
    }

    private void refreshAdapter() {
        mHanlder.sendEmptyMessageDelayed(HANDLER_REFRESH_LIST_EVENT, 100);
    }

    protected void takeRefreshGallery(PhotoInfo photoInfo, boolean selected) {
        if (isFinishing() || photoInfo == null) {
            return;
        }

        Message message = mHanlder.obtainMessage();
        message.obj = photoInfo;
        message.what = HANLDER_TAKE_PHOTO_EVENT;
        mSelectPhotoMap.put(photoInfo.getPhotoPath(), photoInfo);
        mHanlder.sendMessageDelayed(message, 100);
    }

    /**
     * 解决在5.0手机上刷新Gallery问题，从startActivityForResult回到Activity把数据添加到集合中然后理解跳转到下一个页面，
     * adapter的getCount与list.size不一致，所以我这里用了延迟刷新数据
     *
     * @param photoInfo
     */
    private void takeRefreshGallery(PhotoInfo photoInfo) {
        mCurPhotoList.add(0, photoInfo);
        mPhotoListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void takeResult(PhotoInfo photoInfo) {

        Message message = mHanlder.obtainMessage();
        message.obj = photoInfo;
        message.what = HANLDER_TAKE_PHOTO_EVENT;

        if (!mFunctionConfig.isMutiSelect()) { //单选
            mSelectPhotoMap.clear();
            mSelectPhotoMap.put(photoInfo.getPhotoPath(), photoInfo);

            if (mFunctionConfig.isEditPhoto()) {//裁剪
                mHasRefreshGallery = true;
                toPhotoEdit();
            } else {
                ArrayList<PhotoInfo> list = new ArrayList<>();
                list.add(photoInfo);
                resultData(list);
            }

            mHanlder.sendMessageDelayed(message, 100);
        } else {//多选
            mSelectPhotoMap.put(photoInfo.getPhotoPath(), photoInfo);
            mHanlder.sendMessageDelayed(message, 100);
        }
    }

    /**
     * 执行裁剪
     */
    protected void toPhotoEdit() {
        Intent intent = new Intent(this, PhotoEditActivity.class);
        intent.putExtra(PhotoEditActivity.SELECT_MAP, mSelectPhotoMap);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.toolbar_tv_right) {
            if (mSelectPhotoMap.size() > 0) {
                ArrayList<PhotoInfo> photoList = new ArrayList<>(mSelectPhotoMap.values());
                if (!mFunctionConfig.isEditPhoto()) {
                    resultData(photoList);
                } else {
                    toPhotoEdit();
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        photoItemClick(view, position);
    }

    private void photoItemClick(View view, int position) {
        PhotoInfo info = mCurPhotoList.get(position);
        if (!mFunctionConfig.isMutiSelect()) {
            mSelectPhotoMap.clear();
            mSelectPhotoMap.put(info.getPhotoPath(), info);
            String ext = FileUtils.getFileExtension(info.getPhotoPath());
            if (mFunctionConfig.isEditPhoto() && (ext.equalsIgnoreCase("png")
                    || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg"))) {
                toPhotoEdit();
            } else {
                ArrayList<PhotoInfo> list = new ArrayList<>();
                list.add(info);
                resultData(list);
            }
            return;
        }
        boolean checked;
        if (mSelectPhotoMap.get(info.getPhotoPath()) == null) {
            if (mFunctionConfig.isMutiSelect() && mSelectPhotoMap.size() == mFunctionConfig.getMaxSize()) {
                toast("一次" + mFunctionConfig.getMaxSize() + "张，不能再多了");
                return;
            } else {
                mSelectPhotoMap.put(info.getPhotoPath(), info);
                checked = true;
            }
        } else {
            mSelectPhotoMap.remove(info.getPhotoPath());
            checked = false;
        }
        refreshSelectCount();

        PhotoListAdapter.PhotoViewHolder holder = (PhotoListAdapter.PhotoViewHolder) view.getTag();
        if (holder != null) {
            if (checked) {
                holder.mIvCheck.setImageResource(R.mipmap.ic_photo_checked);
            } else {
                holder.mIvCheck.setImageResource(R.mipmap.ic_photo_unchecked);
            }
        } else {
            mPhotoListAdapter.notifyDataSetChanged();
        }
    }

    public void refreshSelectCount() {
        mTvChooseCount.setText(getString(R.string.selected, mSelectPhotoMap.size(), mFunctionConfig.getMaxSize()));
    }

    @Override
    public void onPermissionsGranted(List<String> list) {
        getPhotos();
    }

    @Override
    public void onPermissionsDenied(List<String> list) {
        mTvEmptyView.setText(R.string.permissions_denied_tips);
    }

    /**
     * 获取所有图片
     */
    @AfterPermissionGranted(GalleryFinal.PERMISSIONS_CODE_GALLERY)
    private void requestGalleryPermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            getPhotos();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.permissions_tips_gallery),
                    GalleryFinal.PERMISSIONS_CODE_GALLERY, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void getPhotos() {
        mTvEmptyView.setText(R.string.waiting);
        mGvPhotoList.setEnabled(false);
        new Thread() {
            @Override
            public void run() {
                super.run();

                List<PhotoFolderInfo> allFolderList = PhotoTools.getAllPhotoFolder(PhotoSelectActivity.this, mSelectPhotoMap);

                mCurPhotoList.clear();
                if (allFolderList.size() > 0) {
                    if (allFolderList.get(0).getPhotoList() != null) {
                        mCurPhotoList.addAll(allFolderList.get(0).getPhotoList());
                    }
                }
                refreshAdapter();
            }
        }.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mHasRefreshGallery) {
            mHasRefreshGallery = false;
            requestGalleryPermission();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (GalleryFinal.getCoreConfig().getImageLoader() != null) {
            GalleryFinal.getCoreConfig().getImageLoader().clearMemoryCache();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPhotoTargetFolder = null;
        mSelectPhotoMap.clear();
        System.gc();
    }
}
