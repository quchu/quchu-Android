package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.view.activity.FeedbackActivity;
import co.quchu.quchu.view.activity.StatementActivity;

/**
 * LocationSelectedDialogFg
 * User: Chenhs
 * Date: 2015-12-23
 * 城市选择弹窗
 */
public class MenuSettingDialogFg extends DialogFragment implements View.OnClickListener {
    @Bind(R.id.actionClose)
    ImageView actionClose;

    public static MenuSettingDialogFg newInstance() {
        return new MenuSettingDialogFg();
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

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_menu_setting, null);
        ButterKnife.bind(this, view);
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(view);
        actionClose.setOnClickListener(this);
        view.setOnClickListener(this);
        return dialog;
    }

    @OnClick({R.id.dialog_menu_setting_aboutus_tv,
            R.id.dialog_menu_setting_feedback_tv, R.id.dialog_menu_setting_update_tv})
    public void menuSettingClick(View view) {
        switch (view.getId()) {

            case R.id.dialog_menu_setting_aboutus_tv:
                Intent intent = new Intent(getActivity(), StatementActivity.class);
                intent.putExtra(StatementActivity.REQUEST_KEY_TITLE, "关于我们");
                String about = String.format(Locale.CHINA, getString(R.string.about_us_text), AppContext.packageInfo.versionName);

                intent.putExtra(StatementActivity.REQUEST_KEY_CONTENT, about);
                getActivity().startActivity(intent);

                MenuSettingDialogFg.this.dismiss();
                break;
            case R.id.dialog_menu_setting_feedback_tv:
                getActivity().startActivity(new Intent(getActivity(), FeedbackActivity.class));
                MenuSettingDialogFg.this.dismiss();
                break;
            case R.id.dialog_menu_setting_update_tv:
                EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_APPLICATION_CHECK_UPDATE));
                break;
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
