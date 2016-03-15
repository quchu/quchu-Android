package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.blurdialogfragment.BlurDialogFragment;
import co.quchu.quchu.dialog.adapter.LocationSelectedAdapter;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.AccountSettingActivity;

/**
 * LocationSelectedDialogFg
 * User: Chenhs
 * Date: 2015-12-23
 * 设置界面性别选择弹窗
 */
public class GenderSelectedDialogFg extends BlurDialogFragment {
    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    private static final String CITY_LIST_MODEL = "city_list_model";
    @Bind(R.id.dialog_location_selected_city_tv)
    TextView dialogLocationSelectedCityTv;
    @Bind(R.id.dialog_location_submit_tv)
    TextView dialogLocationSubmitTv;
    @Bind(R.id.dialog_location_cancel_tv)
    TextView dialogLocationCancelTv;
    @Bind(R.id.dialog_location_bottom_textviews_rl)
    RelativeLayout dialogLocationBottomTextviewsRl;
    @Bind(R.id.dialog_location_rv)
    RecyclerView dialogLocationRv;

    private ArrayList<CityModel> cityList;

    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @param list
     * @return well instantiated fragment.
     * Serializable cityList
     */
    public static GenderSelectedDialogFg newInstance(ArrayList<CityModel> list) {
        GenderSelectedDialogFg fragment = new GenderSelectedDialogFg();
        Bundle args = new Bundle();
        args.putSerializable(CITY_LIST_MODEL, list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        cityList = (ArrayList<CityModel>) args.getSerializable(CITY_LIST_MODEL);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private LocationSelectedAdapter adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_location_selected, null);
        ButterKnife.bind(this, view);
        Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(view);




        dialogLocationRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new LocationSelectedAdapter(cityList, dialogLocationSelectedCityTv, getActivity(), 1, new LocationSelectedAdapter.OnItemSelectedListener() {
            @Override
            public void onSelected() {
                dialogLocationSubmitTv.post(new Runnable() {
                    @Override
                    public void run() {
                        dialogLocationSubmitTv.performClick();
                    }
                });
            }
        });
        dialogLocationRv.setAdapter(adapter);
        if (cityList != null && cityList.size() >= 2) {
            dialogLocationSelectedCityTv.setText("设置性别:" + (cityList.get(0).isSelected() ? cityList.get(0).getCvalue() : cityList.get(1).getCvalue()));
            StringUtils.alterTextColor(dialogLocationSelectedCityTv, 5, 6, R.color.gene_textcolor_yellow);
        }

        return dialog;
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
        if (adapter != null && dialogLocationRv != null) {
            adapter.notifyDataSetChanged();
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

    private int selectedIndex = 0;
    private boolean submitClickable = true;

    @OnClick({R.id.dialog_location_submit_tv, R.id.dialog_location_cancel_tv})
    public void loacationDialogClick(View view) {

        switch (view.getId()) {
            case R.id.dialog_location_submit_tv:
                //保存数据 而后关闭
                if (submitClickable) {
                    submitClickable = false;
                    if (cityList != null) {
                        if (getActivity() instanceof AccountSettingActivity) {
                            ((AccountSettingActivity) getActivity()).updateGender(cityList.get(adapter.getSelectedIndex()).getCvalue());
                        }
                    }
                }
            case R.id.dialog_location_cancel_tv:
                GenderSelectedDialogFg.this.dismiss();
                submitClickable = true;
                break;

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
