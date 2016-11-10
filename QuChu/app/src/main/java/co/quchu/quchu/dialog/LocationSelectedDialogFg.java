package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
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
import co.quchu.quchu.dialog.adapter.LocationSelectedAdapter;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;

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
    private OnDissMissListener mOnDissMissListener;

    public void setOnDissMissListener(OnDissMissListener pOnDissMissListener){
        mOnDissMissListener = pOnDissMissListener;
    }


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
        CityModel current = null;
        for (int i = 0; i < cityList.size(); i++) {
            if (SPUtils.getCityName().equals(cityList.get(i).getCvalue())){
                current  = cityList.get(i);
                cityList.remove(i);
            }
        }
        if (null!=current){
            cityList.add(0,current);
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (null!= mOnDissMissListener){
            mOnDissMissListener.onDissMiss();
        }
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
        View imageView = view.findViewById(R.id.ivClose);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });

        dialogLocationRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new LocationSelectedAdapter(cityList, getActivity(), new LocationSelectedAdapter.OnItemSelectedListener() {
            @Override
            public void onSelected(String cityName, int CityId) {
                //保存数据 而后关闭
                SPUtils.setCityId(CityId);
                SPUtils.setCityName(cityName);
                EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_NEW_CITY_SELECTED));
//                if (getActivity() instanceof RecommendActivity)
//                    ((RecommendActivity) getActivity()).updateRecommend();
                dismiss();
            }
        });
        dialogLocationRv.setAdapter(adapter);
        tvBottomTips.setVisibility(View.VISIBLE);
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


    public interface OnDissMissListener{
        void onDissMiss();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
