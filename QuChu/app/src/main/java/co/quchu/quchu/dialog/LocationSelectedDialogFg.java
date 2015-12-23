package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.blurdialogfragment.BlurDialogFragment;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.utils.StringUtils;

/**
 * LocationSelectedDialogFg
 * User: Chenhs
 * Date: 2015-12-23
 * 城市选择弹窗
 */
public class LocationSelectedDialogFg extends BlurDialogFragment {
    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    private static final String CITY_LIST_MODEL = "city_list_model";
    @Bind(R.id.dialog_location_selected_city_tv)
    TextView dialogLocationSelectedCityTv;
    @Bind(R.id.dialog_location_xm_cb)
    CheckBox dialogLocationXmCb;
    @Bind(R.id.dialog_location_hz_cb)
    CheckBox dialogLocationHzCb;
    @Bind(R.id.dialog_location_submit_tv)
    TextView dialogLocationSubmitTv;
    @Bind(R.id.dialog_location_cancel_tv)
    TextView dialogLocationCancelTv;
    @Bind(R.id.dialog_location_bottom_textviews_rl)
    RelativeLayout dialogLocationBottomTextviewsRl;

    private ArrayList<CityModel> cityList;

    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @return well instantiated fragment.
     * Serializable cityList
     */
    public static LocationSelectedDialogFg newInstance() {
        LocationSelectedDialogFg fragment = new LocationSelectedDialogFg();
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

        cityList = (ArrayList<CityModel>) args.getSerializable(CITY_LIST_MODEL);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_location_selected, null);

        ButterKnife.bind(this, view);
        builder.setView(view);


        selectedIndex = 0;
        dialogLocationXmCb.setChecked(true);
        dialogLocationXmCb.setTextColor(getResources().getColor(R.color.gene_textcolor_yellow));
        dialogLocationSelectedCityTv.setText("所在城市:厦门");
        StringUtils.alterTextColor(dialogLocationSelectedCityTv,5,7,R.color.gene_textcolor_yellow);
        //  handler.sendMessageDelayed(handler.obtainMessage(1), 3000);
        return builder.create();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            LocationSelectedDialogFg.this.dismiss();
        }
    };

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

    private int selectedIndex = 0;

    @OnClick({R.id.dialog_location_xm_cb, R.id.dialog_location_hz_cb, R.id.dialog_location_submit_tv, R.id.dialog_location_cancel_tv})
    public void loacationDialogClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_location_submit_tv:
                //保存数据 而后关闭
            case R.id.dialog_location_cancel_tv:
                LocationSelectedDialogFg.this.dismiss();
                break;
            case R.id.dialog_location_xm_cb:
                if (selectedIndex != 0) {
                    selectedIndex = 0;
                    dialogLocationXmCb.setChecked(true);
                    dialogLocationXmCb.setClickable(false);
                    dialogLocationHzCb.setClickable(true);
                    dialogLocationHzCb.setChecked(false);
                    dialogLocationXmCb.setTextColor(getResources().getColor(R.color.gene_textcolor_yellow));
                    dialogLocationHzCb.setTextColor(getResources().getColor(R.color.text_color_white));
                    dialogLocationSelectedCityTv.setText("所在城市:厦门");
                    StringUtils.alterTextColor(dialogLocationSelectedCityTv,5,7, R.color.gene_textcolor_yellow);
                }
                break;
            case R.id.dialog_location_hz_cb:
                if (selectedIndex != 1) {
                    selectedIndex = 1;
                    dialogLocationHzCb.setChecked(true);
                    dialogLocationXmCb.setChecked(false);
                    dialogLocationHzCb.setClickable(false);
                    dialogLocationXmCb.setClickable(true);
                    dialogLocationHzCb.setTextColor(getResources().getColor(R.color.gene_textcolor_yellow));
                    dialogLocationXmCb.setTextColor(getResources().getColor(R.color.text_color_white));
                    dialogLocationSelectedCityTv.setText("所在城市:杭州");
                    StringUtils.alterTextColor(dialogLocationSelectedCityTv,5,7, R.color.gene_textcolor_yellow);
                }
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
