package co.quchu.quchu.view.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import co.quchu.quchu.net.NetUtil;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.dialog.adapter.LocationSelectedAdapter;
import co.quchu.quchu.gallery.utils.Utils;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;

/**
 * 城市列表
 * <p/>
 * Created by mwb on 16/8/19.
 */
public class CityFragment extends BaseFragment {

    private static final String CITY_LIST_MODEL = "city_list_model";
    private static final String CITY_LIST_TYPE = "city_list_type";
    private ArrayList<CityModel> cityList;
    private int cityType;

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

        int selectedIndex = -1;
        for (int i = 0; i < cityList.size(); i++) {
            if (cityList.get(i).getCid()==SPUtils.getCityId()){
                selectedIndex = i;
            }
        }
        if (selectedIndex!=-1){
            CityModel cityModel = cityList.get(selectedIndex);
            cityList.remove(selectedIndex);
            cityList.add(0,cityModel);
        }

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        LocationSelectedAdapter selectedAdapter = new LocationSelectedAdapter(cityList, null, getActivity(), new LocationSelectedAdapter.OnItemSelectedListener() {
            @Override
            public void onSelected(String cityName, int cityId) {
//                if (getActivity() instanceof RecommendActivity)
//                    ((RecommendActivity) getActivity()).updateRecommend();

                if (!NetUtil.isNetworkConnected(getActivity())){
                    Toast.makeText(getActivity(),R.string.network_error,Toast.LENGTH_SHORT).show();
                }else{
                    //保存数据 而后关闭
                    SPUtils.setCityId(cityId);
                    SPUtils.setCityName(cityName);
                    EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_NEW_CITY_SELECTED));
                    getActivity().finish();
                }
            }
        });
        recyclerView.addItemDecoration(new MyItemDecoration());
        recyclerView.setAdapter(selectedAdapter);

        return view;
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

    /**
     * 列表item间距
     */
    private class MyItemDecoration extends RecyclerView.ItemDecoration {

        private final int space;

        public MyItemDecoration() {
            space = Utils.dip2px(getActivity(), 19);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            int itemCount = parent.getAdapter().getItemCount();
            int position = parent.getChildAdapterPosition(view);
            if (position < 3) {
                outRect.top = space;
            }
            outRect.bottom = space;

            if (position % 3 == 0) {
                outRect.left = space;
                outRect.right = space / 2;
            } else if ((position + 1) % 3 == 0) {
                outRect.left = space / 2;
                outRect.right = space;
            } else {
                outRect.left = space / 2;
                outRect.right = space / 2;
            }
        }
    }
}
