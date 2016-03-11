package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.analysis.GatherCityModel;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.blurdialogfragment.BlurDialogFragment;
import co.quchu.quchu.dialog.adapter.LocationSelectedAdapter;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.RecommendActivity;

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
    /*    @Bind(R.id.dialog_location_xm_cb)
        CheckBox dialogLocationXmCb;
        @Bind(R.id.dialog_location_hz_cb)
        CheckBox dialogLocationHzCb;*/
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
    public static LocationSelectedDialogFg newInstance(ArrayList<CityModel> list) {
        LocationSelectedDialogFg fragment = new LocationSelectedDialogFg();
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


    private LocationSelectedAdapter adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_location_selected, null);
        ButterKnife.bind(this, view);
        initSelected();
        dialogLocationRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new LocationSelectedAdapter(cityList, dialogLocationSelectedCityTv, getActivity());
        dialogLocationRv.setAdapter(adapter);
        builder.setView(view);
        dialogLocationSelectedCityTv.setText("所在城市:" + SPUtils.getCityName());
        StringUtils.alterTextColor(dialogLocationSelectedCityTv, 5, 5 + SPUtils.getCityName().length(), R.color.gene_textcolor_yellow);

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

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        if (adapter != null && dialogLocationRv != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void initSelected() {
        if (cityList != null && cityList.size() > 0) {
            for (int i = 0; i < cityList.size(); i++) {
                if (cityList.get(i).getCvalue().equals(SPUtils.getCityName())) {
                    cityList.get(i).setIsSelected(true);
                } else {
                    cityList.get(i).setIsSelected(false);
                }
            }
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

    private boolean submitClickable = true;

    @OnClick({R.id.dialog_location_submit_tv, R.id.dialog_location_cancel_tv})
    public void loacationDialogClick(View view) {

        switch (view.getId()) {
            case R.id.dialog_location_submit_tv:
                //保存数据 而后关闭
                if (submitClickable) {
                    submitClickable = false;

                    if (cityList != null) {
                        SPUtils.setCityId(cityList.get(adapter.getSelectedIndex()).getCid());
                        SPUtils.setCityName(cityList.get(adapter.getSelectedIndex()).getCvalue());
                        if (getActivity() instanceof RecommendActivity)
                            ((RecommendActivity) getActivity()).updateRecommend();
                        if (AppContext.gatherList == null)
                            AppContext.gatherList = new ArrayList<>();
                        AppContext.gatherList.add(new GatherCityModel(cityList.get(adapter.getSelectedIndex()).getCid()));
                    }
                }
            case R.id.dialog_location_cancel_tv:
                LocationSelectedDialogFg.this.dismiss();
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
