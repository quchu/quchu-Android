package co.quchu.quchu.analysis;

import android.content.Context;

import org.json.JSONObject;

import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;

/**
 * UserAnalysisUtils
 * User: Chenhs
 * Date: 2015-11-17
 * <p/>
 * 用户数据统计工具类
 * pages:页面动作集合
 * page属性说明：{
 * page：页面名称
 * startTime：开始时间
 * endTime：结束时间
 * even 事件集合：{
 * eventname：事件名（click、longclick、……）
 * eventvalue：事件所记录的操作key-value集合
 * }
 */
public class UserAnalysisUtils {

    public static void sendUserBehavior(Context mContext){
        NetService.post(mContext, NetApi.userBehavior, null, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }

}
