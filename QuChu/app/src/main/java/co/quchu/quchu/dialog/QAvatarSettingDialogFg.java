package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.blurdialogfragment.BlurDialogFragment;
import co.quchu.quchu.view.activity.AccountSettingActivity;
import co.quchu.quchu.view.adapter.SettingQAvatarGridAdapter;

/**
 * LocationSelectedDialogFg
 * User: Chenhs
 * Date: 2015-12-23
 * 城市选择弹窗
 */
public class QAvatarSettingDialogFg extends BlurDialogFragment {
    private static final String CITY_LIST_MODEL = "image_list";
    @Bind(R.id.dialog_location_selected_city_tv)
    TextView dialogLocationSelectedCityTv;
    @Bind(R.id.dialog_location_rv)
    GridView dialogLocationRv;

    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @param imageList
     * @return well instantiated fragment.
     * Serializable cityList
     */
    public static QAvatarSettingDialogFg newInstance(ArrayList<String> imageList) {
        QAvatarSettingDialogFg fragment = new QAvatarSettingDialogFg();
        Bundle args = new Bundle();
        args.putSerializable(CITY_LIST_MODEL, imageList);
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<String> imageList = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        imageList = (ArrayList<String>) args.getSerializable(CITY_LIST_MODEL);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private SettingQAvatarGridAdapter gAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_q_avatar_selected, null);
        ButterKnife.bind(this, view);
        gAdapter = new SettingQAvatarGridAdapter(getActivity(), imageList);
        dialogLocationRv.setAdapter(gAdapter);
        dialogLocationRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (imageList != null) {
                    ((AccountSettingActivity) getActivity()).updateAvatar(imageList.get(position));
                }
                QAvatarSettingDialogFg.this.dismiss();
            }
        });
        builder.setView(view);
        return builder.create();
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
