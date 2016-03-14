package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.blurdialogfragment.BlurDialogFragment;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.view.activity.AboutUsActivity;
import co.quchu.quchu.view.activity.AccountSettingActivity;
import co.quchu.quchu.view.activity.FeedbackActivity;
import co.quchu.quchu.view.activity.MenusActivity;

/**
 * LocationSelectedDialogFg
 * User: Chenhs
 * Date: 2015-12-23
 * 城市选择弹窗
 */
public class MenuSettingDialogFg extends BlurDialogFragment {
    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    private static final String CITY_LIST_MODEL = "city_list_model";
    @Bind(R.id.dialog_menu_setting_account_setting_tv)
    TextView dialogMenuSettingAccountSettingTv;
    @Bind(R.id.dialog_menu_setting_feedback_tv)
    TextView dialogMenuSettingFeedbackTv;
    @Bind(R.id.dialog_menu_setting_aboutus_tv)
    TextView dialogMenuSettingAboutusTv;
    @Bind(R.id.dialog_menu_setting_update_tv)
    TextView dialogMenuSettingUpdateTv;


    private ArrayList<CityModel> cityList;

    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @return well instantiated fragment.
     * Serializable cityList
     */
    public static MenuSettingDialogFg newInstance() {
        MenuSettingDialogFg fragment = new MenuSettingDialogFg();
        Bundle args = new Bundle();
        // args.putSerializable(CITY_LIST_MODEL, cityList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Bundle args = getArguments();
    /*    mRadius = args.getInt(BUNDLE_KEY_BLUR_RADIUS);
        mDownScaleFactor = args.getFloat(BUNDLE_KEY_DOWN_SCALE_FACTOR);
        mDimming = args.getBoolean(BUNDLE_KEY_DIMMING);
        mDebug = args.getBoolean(BUNDLE_KEY_DEBUG);*/


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_menu_setting, null);

        ButterKnife.bind(this, view);
        builder.setView(view);

        //去掉背景&将其居中
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        return dialog;
    }

    @OnClick({R.id.dialog_menu_setting_account_setting_tv, R.id.dialog_menu_setting_aboutus_tv,
            R.id.dialog_menu_setting_feedback_tv, R.id.dialog_menu_setting_update_tv})
    public void menuSettingClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_menu_setting_account_setting_tv:
                if (AppContext.user.isIsVisitors()) {
                    MenuSettingDialogFg.this.dismiss();
                    VisitorLoginDialogFg vDialog = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QACCOUNTSETTING);
                    vDialog.show(getActivity().getFragmentManager(), "visitor");
                } else {
                    getActivity().startActivity(new Intent(getActivity(), AccountSettingActivity.class));
                    MenuSettingDialogFg.this.dismiss();
                    getActivity().finish();
                }
                break;
            case R.id.dialog_menu_setting_aboutus_tv:
                //     Toast.makeText(getActivity(), "即将开发，敬请期待", Toast.LENGTH_SHORT).show();
                getActivity().startActivity(new Intent(getActivity(), AboutUsActivity.class));
                MenuSettingDialogFg.this.dismiss();
                //      getActivity().finish();
                break;
            case R.id.dialog_menu_setting_feedback_tv:
                getActivity().startActivity(new Intent(getActivity(), FeedbackActivity.class));
                MenuSettingDialogFg.this.dismiss();
                //    getActivity().finish();
                break;
            case R.id.dialog_menu_setting_update_tv:
                if (getActivity() instanceof MenusActivity)
                    ((MenusActivity) getActivity()).checkUpdate();
                MenuSettingDialogFg.this.dismiss();
                break;
        }

    }

    @Override
    protected boolean isDebugEnable() {
        return false;
    }

    @Override
    protected boolean isDimmingEnable() {
        return true;
    }

    @Override
    protected boolean isActionBarBlurred() {
        return true;
    }

    @Override
    protected float getDownScaleFactor() {
        return 3.8f;
    }

    @Override
    protected int getBlurRadius() {
        return 8;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
