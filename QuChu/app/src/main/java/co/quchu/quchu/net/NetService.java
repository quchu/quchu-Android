package co.quchu.quchu.net;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import co.quchu.quchu.analysis.GatherDataModel;
import co.quchu.quchu.base.ActManager;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.dialog.NetErrorDialog;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.SearchActivity;
import co.quchu.quchu.view.activity.UserLoginActivity;

/**
 * NetService
 * User: Chenhs
 * Date: 2015-10-30
 * 网络请求框架的进一步封装
 * 对返回数据初步处理
 */

public class NetService {

    //    private static MaterialDialog dialog;
    public static RequestQueue mRequestQueue = Volley
            .newRequestQueue(AppContext.mContext, new OkHttpStack(new OkHttpClient()));

    //    ,new OkHttpStack()
    public static void addRequest(Request request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

    public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    public static void post(Context cont, String pUrl, JSONObject params,
                            IRequestListener pListener) {

        if (!NetUtil.isNetworkConnected(AppContext.mContext)) {
            NetErrorDialog.showProgess(cont);
        } else {
            try {
                if (AppContext.gatherDataModel != null && (AppContext.gatherDataModel.collectList.size() > 0 || AppContext.gatherDataModel.rateList.size() > 0 || AppContext.gatherDataModel.viewList.size() > 0)) {
                    if (params == null)
                        params = new JSONObject();
                    params.put("user_data", AppContext.gatherDataModel);
                    AppContext.gatherDataModel = new GatherDataModel();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            addToQueue(Request.Method.POST, pUrl, params, pListener, 0);
        }
    }

    public static void get(Context cont, String pUrl, IRequestListener pListener) {
        if (!NetUtil.isNetworkConnected(AppContext.mContext)) {
            NetErrorDialog.showProgess(cont);
        } else {

            addToQueue(Request.Method.GET, pUrl, null, pListener, 0);
        }
    }

    public static void get(Context cont, String pUrl, JSONObject params, IRequestListener pListener) {
        if (!NetUtil.isNetworkConnected(AppContext.mContext)) {
            NetErrorDialog.showProgess(cont);
        } else {
//            dialog=    new MaterialDialog.Builder(ActManager.getAppManager().currentActivity())
//                    .theme(Theme.DARK)
//                    .content("网络数据加载中...")
//                    .progress(true, 0).autoDismiss(false)
//                    .contentGravity(GravityEnum.CENTER)
//                    .show();
            addToQueue(Request.Method.GET, pUrl, params, pListener, 0);
        }
        new HashMap<String, String>();
    }


    private static void addToQueue(int pMethod, String pUrl, JSONObject params,
                                   final IRequestListener pListener, int tag) {
        if (params == null) {
            params = new JSONObject();
        }
        HttpRequest req = new HttpRequest(pMethod, pUrl, params,
                newResponseListener(tag, pListener), newErrorListener(tag,
                pListener));
        req.setRetryPolicy(new DefaultRetryPolicy(6 * 1000, 1, 1.0f));
        addRequest(req, tag);
    }

    private static Response.Listener<JSONObject> newResponseListener(
            final int tag, final IRequestListener pListener) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                if (dialog != null) {
//                    dialog.dismiss();
//                }
                boolean result = false;
                LogUtils.json("NetService==" + response.toString());
                if (response.has("access_token") && response.has("openid")) {
                    pListener.onSuccess(response);
                } else if (response.has("result")) {
                    try {
                        //   LogUtils.json("Response-Listener===" + response.getString("data").toString() + "//isnull==" + StringUtils.isEmpty(response.getString("data").toString()) + "//isnull==" + response.getString("data").equals("null"));
                        result = response.getBoolean("result");
                        if (null != pListener) {
                            if (result) {
                                if (response.has("data") && !StringUtils.isEmpty(response.getString("data")) && !"null".equals(response.getString("data").toString())) {
                                    if (response.has("exception") && "分类列表".equals(response.getString("exception"))) {
                                        pListener.onSuccess(response);
                                    } else {
                                        pListener.onSuccess(response.getJSONObject("data"));
                                    }
                                } else {
                                    pListener.onSuccess(response);

                                }
                            } else {
                                if (ActManager.getAppManager().currentActivity() instanceof SearchActivity) {
                                    if (response.has("msg") && !StringUtils.isEmpty(response.getString("msg")))
                                        pListener.onError(response.getString("msg"));
                                } else {
                                    if (response.has("msg") && response.has("exception") && "error".equals(response.getString("msg")) && !StringUtils.isEmpty(response.getString("exception")) && "登录名已存在".equals(response.getString("exception"))) {
                                        pListener.onSuccess(response);
                                    } else if (response.has("msg") && response.has("exception") && "1077".equals(response.getString("msg"))) {
                                        ActManager.getAppManager().finishActivitiesAndKeepLastOne();
                                        SPUtils.clearUserinfo(AppContext.mContext);
                                        AppContext.user = null;
                                        ActManager.getAppManager().currentActivity().startActivity(new Intent(ActManager.getAppManager().currentActivity(), UserLoginActivity.class));
                                    } else {
                                        if (response.has("data") && !StringUtils.isEmpty(response.getString("data")) && !"null".equals(response.getString("data").toString())) {
                                            Toast.makeText(AppContext.mContext, response.getJSONObject("data").getString("error"), 0).show();
                                            if (response.has("msg") && response.has("exception") && StringUtils.isEmpty(response.getString("exception")) && !StringUtils.isEmpty(response.getString("msg"))) {
                                                pListener.onError(response.getString("msg"));
                                            }
                                        } else {
                                            if (response.has("exception") && !StringUtils.isEmpty(response.getString("exception")) && "已有记录".equals(response.getString("exception"))) {
                                                Toast.makeText(AppContext.mContext, "已有记录", Toast.LENGTH_SHORT).show();
                                            } else {

                                                Toast.makeText(AppContext.mContext, "网络出错", 0).show();
                                            }
                                        }
                                        pListener.onError(response.toString());

                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtils.json("NetService JSONException" + e);
                    }
                }

            }
        };
    }

    private static Response.ErrorListener newErrorListener(final int tag,
                                                           final IRequestListener pListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
//                if (dialog != null) {
//                    dialog.dismiss();
//                }
                DialogUtil.dismissProgessDirectly();
                if (error != null && error.networkResponse != null) {
                    pListener.onError(error.toString());
                    if (error.networkResponse.statusCode == 401) {
                    }
                }
            }
        };
    }

}
