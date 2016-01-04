package co.quchu.quchu.analysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.utils.SPUtils;

/**
 * GatherCollectModel
 * User: Chenhs
 * Date: 2015-12-29
 * 数据采集 集合
 */
public class GatherDataModel implements Serializable {


    /**
     * collectList : []
     * platformId : IOS
     * rateList : []
     * userProperties : {}
     * userToken : h22nsd2dsd3
     * version : 1.2
     * viewList : []
     */

    public static final String platformId = "ANDROID";
    public GatherCityModel userProperties;
    public String userToken = "";
    public String version = "";
    public List<GatherCollectModel> collectList;
    public List<GatherRateModel> rateList;
    public List<GatherViewModel> viewList;

    public GatherDataModel() {
        userProperties = new GatherCityModel(SPUtils.getCityId());
        userToken = SPUtils.getUserToken(AppContext.mContext);
        collectList = new ArrayList<GatherCollectModel>();
        rateList = new ArrayList<GatherRateModel>();
        viewList = new ArrayList<GatherViewModel>();
    }

    public void setCityId(int cityId) {
        userProperties = new GatherCityModel(cityId);
    }
}
