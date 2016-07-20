package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.tencent.tauth.Tencent;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.dialog.adapter.DialogShareAdapter;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.thirdhelp.QQHelper;
import co.quchu.quchu.thirdhelp.WechatHelper;
import co.quchu.quchu.thirdhelp.WeiboHelper;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;

/**
 * ShareDialogFg
 * User: Chenhs
 * Date: 2015-12-23
 */
public class ShareDialogFg extends DialogFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    private static final String SHAREID = "share_id";
    private static final String SHRETITLE = "share_title";
    private static final String ISSHARE_PLACE = "isshare_place";
    private static final String SHARE_URL = "SHARE_URL";
    private static final String SHARE_IMAGE_ID = "imageId";
    private static final String SHARE_IMAGE_PATH = "imagePath";
    private static final int CUSTOM_SHIT = 0x0000001;

    @Bind(R.id.dialog_share_gv)
    GridView dialogShareGv;
    @Bind(R.id.actionClose)
    ImageView actionClose;
    private int imageId;
    private String path;


    public static ShareDialogFg newInstance(int shareId, String titles, boolean isPlace, @Nullable int imageId, @Nullable String imagePath) {
        ShareDialogFg fragment = new ShareDialogFg();
        Bundle args = new Bundle();
        args.putInt(SHAREID, shareId);
        args.putInt(SHARE_IMAGE_ID, imageId);
        if (!isPlace) {
            args.putString(SHRETITLE, "送你一张美图，我想约你去" + titles);
        } else {
            args.putString(SHRETITLE, titles);
        }

        args.putBoolean(ISSHARE_PLACE, isPlace);
        args.putString(SHARE_IMAGE_PATH, imagePath);
        fragment.setArguments(args);

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(imagePath))
                .build();
        //图片目标大小
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.prefetchToBitmapCache(imageRequest, null);

        return fragment;
    }

    public static ShareDialogFg newInstance(String shareUrl, String title,String imagePath) {
        ShareDialogFg fragment = new ShareDialogFg();
        Bundle args = new Bundle();
        args.putInt(SHAREID, CUSTOM_SHIT);
        args.putString(SHRETITLE, title);
        args.putString(SHARE_URL, shareUrl);
        args.putString(SHARE_IMAGE_PATH, imagePath);

        fragment.setArguments(args);

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(imagePath))
                .build();
        //图片目标大小
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.prefetchToBitmapCache(imageRequest, null);


        return fragment;
    }

    private int shareId = 0;
    private String shareTitle = "";
    private boolean isPlace = false;
    Tencent mTencent;
    String shareUrlFinal = "";

    String shareUrl = "";


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        Bundle args = getArguments();
        shareId = args.getInt(SHAREID);
        shareTitle = args.getString(SHRETITLE);
        isPlace = args.getBoolean(ISSHARE_PLACE);
        shareUrl = args.getString(SHARE_URL);
        imageId = args.getInt(SHARE_IMAGE_ID);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_share_view, null);
        ButterKnife.bind(this, view);
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(view);
        ButterKnife.bind(this, view);

        actionClose.setOnClickListener(this);
        view.setOnClickListener(this);

        dialogShareGv.setAdapter(new DialogShareAdapter(getContext()));
        dialogShareGv.setOnItemClickListener(this);
        if (isPlace) {
            shareUrlFinal = String.format(NetApi.sharePlace, shareId);
        } else {
            shareUrlFinal = String.format(NetApi.sharePostCard, shareId, imageId);
        }

        if (shareId == CUSTOM_SHIT) {
            shareUrlFinal = shareUrl;
        }
        return dialog;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private Bitmap shareBitmap;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        if (!NetUtil.isNetworkConnected(getContext())) {
            Toast.makeText(getContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            return;
        }

        path = getArguments().getString(SHARE_IMAGE_PATH);
        if (TextUtils.isEmpty(path)) {
            share(position);
        } else {
            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(path))
                    .build();
            //图片目标大小
            ImagePipeline imagePipeline = Fresco.getImagePipeline();

            DataSource<CloseableReference<CloseableImage>> dataSource =
                    imagePipeline.fetchDecodedImage(imageRequest, null);
            try {
                CloseableReference<CloseableImage> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    try {
                        // Do something with the image, but do not keep the reference to it!
                        // The image may get recycled as soon as the reference gets closed below.
                        // If you need to keep a reference to the image, read the following sections.
                        CloseableStaticBitmap image = (CloseableStaticBitmap) imageReference.get();
                        shareBitmap = image.getUnderlyingBitmap();
                        shareBitmap = Bitmap.createScaledBitmap(shareBitmap, 150, 150, false);
                        LogUtils.e(image.getClass().getName());
                    } finally {
                        CloseableReference.closeSafely(imageReference);
                    }
                } else {
                    // cache miss
                    LogUtils.e("bitmap is null");

                }
            } finally {
                dataSource.close();
                share(position);
            }
        }

        if (!isPlace) {//数据收集
            GsonRequest<String> request = new GsonRequest<>(String.format(Locale.SIMPLIFIED_CHINESE, NetApi.shareCollect, shareId), null);
            request.start(getContext());

        }
        ShareDialogFg.this.dismiss();
    }

    private void share(int position) {
        switch (position) {
            case 0:
                WechatHelper.shareFriends(getActivity(), shareUrlFinal, shareTitle, true, shareBitmap);
                break;
            case 1:
                WechatHelper.shareFriends(getActivity(), shareUrlFinal, shareTitle, false, shareBitmap);
                break;
            case 2:
                mTencent = Tencent.createInstance("1104964977", AppContext.mContext);
                QQHelper.share2QQ(getActivity(), mTencent, shareUrlFinal, shareTitle, path);
                break;
            case 3:
                WeiboHelper.getInstance(getActivity()).share2Weibo(getActivity(), shareUrlFinal, shareTitle, shareBitmap);
                break;
            case 4:
                copyToClipBoard(shareTitle, shareUrlFinal);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionClose:
                dismiss();
                break;
            default:
                dismiss();
        }

    }

    private void copyToClipBoard(String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Activity.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), "复制成功", Toast.LENGTH_SHORT).show();
    }
}