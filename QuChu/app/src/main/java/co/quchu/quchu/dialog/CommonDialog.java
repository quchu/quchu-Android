package co.quchu.quchu.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.blurdialogfragment.BlurDialogFragment;

/**
 * Created by no21 on 2016/5/11.
 * email:437943145@qq.com
 * desc :通用的,对话框
 */
public class CommonDialog extends BlurDialogFragment implements View.OnClickListener {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.subTitle)
    TextView subTitle;
    @Bind(R.id.passive)
    Button passive;
    @Bind(R.id.active)
    Button active;


    private OnActionListener listener;

    private static final String KEY_TITLE = "title";
    private static final String KEY_SUBTITLE = "subTitle";
    private static final String KEY_ACTIVE = "sactive";
    private static final String KEY_PASSIVE = "passive";

    public static CommonDialog newInstance(@NonNull String title, @NonNull String subTitle, @NonNull String activeName, @Nullable String passiveName) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_SUBTITLE, subTitle);
        bundle.putString(KEY_ACTIVE, activeName);
        bundle.putString(KEY_PASSIVE, passiveName);
        CommonDialog dialog = new CommonDialog();
        dialog.setCancelable(false);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_common, null);
        ButterKnife.bind(this, view);
        builder.setView(view);
        Bundle bundle = getArguments();

        title.setText(bundle.getString(KEY_TITLE));
        subTitle.setText(bundle.getString(KEY_SUBTITLE));
        active.setText(bundle.getString(KEY_ACTIVE));

        if (TextUtils.isEmpty(bundle.getString(KEY_PASSIVE))) {
            passive.setVisibility(View.GONE);
        } else {
            passive.setText(bundle.getString(KEY_PASSIVE));
            passive.setOnClickListener(this);
        }
        active.setOnClickListener(this);


        return builder.create();
    }


    public void setListener(OnActionListener listener) {
        this.listener = listener;
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

    @Override
    public void onClick(View v) {

        if (listener == null)
            return;

        switch (v.getId()) {
            case R.id.passive:
                listener.passiveClick();
                break;
            case R.id.active:
                listener.activeClick();
                break;
        }
    }

    public interface OnActionListener {
        void passiveClick();

        void activeClick();
    }

}
