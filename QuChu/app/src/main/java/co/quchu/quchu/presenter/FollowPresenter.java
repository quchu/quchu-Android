package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.quchu.quchu.model.FollowBean;
import co.quchu.quchu.model.FollowUserModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * FollowPresenter
 * User: Chenhs
 * Date: 2016-03-01
 * 关注相关
 */
public class FollowPresenter {
    public static final int TAFOLLOWING = 0x01;//TA关注的
    public static final int TAFOLLOWERS = 0x02;//关注TA的

    private PageLoadListener<List<FollowUserModel>> view;
    private Context context;

    public FollowPresenter(Context context, PageLoadListener<List<FollowUserModel>> view) {
        this.view = view;
        this.context = context;
    }

    /**
     * @param type 关注和被关注 我关注 host  关注我的 follow
     * @param head 头部信息
     */
    public void getFollow(int userId, int type, boolean head, final int pageNo) {

        String typeString = type == TAFOLLOWING ? "host" : "follow";

        String uri = String.format(Locale.CHINA, NetApi.getFollow, userId, pageNo, typeString);

        GsonRequest<FollowBean> request = new GsonRequest<>(uri, FollowBean.class, new ResponseListener<FollowBean>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                view.netError(pageNo, "");
            }

            @Override
            public void onResponse(FollowBean response, boolean result, @Nullable String exception, @Nullable String msg) {
                if (response.getUsers().getResult() == null || response.getUsers().getResult().size() == 0) {
                    view.nullData();
                } else if (pageNo == 1) {
                    view.initData(response.getUsers().getResult());
                } else {
                    view.moreData(response.getUsers().getResult());
                }
            }
        });

        request.start(context, null);
    }


    /**
     * 获取关注列表
     *
     * @param userID     用户id
     * @param followType 关注类型
     * @param pageNo     页码
     * @param callBack   请求回调
     */
    public static void getFollowsList(Context mContext, final int userID, int followType, int pageNo, final GetFollowCallBack callBack) {
        String followUrl = "";
        switch (followType) {
            case TAFOLLOWING://TA关注的
                followUrl = String.format(NetApi.getFollow, userID, pageNo, "host");
                break;
            case TAFOLLOWERS://关注TA的
                followUrl = String.format(NetApi.getFollow, userID, pageNo, "follow");
                break;
        }
        if (StringUtils.isEmpty(followUrl))
            return;
        NetService.get(mContext, followUrl, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json(response + "");
                try {
                    if (response.has("users") && !StringUtils.isEmpty(response.getString("users"))) {
                        JSONObject users = response.getJSONObject("users");

                        if (null != users && users.has("result")) {
                            JSONArray userlist = users.getJSONArray("result");
                            Gson gson = new Gson();
                            ArrayList<FollowUserModel> lists = new ArrayList<FollowUserModel>();
                            if (userlist.length() > 0) {
                                for (int i = 0; i < userlist.length(); i++) {
                                    lists.add(gson.fromJson(userlist.getString(i), FollowUserModel.class));
                                }
                                if (callBack != null)
                                    callBack.onSuccess(lists);
                            } else {
                                if (callBack != null)
                                    callBack.onError();
                            }
                        }
                    } else {
                        if (callBack != null)
                            callBack.onError();
                    }
                } catch (JSONException e) {
                    if (callBack != null)
                        callBack.onError();
                    e.printStackTrace();
                }
            }

            @Override
            public boolean onError(String error) {
                if (callBack != null)
                    callBack.onError();
                return false;
            }
        });
    }

    /**
     * 获取个人关注列表
     *
     * @param requestAdditionalInfo 是否需要头部信息？wtf is it
     * @param followType            关注类型
     * @param pageNo                页码
     * @param callBack              请求回调
     */
    public static void getCurrentUserFollowers(Context mContext, boolean requestAdditionalInfo, int followType, int pageNo, final GetFollowCallBack callBack) {
        String followUrl = "";
        String strValue = requestAdditionalInfo ? "yes" : "no";
        switch (followType) {
            case TAFOLLOWING://TA关注的
                followUrl = String.format(NetApi.getCurrentUserFollowers, strValue, "host", pageNo);
                break;
            case TAFOLLOWERS://关注TA的
                followUrl = String.format(NetApi.getCurrentUserFollowers, strValue, "follow", pageNo);
                break;
        }
        if (StringUtils.isEmpty(followUrl))
            return;
        NetService.get(mContext, followUrl, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json(response + "");
                try {
                    if (response.has("users") && !StringUtils.isEmpty(response.getString("users"))) {
                        JSONObject users = response.getJSONObject("users");

                        if (null != users && users.has("result")) {
                            JSONArray userlist = users.getJSONArray("result");
                            Gson gson = new Gson();
                            ArrayList<FollowUserModel> lists = new ArrayList<FollowUserModel>();
                            if (userlist.length() > 0) {
                                for (int i = 0; i < userlist.length(); i++) {
                                    lists.add(gson.fromJson(userlist.getString(i), FollowUserModel.class));
                                }
                                if (callBack != null)
                                    callBack.onSuccess(lists);
                            } else {
                                if (callBack != null)
                                    callBack.onError();
                            }
                        }
                    } else {
                        if (callBack != null)
                            callBack.onError();
                    }
                } catch (JSONException e) {
                    if (callBack != null)
                        callBack.onError();
                    e.printStackTrace();
                }
            }

            @Override
            public boolean onError(String error) {
                if (callBack != null)
                    callBack.onError();
                return false;
            }
        });
    }

    public interface GetFollowCallBack {
        void onSuccess(ArrayList<FollowUserModel> lists);

        void onError();
    }
}
