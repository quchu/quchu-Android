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

package co.quchu.quchu.gallery.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.List;
import java.util.Map;

import co.quchu.quchu.R;
import co.quchu.quchu.gallery.GalleryFinal;
import co.quchu.quchu.gallery.model.PhotoInfo;
import co.quchu.quchu.gallery.utils.ImageUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/10 下午4:59
 */
public class PhotoListAdapter extends BaseAdapter {

    private Map<String, PhotoInfo> mSelectList;
    private int mScreenWidth;
    private List<PhotoInfo> list;
    private Activity activity;

    public PhotoListAdapter(Activity activity, List<PhotoInfo> list, Map<String, PhotoInfo> selectList, int screenWidth) {
//        super(activity, list);
        this.list = list;
        this.activity = activity;
        this.mSelectList = selectList;
        this.mScreenWidth = screenWidth;
    }

//    @Override
//    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int position) {
//        View view = inflate(R.layout.gf_adapter_photo_list_item, parent);
//        setHeight(view);
//        return new PhotoViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(PhotoViewHolder holder, int position) {
//        PhotoInfo photoInfo = getDatas().get(position);
//
//        String path;
//        path = photoInfo.getPhotoPath();
//
//        if (null != photoInfo.getThumbPath()) {
//            holder.mIvThumb.setImageURI(Uri.fromFile(new File(photoInfo.getThumbPath())));
//        } else {
//            ImageUtils.loadWithAppropriateSize(holder.mIvThumb, Uri.fromFile(new File(path)));
//        }
//        if (GalleryFinal.getFunctionConfig().isMutiSelect()) {
//            holder.mIvCheck.setVisibility(View.VISIBLE);
//            if (mSelectList.get(photoInfo.getPhotoPath()) != null) {
//                holder.mIvCheck.setImageResource(R.mipmap.ic_photo_checked);
//            } else {
//                holder.mIvCheck.setImageResource(R.mipmap.ic_photo_unchecked);
//            }
//        } else {
//            holder.mIvCheck.setVisibility(View.GONE);
//        }
//    }

    private void setHeight(final View convertView) {
        int height = mScreenWidth / 3 - 8;
        convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(activity, R.layout.gf_adapter_photo_list_item, null);
            setHeight(convertView);
            holder = new PhotoViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (PhotoViewHolder) convertView.getTag();
        }
        PhotoInfo photoInfo = list.get(position);

        String path;
        path = photoInfo.getPhotoPath();

        if (null != photoInfo.getThumbPath()) {
            holder.mIvThumb.setImageURI(Uri.fromFile(new File(photoInfo.getThumbPath())));
        } else {
            ImageUtils.loadWithAppropriateSize(holder.mIvThumb, Uri.fromFile(new File(path)));
        }
        if (GalleryFinal.getFunctionConfig().isMutiSelect()) {
            holder.mIvCheck.setVisibility(View.VISIBLE);
            if (mSelectList.get(photoInfo.getPhotoPath()) != null) {
                holder.mIvCheck.setImageResource(R.mipmap.ic_photo_checked);
            } else {
                holder.mIvCheck.setImageResource(R.mipmap.ic_photo_unchecked);
            }
        } else {
            holder.mIvCheck.setVisibility(View.GONE);
        }

        return convertView;
    }

    public static class PhotoViewHolder {

        public SimpleDraweeView mIvThumb;
        public ImageView mIvCheck;
        View mView;

        public PhotoViewHolder(View view) {
            mView = view;
            mIvThumb = (SimpleDraweeView) view.findViewById(R.id.iv_thumb);
            mIvCheck = (ImageView) view.findViewById(R.id.iv_check);
        }
    }
}
