package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.blurdialogfragment.BlurDialogFragment;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.view.activity.AboutUsActivity;
import co.quchu.quchu.view.activity.FeedbackActivity;

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

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_menu_setting, null);
        ButterKnife.bind(this, view);
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(view);

        return dialog;
    }

    @OnClick({R.id.dialog_menu_setting_aboutus_tv,
            R.id.dialog_menu_setting_feedback_tv, R.id.dialog_menu_setting_update_tv})
    public void menuSettingClick(View view) {
        switch (view.getId()) {
//            case R.id.dialog_menu_setting_account_setting_tv:
//                if (AppContext.user.isIsVisitors()) {
//                    MenuSettingDialogFg.this.dismiss();
//                    VisitorLoginDialogFg vDialog = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QACCOUNTSETTING);
//                    vDialog.show(getActivity().getFragmentManager(), "visitor");
//                } else {
//                    getActivity().startActivity(new Intent(getActivity(), AccountSettingActivity.class));
//                    MenuSettingDialogFg.this.dismiss();
//                    getActivity().finish();
//                }
//                break;
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
//                if (getActivity() instanceof MenusActivity)
//                    ((MenusActivity) getActivity()).checkUpdate();
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
