package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.dialog.adapter.LocationSelectedAdapter;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.activity.RecommendActivity;

/**
 * LocationSelectedDialogFg
 * User: Chenhs
 * Date: 2015-12-23
 * 城市选择弹窗
 */
public class LocationSelectedDialogFg extends DialogFragment {
    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    private static final String CITY_LIST_MODEL = "city_list_model";
//    @Bind(R.id.dialog_location_selected_city_tv)
//    TextView dialogLocationSelectedCityTv;

//    @Bind(R.id.dialog_location_submit_tv)
//    TextView dialogLocationSubmitTv;

    @Bind(R.id.dialog_location_rv)
    RecyclerView dialogLocationRv;
    @Bind(R.id.tv_bottom_tips)
    TextView tvBottomTips;

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


        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_location_selected, null);
        ButterKnife.bind(this, view);
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(view);

        initSelected();
        dialogLocationRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new LocationSelectedAdapter(cityList, null, getActivity(), new LocationSelectedAdapter.OnItemSelectedListener() {
            @Override
            public void onSelected(String cityName, int CityId) {
                //保存数据 而后关闭
                SPUtils.setCityId(CityId);
                SPUtils.setCityName(cityName);
                EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_NEW_CITY_SELECTED));
                if (getActivity() instanceof RecommendActivity)
                    ((RecommendActivity) getActivity()).updateRecommend();
                dismiss();
            }
        });
        dialogLocationRv.setAdapter(adapter);
        tvBottomTips.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return dialog;
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
