package co.quchu.quchu.net;

import android.content.Context;
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

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;

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


        } else {
            addToQueue(Request.Method.POST, pUrl, params, pListener, 0);
        }
    }

    public static void get(Context cont, String pUrl, IRequestListener pListener) {
        if (!NetUtil.isNetworkConnected(AppContext.mContext)) {
        } else {
            addToQueue(Request.Method.GET, pUrl, null, pListener, 0);
        }
    }

    public static void get(Context cont, String pUrl, JSONObject params, IRequestListener pListener) {
        if (!NetUtil.isNetworkConnected(AppContext.mContext)) {
        } else {
//            dialog=    new MaterialDialog.Builder(ActManager.getAppManager().currentActivity())
//                    .theme(Theme.DARK)
//                    .content("网络数据加载中...")
//                    .progress(true, 0).autoDismiss(false)
//                    .contentGravity(GravityEnum.CENTER)
//                    .show();
            LogUtils.json(pUrl);
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
                                    pListener.onSuccess(response.getJSONObject("data"));
                                } else {
                                    pListener.onSuccess(response);

                                }
                            } else {
                                if (response.has("msg") && response.has("exception") && "error".equals(response.getString("msg")) && "登录名已存在".equals(response.getString("exception"))) {
                                    pListener.onSuccess(response);
                                } else {
                                    if (response.has("data") && !StringUtils.isEmpty(response.getString("data")) && !"null".equals(response.getString("data").toString())) {
                                        Toast.makeText(AppContext.mContext, response.getJSONObject("data").getString("error"), 0).show();
                                    } else {
                                        Toast.makeText(AppContext.mContext, "网络出错", 0).show();
                                    }
                                    pListener.onError(response.toString());
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
                if (error != null && error.networkResponse != null) {
                    pListener.onError(error.toString());
                    if (error.networkResponse.statusCode == 401) {
                    }
                }
            }
        };
    }

}
