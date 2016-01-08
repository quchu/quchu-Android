package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

    @OnClick({R.id.dialog_location_submit_tv, R.id.dialog_location_cancel_tv})
    public void loacationDialogClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_location_submit_tv:
                //保存数据 而后关闭
                SPUtils.setCityId(cityList.get(adapter.getSelectedIndex()).getCid());
                SPUtils.setCityName(cityList.get(adapter.getSelectedIndex()).getCvalue());
                if (getActivity() instanceof RecommendActivity)
                    ((RecommendActivity) getActivity()).updateRecommend();

                //   AppContext.gatherList.setCityId(cityList.get(adapter.getSelectedIndex()).getCid());
                if (AppContext.gatherList == null)
                    AppContext.gatherList = new ArrayList<>();
                AppContext.gatherList.add(new GatherCityModel(cityList.get(adapter.getSelectedIndex()).getCid()));
            case R.id.dialog_location_cancel_tv:
                LocationSelectedDialogFg.this.dismiss();
                break;
        /*    case R.id.dialog_location_xm_cb:
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
                break;*/
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
