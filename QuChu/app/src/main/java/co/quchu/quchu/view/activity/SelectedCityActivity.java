package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppLocationListener;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.adapter.LocationSelectedAdapter;
import co.quchu.quchu.model.CityEntity;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.widget.SpacesItemDecoration;

/**
 * 选择城市
 * <p/>
 * Created by mwb on 16/8/19.
 */
public class SelectedCityActivity extends BaseBehaviorActivity {

  private static final String CITY_LIST_MODEL = "city_list_model";
  private ArrayList<CityModel> mCityList = new ArrayList<>();

  @Bind(R.id.location_tv) TextView mLocationTv;
  @Bind(R.id.future_city_tv) TextView mFutureCityTv;
  @Bind(R.id.city_list_bottom_layout) LinearLayout mBottomLayout;
  @Bind(R.id.city_recycler_view) RecyclerView mRecyclerView;

  public static void launch(Activity activity, CityEntity cityEntity) {
    Intent intent = new Intent(activity, SelectedCityActivity.class);
    intent.putExtra(CITY_LIST_MODEL, cityEntity);
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

    CityEntity cityEntity = (CityEntity) getIntent().getSerializableExtra(CITY_LIST_MODEL);

    //当前所在城市
    String currentCity = "";
    if (AppLocationListener.currentCity != null) {
      currentCity = AppLocationListener.currentCity;
      mLocationTv.setVisibility(View.VISIBLE);
      mLocationTv.setText("您当前所在的城市: " + currentCity);
    }

    processCityList(cityEntity);

    processFutureCity(cityEntity, currentCity);

    initRecyclerView();
  }

  private void initRecyclerView() {
    GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        if (mCityList != null) {
          if (mCityList.get(position).getGroup() == 1 || mCityList.get(position).getGroup() == 0) {
            return 3;
          }
        }
        return 1;
      }
    });
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.addItemDecoration(new SpacesItemDecoration(20, 3));
    LocationSelectedAdapter selectedAdapter = new LocationSelectedAdapter(mCityList, this, new LocationSelectedAdapter.OnItemSelectedListener() {
      @Override
      public void onSelected(String cityName, int cityId) {
        //保存数据 而后关闭
        SPUtils.setCityId(cityId);
        SPUtils.setCityName(cityName);
        EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_NEW_CITY_SELECTED));
        finish();
      }
    });
    mRecyclerView.setAdapter(selectedAdapter);
  }

  /**
   * 处理已经上线的城市列表
   */
  private void processCityList(CityEntity cityEntity) {
    if (cityEntity == null) {
      return;
    }

    //所有城市
    if (cityEntity.getPage() != null) {
      ArrayList<CityModel> allCityList = cityEntity.getPage().getResult();
      List<Integer> indexList = new ArrayList<>();
      if (allCityList != null && allCityList.size() > 0) {
        CityModel cityModel = new CityModel();
        cityModel.setGroup(1);
        cityModel.setCvalue("国内");
        mCityList.add(cityModel);
        for (int i = 0; i < allCityList.size(); i++) {
          CityModel model = allCityList.get(i);
          //国内城市
          if (!TextUtils.isEmpty(model.getIsInland()) && model.getIsInland().equals("1")) {
            mCityList.add(model);
          } else {
            indexList.add(i);
          }
        }
        //国外城市
        if (indexList.size() > 0) {
          cityModel = new CityModel();
          cityModel.setGroup(0);
          cityModel.setCvalue("国外");
          mCityList.add(cityModel);

          for (Integer index : indexList) {
            mCityList.add(allCityList.get(index));
          }
        }
      }
    }
  }

  /**
   * 处理即将上线的城市
   */
  private void processFutureCity(CityEntity cityEntity, String currentCity) {
    if (cityEntity == null) {
      return;
    }

    //当前城市,去除"市"
    String fixStr = "";
    if (!TextUtils.isEmpty(currentCity)) {
      fixStr = currentCity.endsWith("市") ? currentCity.substring(0, currentCity.length() - 1) : currentCity;
    }

    //即将上线的城市
    int startIndex = 0;
    int endIndex = 0;
    boolean futureHasCurrentCity = false;//即将上线的城市中有当前城市
    if (cityEntity.getFuture() != null) {
      String[] futureCity = cityEntity.getFuture();
      mBottomLayout.setVisibility(View.VISIBLE);

      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < futureCity.length; i++) {
        String s = futureCity[i];
        sb.append(s);
        if (i < futureCity.length - 1) {
          sb.append(", ");
        }

        if (!futureHasCurrentCity) {
          if (fixStr.equals(s)) {
            if (i + 1 == futureCity.length) {
              endIndex = sb.toString().length();
            } else {
              endIndex = sb.toString().length() - 2;
            }
            futureHasCurrentCity = true;
          } else {
            endIndex = endIndex + s.length();
            startIndex = endIndex + (i + 1) * 2;
          }
        }
      }
      SpannableString ss = new SpannableString(sb.toString());
      if (futureHasCurrentCity) {
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.standard_color_red));
        ss.setSpan(colorSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
      }
      mFutureCityTv.setText(ss);
    }
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
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
