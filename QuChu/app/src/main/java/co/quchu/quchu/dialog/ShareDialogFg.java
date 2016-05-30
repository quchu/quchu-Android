package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.tauth.Tencent;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.dialog.adapter.DialogShareAdapter;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.thirdhelp.QQHelper;
import co.quchu.quchu.thirdhelp.WechatHelper;
import co.quchu.quchu.thirdhelp.WeiboHelper;
import co.quchu.quchu.utils.KeyboardUtils;

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
    private static final int CUSTOM_SHIT = 0x0000001;
    @Bind(R.id.dialog_share_gv)
    GridView dialogShareGv;
    @Bind(R.id.actionClose)
    ImageView actionClose;


    public static ShareDialogFg newInstance(int shareId, String titles, boolean isPlace) {
        ShareDialogFg fragment = new ShareDialogFg();
        Bundle args = new Bundle();
        args.putInt(SHAREID, shareId);
        args.putString(SHRETITLE, titles);
        args.putBoolean(ISSHARE_PLACE, isPlace);
        fragment.setArguments(args);
        return fragment;
    }

    public static ShareDialogFg newInstance(String shareUrl, String title) {
        ShareDialogFg fragment = new ShareDialogFg();
        Bundle args = new Bundle();
        args.putInt(SHAREID, CUSTOM_SHIT);
        args.putString(SHRETITLE, title);
        args.putString(SHARE_URL, shareUrl);
        fragment.setArguments(args);
        return fragment;
    }

    private int shareId = 0;
    private String shareTitle = "";
    private boolean isPlace = false;
    Tencent mTencent;
    String shareUrlFinal = "";

    String shareUrl = "";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Bundle args = getArguments();
        shareId = args.getInt(SHAREID);
        shareTitle = args.getString(SHRETITLE);
        isPlace = args.getBoolean(ISSHARE_PLACE);
        shareUrl = args.getString(SHARE_URL);
        mTencent = Tencent.createInstance("1104964977", AppContext.mContext);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_share_view, null);
        ButterKnife.bind(this, view);
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(view);
        ButterKnife.bind(this, view);

        actionClose.setOnClickListener(this);
        view.setOnClickListener(this);

        dialogShareGv.setAdapter(new DialogShareAdapter(getContext()));
        dialogShareGv.setOnItemClickListener(this);
        shareUrlFinal = String.format(isPlace ? NetApi.sharePlace : NetApi.sharePostCard, shareId);

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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (position) {
            case 0:
                WechatHelper.shareFriends(getActivity(), shareUrlFinal, shareTitle, true);
                break;
            case 1:
                WechatHelper.shareFriends(getActivity(), shareUrlFinal, shareTitle, false);
                break;
            case 2:
                QQHelper.share2QQ(getActivity(), mTencent, shareUrlFinal, shareTitle);
                break;
            case 3:
                WeiboHelper.getInstance(getActivity()).share2Weibo(getActivity(), shareUrlFinal, shareTitle);
                break;
            case 4:
                copyToClipBoard(shareTitle, shareUrlFinal);
                break;
        }

        if (!isPlace) {//数据收集
            GsonRequest<String> request = new GsonRequest<>(String.format(Locale.SIMPLIFIED_CHINESE, NetApi.shareCollect, shareId), null);
            request.start(getContext());

        }

        ShareDialogFg.this.dismiss();
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