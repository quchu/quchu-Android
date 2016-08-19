package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppLocationListener;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.dialog.adapter.LocationSelectedAdapter;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;

/**
 * Created by mwb on 16/8/19.
 */
public class CityFragment extends BaseFragment {

    private static final String CITY_LIST_MODEL = "city_list_model";
    private static final String CITY_LIST_TYPE = "city_list_type";
    private ArrayList<CityModel> cityList;
    private int cityType;

    @Bind(R.id.location_city_tv)
    TextView locationCityTv;
    @Bind(R.id.city_recycler_view)
    RecyclerView recyclerView;

    public static CityFragment newInstance(ArrayList<CityModel> list, int cityType) {
        CityFragment cityFragment = new CityFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CITY_LIST_MODEL, list);
        bundle.putInt(CITY_LIST_TYPE, cityType);
        cityFragment.setArguments(bundle);
        return cityFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        ButterKnife.bind(this, view);

        cityList = (ArrayList<CityModel>) getArguments().getSerializable(CITY_LIST_MODEL);
        cityType = getArguments().getInt(CITY_LIST_TYPE);

        initLocation();

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        LocationSelectedAdapter selectedAdapter = new LocationSelectedAdapter(cityList, null, getActivity(), new LocationSelectedAdapter.OnItemSelectedListener() {
            @Override
            public void onSelected(String cityName, int cityId) {
                //保存数据 而后关闭
                SPUtils.setCityId(cityId);
                SPUtils.setCityName(cityName);
                EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_NEW_CITY_SELECTED));
                getActivity().finish();

                //选择城市后城市列表重新排序
//                if (getActivity() instanceof RecommendActivity)
//                    ((RecommendActivity) getActivity()).updateRecommend();
            }
        });
        recyclerView.setAdapter(selectedAdapter);

        return view;
    }

    /**
     * 设置当前定位的城市
     */
    private void initLocation() {
        if (cityType == SelectedCityActivity.TYPE_CHINA) {
            locationCityTv.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(AppLocationListener.currentCity)) {
                String currentLocation = AppLocationListener.currentCity;
                locationCityTv.append(currentLocation);
            }

        } else {
            locationCityTv.setVisibility(View.GONE);
        }
    }

    @Override
    protected String getPageNameCN() {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
