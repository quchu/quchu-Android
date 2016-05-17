package co.quchu.quchu.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * Created by no21 on 2016/5/11.
 * email:437943145@qq.com
 * desc :通用的,对话框
 */
public class CommonDialog extends DialogFragment implements View.OnClickListener {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.subTitle)
    TextView subTitle;
    @Bind(R.id.passive)
    Button passive;
    @Bind(R.id.active)
    Button active;
    @Bind(R.id.subButton)
    TextView subButton;


    private OnActionListener listener;

    private static final String KEY_TITLE = "title";
    private static final String KEY_SUBTITLE = "subTitle";
    private static final String KEY_ACTIVE = "sactive";
    private static final String KEY_PASSIVE = "passive";
    private static final String KEY_SUBBUTTON = "subButton";

    public static CommonDialog newInstance(@NonNull String title, @NonNull String subTitle,
                                           @NonNull String activeName, @Nullable String passiveName, @Nullable String sunButton) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_SUBTITLE, subTitle);
        bundle.putString(KEY_ACTIVE, activeName);
        bundle.putString(KEY_PASSIVE, passiveName);
        bundle.putString(KEY_SUBBUTTON, sunButton);
        CommonDialog dialog = new CommonDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    public static CommonDialog newInstance(@NonNull String title, @NonNull String subTitle,
                                           @NonNull String activeName, @Nullable String passiveName) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_SUBTITLE, subTitle);
        bundle.putString(KEY_ACTIVE, activeName);
        bundle.putString(KEY_PASSIVE, passiveName);
        CommonDialog dialog = new CommonDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AppCompatDialog dialog = new AppCompatDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_common, null);
        ButterKnife.bind(this, view);
        dialog.setContentView(view);
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

        if (TextUtils.isEmpty(bundle.getString(KEY_SUBBUTTON))) {
            subButton.setVisibility(View.GONE);
        } else {
            subButton.setText(bundle.getString(KEY_PASSIVE));
            subButton.setOnClickListener(this);
        }
        active.setOnClickListener(this);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCancelable()) {
                    dismiss();
                }
            }
        });

        return dialog;
    }


    public void setListener(OnActionListener listener) {
        this.listener = listener;
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
                listener.passiveClick(OnActionListener.CLICK_ID_PASSIVE);
                break;
            case R.id.active:
                listener.passiveClick(OnActionListener.CLICK_ID_ACTIVE);
                break;
            case R.id.subButton:
                listener.passiveClick(OnActionListener.CLICK_ID_SUBBUTTON);
        }
    }


    public interface OnActionListener {
        int CLICK_ID_PASSIVE = 1;
        int CLICK_ID_ACTIVE = 2;
        int CLICK_ID_SUBBUTTON = 3;

        void passiveClick(int id);
    }

}
