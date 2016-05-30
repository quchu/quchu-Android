package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tencent.tauth.Tencent;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.dialog.adapter.DialogAccountSettingAdapter;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.utils.KeyboardUtils;

/**
 * ShareDialogFg
 * User: Chenhs
 * Date: 2015-12-23
 */
public class ASUserPhotoDialogFg extends DialogFragment implements AdapterView.OnItemClickListener {
    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    @Bind(R.id.dialog_share_gv)
    GridView dialogShareGv;

    private ArrayList<CityModel> cityList;

    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @return well instantiated fragment.
     * Serializable cityList
     */
    public static ASUserPhotoDialogFg newInstance() {
        ASUserPhotoDialogFg fragment = new ASUserPhotoDialogFg();
        return fragment;
    }

    private int shareId = 0;
    private String shareTitle = "";
    private boolean isPlace = false;
    Tencent mTencent;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_setting, null);
        ButterKnife.bind(this, view);
        dialogShareGv.setAdapter(new DialogAccountSettingAdapter(getActivity()));
        dialogShareGv.setOnItemClickListener(this);
        shareUrl = String.format(isPlace ? NetApi.sharePlace : NetApi.sharePostCard, shareId);
        builder.setView(view);
        return builder.create();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (position) {
            case 0:
                listener.selectedCamare();
                break;
            case 1:
                listener.selectedAblum();
                break;
            case 2:
                listener.selectedQuPhtot();
                break;
        }
        ASUserPhotoDialogFg.this.dismiss();
    }

    public interface UserPhotoOriginSelectedListener {
        void selectedCamare();

        void selectedAblum();

        void selectedQuPhtot();
    }


}
