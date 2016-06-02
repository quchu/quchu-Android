package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.net.NetApi;

/**
 * ShareDialogFg
 * User: Chenhs
 * Date: 2015-12-23
 */
public class ASUserPhotoDialogFg extends DialogFragment implements View.OnClickListener {


    @Bind(R.id.select_photo)
    TextView selectPhoto;
    @Bind(R.id.select_qutouxiang)
    TextView selectQutouxiang;
    @Bind(R.id.select_cancle)
    Button selectCancle;

    public static ASUserPhotoDialogFg newInstance() {
        return new ASUserPhotoDialogFg();
    }

    String shareUrl = "";
    private UserPhotoOriginSelectedListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void setOnOriginListener(UserPhotoOriginSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AppCompatDialog dialog = new AppCompatDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_selected_photo, null);
        ButterKnife.bind(this, view);
        dialog.setContentView(view);


        boolean isPlace = false;
        int shareId = 0;
        shareUrl = String.format(isPlace ? NetApi.sharePlace : NetApi.sharePostCard, shareId);

        selectCancle.setOnClickListener(this);
        selectQutouxiang.setOnClickListener(this);
        selectPhoto.setOnClickListener(this);
        return dialog;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_cancle:
                break;
            case R.id.select_photo:
                listener.selectedAblum();
                break;
            case R.id.select_qutouxiang:
                listener.selectedQuPhtot();
                break;
        }
        dismiss();
    }

    public interface UserPhotoOriginSelectedListener {

        void selectedAblum();

        void selectedQuPhtot();
    }


}
