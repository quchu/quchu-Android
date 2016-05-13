package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.activity.AccountSettingActivity;

/**
 * LocationSelectedDialogFg
 * User: Chenhs
 * Date: 2015-12-23
 * 城市选择弹窗
 */
public class LocationSettingDialogFg extends DialogFragment {


    @Bind(R.id.account_setting_location_rl)
    RelativeLayout accountSettingLocationRl;
    @Bind(R.id.account_setting_user_location)
    EditText accountSettingUserLocation;
    @Bind(R.id.dialog_location_submit_tv)
    TextView dialogLocationSubmitTv;
    @Bind(R.id.dialog_location_cancel_tv)
    TextView dialogLocationCancelTv;
    @Bind(R.id.dialog_location_bottom_textviews_rl)
    RelativeLayout dialogLocationBottomTextviewsRl;

    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @return well instantiated fragment.
     * Serializable cityList
     */
    public static LocationSettingDialogFg newInstance() {
        LocationSettingDialogFg fragment = new LocationSettingDialogFg();

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_location_setting, null);
        ButterKnife.bind(this, view);
        accountSettingUserLocation.setText(SPUtils.getValueFromSPMap(getActivity(), AppKey.LOCATION_CITY));
        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onResume() {
        LogUtils.json("dialog resume");
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtils.json("dialog onPause");
        super.onPause();
    }


    @OnClick({R.id.dialog_location_submit_tv, R.id.dialog_location_cancel_tv, R.id.account_setting_location_rl})
    public void loacationDialogClick(View view) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case R.id.dialog_location_submit_tv:
                if (getActivity() instanceof AccountSettingActivity)
                    ((AccountSettingActivity) getActivity()).updateLocation(accountSettingUserLocation.getText().toString());
            case R.id.dialog_location_cancel_tv:
                LocationSettingDialogFg.this.dismiss();
                break;

            case R.id.account_setting_location_rl:
                accountSettingUserLocation.setText(SPUtils.getValueFromSPMap(getActivity(), AppKey.LOCATION_CITY));
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
