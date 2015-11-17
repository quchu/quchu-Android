package co.quchu.quchu.analysis;

import android.content.Context;

import co.quchu.quchu.utils.AppUtil;

/**
 * AppAnalysisModel
 * User: Chenhs
 * Date: 2015-11-17
 */
public class AppAnalysisModel {


    private Context mContext;
    public String usertoken = "";
    public String platformid = "ANDROID";
    public String version = "";

    public AppAnalysisModel(Context mContext) {
        this.mContext = mContext;
        version = AppUtil.getVerName(mContext);
    }

    @Override
    public String toString() {
        return "{usertoken:"+usertoken+",platformid:"+platformid+",version:"+version+"}";
    }
}
