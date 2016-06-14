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

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import java.util.List;

import co.quchu.quchu.R;
import co.quchu.quchu.gallery.model.PhotoInfo;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/2 上午11:05
 */
public class GalleryFinal {
    static final int TAKE_REQUEST_CODE = 1001;

    static final int PERMISSIONS_CODE_GALLERY = 2001;

    private static FunctionConfig mCurrentFunctionConfig;
    private static FunctionConfig mGlobalFunctionConfig;
    //    private static ThemeConfig mThemeConfig;
    private static CoreConfig mCoreConfig;

    private static OnHanlderResultCallback mCallback;
    private static int mRequestCode;

    public static void init(CoreConfig coreConfig) {
//        mThemeConfig = coreConfig.getThemeConfig();
        mCoreConfig = coreConfig;
        mGlobalFunctionConfig = coreConfig.getFunctionConfig();
    }

    public static FunctionConfig copyGlobalFuncationConfig() {
        if (mGlobalFunctionConfig != null) {
            return mGlobalFunctionConfig.clone();
        }
        return null;
    }

    public static CoreConfig getCoreConfig() {
        return mCoreConfig;
    }

    public static FunctionConfig getFunctionConfig() {
        return mCurrentFunctionConfig;
    }

//    public static ThemeConfig getGalleryTheme() {
//        if (mThemeConfig == null) {
//            //使用默认配置
//            mThemeConfig = ThemeConfig.DEFAULT;
//        }
//        return mThemeConfig;
//    }


    /**
     * 打开Gallery-单选
     *
     * @param requestCode
     * @param config
     * @param callback
     */
    public static void openGallerySingle(Context context, int requestCode, FunctionConfig config, OnHanlderResultCallback callback) {
        if (mCoreConfig.getImageLoader() == null) {
            if (callback != null) {
                callback.onHanlderFailure(requestCode, mCoreConfig.getContext().getString(R.string.open_gallery_fail));
            }
            return;
        }

        if (config == null && mGlobalFunctionConfig == null) {
            if (callback != null) {
                callback.onHanlderFailure(requestCode, mCoreConfig.getContext().getString(R.string.open_gallery_fail));
            }
            return;
        }

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(mCoreConfig.getContext(), R.string.empty_sdcard, Toast.LENGTH_SHORT).show();
            return;
        }
        config.mutiSelect = false;
        mRequestCode = requestCode;
        mCallback = callback;
        mCurrentFunctionConfig = config;

        Intent intent = new Intent(mCoreConfig.getContext(), PhotoSelectActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 打开Gallery-多选
     *
     * @param requestCode
     * @param config
     * @param callback
     */
    public static void openGalleryMuti(Context context, int requestCode, FunctionConfig config, OnHanlderResultCallback callback) {
        if (mCoreConfig.getImageLoader() == null) {
            if (callback != null) {
                callback.onHanlderFailure(requestCode, mCoreConfig.getContext().getString(R.string.open_gallery_fail));
            }
            return;
        }

        if (config == null && mGlobalFunctionConfig == null) {
            if (callback != null) {
                callback.onHanlderFailure(requestCode, mCoreConfig.getContext().getString(R.string.open_gallery_fail));
            }
            return;
        }

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(mCoreConfig.getContext(), R.string.empty_sdcard, Toast.LENGTH_SHORT).show();
            return;
        }
        mRequestCode = requestCode;
        mCallback = callback;
        mCurrentFunctionConfig = config;

        if (config != null) {
            config.mutiSelect = true;
        }

        Intent intent = new Intent(mCoreConfig.getContext(), PhotoSelectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 打开相机
     *
     * @param config
     * @param callback
     */
    public static void openCamera(Context context, int requestCode, FunctionConfig config, OnHanlderResultCallback callback) {
        if (mCoreConfig.getImageLoader() == null) {
            if (callback != null) {
                callback.onHanlderFailure(requestCode, mCoreConfig.getContext().getString(R.string.open_gallery_fail));
            }
            return;
        }

        if (config == null && mGlobalFunctionConfig == null) {
            if (callback != null) {
                callback.onHanlderFailure(requestCode, mCoreConfig.getContext().getString(R.string.open_gallery_fail));
            }
            return;
        }

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(mCoreConfig.getContext(), R.string.empty_sdcard, Toast.LENGTH_SHORT).show();
            return;
        }
        mRequestCode = requestCode;
        mCallback = callback;

//        config.mutiSelect = false;//拍照为单选
        mCurrentFunctionConfig = config;

        Intent intent = new Intent(mCoreConfig.getContext(), PhotoEditActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(PhotoEditActivity.TAKE_PHOTO_ACTION, true);
        context.startActivity(intent);
    }


//    /**
//     * 缓存文件
//     */
//    public static void cleanCacheFile() {
//        if (mCurrentFunctionConfig != null && mCoreConfig.getEditPhotoCacheFolder() != null) {
//            //清楚裁剪冗余图片
//            new Thread() {
//                @Override
//                public void run() {
//                    super.run();
//                    FileUtils.deleteFile(mCoreConfig.getEditPhotoCacheFolder());
//                }
//            }.start();
//        }
//    }

    public static int getRequestCode() {
        return mRequestCode;
    }

    public static OnHanlderResultCallback getCallback() {
        return mCallback;
    }

    /**
     * 处理结果
     */
    public interface OnHanlderResultCallback {
        /**
         * 处理成功
         *
         * @param reqeustCode
         * @param resultList
         */
        void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList);

        /**
         * 处理失败或异常
         *
         * @param requestCode
         * @param errorMsg
         */
        void onHanlderFailure(int requestCode, String errorMsg);
    }
}
