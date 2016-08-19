package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.widget.NoScrollViewPager;

/**
 * 选择城市
 * <p/>
 * Created by mwb on 16/8/19.
 */
public class SelectedCityActivity extends BaseBehaviorActivity {

    private static final String CITY_LIST_MODEL = "city_list_model";
    public static int TYPE_CHINA = 1; // 国内
    public static int TYPE_INTERNATIONAL = 2; // 国外
    private ArrayList<CityModel> allCityList;
    private ArrayList<CityModel> chinaCity;
    private ArrayList<CityModel> internationalCity;

    @Bind(R.id.selected_city_tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.selected_city_viewpager)
    NoScrollViewPager viewpager;

    public static void launch(Activity activity, ArrayList<CityModel> list) {
        Intent intent = new Intent(activity, SelectedCityActivity.class);
        intent.putExtra(CITY_LIST_MODEL, list);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_city);
        ButterKnife.bind(this);

        EnhancedToolbar toolbar = getEnhancedToolbar();
        TextView titleTv = toolbar.getTitleTv();
        titleTv.setText("选择城市");

        allCityList = (ArrayList<CityModel>) getIntent().getSerializableExtra(CITY_LIST_MODEL);
        chinaCity = new ArrayList<>();
        internationalCity = new ArrayList<>();
        if (allCityList != null && allCityList.size() > 0) {
            for (CityModel model : allCityList) {
                if (!TextUtils.isEmpty(model.getIsInland()) && model.getIsInland().equals("1")) {
                    chinaCity.clear();
                    chinaCity.add(model);
                } else {
                    internationalCity.clear();
                    internationalCity.add(model);
                }
            }
        }

        CityPagerAdapter adapter = new CityPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
    }

    private class CityPagerAdapter extends FragmentPagerAdapter {

        public CityPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return CityFragment.newInstance(allCityList, TYPE_CHINA);
            }
            return CityFragment.newInstance(allCityList, TYPE_INTERNATIONAL);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "国内";
            }
            return "国外";
        }
    }

    @Override
    protected int activitySetup() {
        return 0;
    }

    @Override
    protected String getPageNameCN() {
        return "选择城市";
    }

    @Override
    public ArrayMap<String, Object> getUserBehaviorArguments() {
        return null;
    }

    @Override
    public int getUserBehaviorPageId() {
        return 0;
    }
}
