package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.blurdialogfragment.BlurDialogFragment;

/**
 * Created by admin on 2016/3/25.
 */
public class ConfirmDialogFg extends BlurDialogFragment {
    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    @Bind(R.id.dialog_location_selected_city_tv)
    TextView dialogLocationSelectedCityTv;
    @Bind(R.id.dialog_location_tv)
    TextView dialogLocationTv;
    @Bind(R.id.dialog_location_submit_tv)
    TextView dialogLocationSubmitTv;
    @Bind(R.id.dialog_location_cancel_tv)
    TextView dialogLocationCancelTv;

    public static final int INDEX_OK = 0x0001;
    public static final int INDEX_CANCEL = 0x0002;

    private OnActionListener mListener;

    public static final String BUNDLE_KEY_ALERT_DIALOG_RES_TITLE = "BUNDLE_KEY_ALERT_DIALOG_RES_TITLE";
    public static final String BUNDLE_KEY_ALERT_DIALOG_RES_CONTENT = "BUNDLE_KEY_ALERT_DIALOG_RES_CONTENT";


    public static ConfirmDialogFg newInstance(String title, String mResContent) {
        ConfirmDialogFg fg = new ConfirmDialogFg();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_ALERT_DIALOG_RES_TITLE, title);
        bundle.putString(BUNDLE_KEY_ALERT_DIALOG_RES_CONTENT, mResContent);
        fg.setArguments(bundle);
        return fg;
    }

    public void setActionListener(OnActionListener onActionListener) {
        this.mListener = onActionListener;
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
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_confirm, null);
        ButterKnife.bind(this, view);

        builder.setView(view);
        Bundle bundle = getArguments();
        dialogLocationSelectedCityTv.setText(bundle.getString(BUNDLE_KEY_ALERT_DIALOG_RES_TITLE));
        dialogLocationTv.setText(bundle.getString(BUNDLE_KEY_ALERT_DIALOG_RES_CONTENT));
        dialogLocationSubmitTv.setText("确定");
        dialogLocationCancelTv.setText("取消");

        //StringUtils.alterTextColor(dialogLocationTv, 2, 4, R.color.standard_color_yellow);
        return builder.create();
    }


    public interface OnActionListener {
        void onClick(int index);
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


    @OnClick({R.id.dialog_location_submit_tv, R.id.dialog_location_cancel_tv})
    public void loacationDialogClick(View view) {

        int index = -1;
        switch (view.getId()) {
            case R.id.dialog_location_submit_tv:
                index = INDEX_OK;
                break;
            case R.id.dialog_location_cancel_tv:
                index = INDEX_CANCEL;
                break;
        }
        if (null != mListener) {
            mListener.onClick(index);
        }

        dismiss();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
