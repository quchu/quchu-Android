package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tencent.tauth.Tencent;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.blurdialogfragment.BlurDialogFragment;
import co.quchu.quchu.dialog.adapter.DialogShareAdapter;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.thirdhelp.QQHelper;
import co.quchu.quchu.thirdhelp.WechatHelper;
import co.quchu.quchu.thirdhelp.WeiboHelper;
import co.quchu.quchu.utils.SPUtils;

/**
 * ShareDialogFg
 * User: Chenhs
 * Date: 2015-12-23
 */
public class ShareDialogFg extends BlurDialogFragment implements AdapterView.OnItemClickListener {
    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    private static final String SHAREID = "share_id";
    private static final String SHRETITLE = "share_title";
    private static final String ISSHARE_PLACE = "isshare_place";
    @Bind(R.id.dialog_share_gv)
    GridView dialogShareGv;

    private ArrayList<CityModel> cityList;

    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @return well instantiated fragment.
     * Serializable cityList
     */
    public static ShareDialogFg newInstance(int shareId, String titles, boolean isPlace) {
        ShareDialogFg fragment = new ShareDialogFg();
        Bundle args = new Bundle();
        // args.putSerializable(CITY_LIST_MODEL, cityList);
        args.putInt(SHAREID, shareId);
        args.putString(SHRETITLE, titles);
        args.putBoolean(ISSHARE_PLACE, isPlace);
        fragment.setArguments(args);
        return fragment;
    }

    private int shareId = 0;
    private String shareTitle = "";
    private boolean isPlace = false;
    Tencent mTencent;
    String shareUrl = "";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Bundle args = getArguments();
        shareId = args.getInt(SHAREID);
        shareTitle = args.getString(SHRETITLE);
        isPlace = args.getBoolean(ISSHARE_PLACE);
        mTencent = Tencent.createInstance("1104964977", AppContext.mContext);

        //   cityList = (ArrayList<CityModel>) args.getSerializable(CITY_LIST_MODEL);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_share_view, null);
        ButterKnife.bind(this, view);
        dialogShareGv.setAdapter(new DialogShareAdapter(getActivity()));
        dialogShareGv.setOnItemClickListener(this);
        shareUrl = String.format(NetApi.sharePlace, shareId, SPUtils.getUserToken(AppContext.mContext));
        builder.setView(view);
        return builder.create();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
                new WechatHelper(getActivity()).shareFriends(shareUrl, shareTitle, true);
                break;
            case 1:
                new WechatHelper(getActivity()).shareFriends(shareUrl, shareTitle, false);
                break;
            case 2:
                QQHelper.share2QQ(getActivity(), mTencent, shareUrl, shareTitle);
                break;
            case 3:
                QQHelper.shareToQzone(getActivity(), mTencent, shareUrl, shareTitle);
                break;
            case 4:
                WeiboHelper.share2Weibo(getActivity(),shareUrl,shareTitle);
                break;
        }
        ShareDialogFg.this.dismiss();
    }
}
