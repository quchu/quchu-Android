package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;


import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.LoginActivity;


/**
 * LocationSelectedDialogFg
 * User: Chenhs
 * Date: 2015-12-23
 * 游客登录提醒弹窗
 */
public class VisitorLoginDialogFg extends DialogFragment {
    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    private static final String VIEW_MODEL = "view_model"; //弹窗界面
    //    @Bind(R.id.dialog_location_selected_city_tv)
//    TextView dialogLocationSelectedCityTv;
    @Bind(R.id.dialog_location_tv)
    TextView dialogLocationTv;
//    @Bind(R.id.dialog_location_submit_tv)
//    TextView dialogLocationSubmitTv;
//    @Bind(R.id.dialog_location_cancel_tv)
//    TextView dialogLocationCancelTv;


    private int viewModel = 0x00;
    public static final int QFOCUS = 0x01; // 关注
    public static final int QPRAISE = 0x02; // 点赞
    public static final int QAVATAR = 0x03; //头像 趣星球
    public static final int QACCOUNTSETTING = 0x04; //账户设置
    public static final int QMESSAGECENTER = 0x05; // 消息中心
    public static final int QBEEN = 0x06;//quguo
    public static final int QFAVORITE = 0x07; //收藏

    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @param viewModel
     * @return well instantiated fragment.
     */
    public static VisitorLoginDialogFg newInstance(int viewModel) {
        VisitorLoginDialogFg fragment = new VisitorLoginDialogFg();
        Bundle args = new Bundle();
        args.putSerializable(VIEW_MODEL, viewModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        viewModel = (int) args.getSerializable(VIEW_MODEL);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_visitor_login, null);
        ButterKnife.bind(this, view);

        builder.setView(view);
        switch (viewModel) {
            case QACCOUNTSETTING:
                dialogLocationTv.setText(String.format(getResources().getString(R.string.visitor_login_prompt_text, "进入账户设置")));
                StringUtils.alterTextColor(dialogLocationTv, 2, 6, R.color.standard_color_yellow);
                break;
            case QMESSAGECENTER:
                dialogLocationTv.setText(String.format(getResources().getString(R.string.visitor_login_prompt_text, "进入消息中心")));
                StringUtils.alterTextColor(dialogLocationTv, 2, 6, R.color.standard_color_yellow);
                break;
            case QBEEN:
                dialogLocationTv.setText(String.format(getResources().getString(R.string.visitor_login_prompt_text, "明信片操作")));
                StringUtils.alterTextColor(dialogLocationTv, 2, 6, R.color.standard_color_yellow);
                break;
            default:
                dialogLocationTv.setText(String.format(getResources().getString(R.string.visitor_login_prompt_text, "进行该操作")));
                break;
        }
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);

    }


    @OnClick({R.id.dialog_location_submit_tv, R.id.dialog_location_cancel_tv})
    public void loacationDialogClick(View view) {

        switch (view.getId()) {
            case R.id.dialog_location_submit_tv:
                MobclickAgent.onEvent(view.getContext(), "pop_login_c");
                startActivity(new Intent(getActivity(), LoginActivity.class));

            case R.id.dialog_location_cancel_tv:
                VisitorLoginDialogFg.this.dismiss();
                break;

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
