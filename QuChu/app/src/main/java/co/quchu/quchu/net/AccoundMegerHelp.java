package co.quchu.quchu.net;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import co.quchu.quchu.base.AppContext;

/**
 * Created by no21 on 2016/4/6.
 * email:437943145@qq.com
 * desc : 用户账号合并类
 */
public class AccoundMegerHelp {
    GsonRequest<Object> request = null;

    public void merger(int type, String token, String id) {


        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(id) && type != 0) {


//            ConfirmDialogFg dialogFg = ConfirmDialogFg.newInstance(R.string.accound_merger_title, R.string.accound_merger_body);
//            dialogFg.setTitleString("账号合并");
//            dialogFg.setBody("是否合并?");

            Map<String, String> params = new HashMap<>();

            params.put("open_type", type + "");
            params.put("open_token", token);
            params.put("open_id", id);

            request = new GsonRequest<>(Request.Method.POST, NetApi.accoundMerger, params, Object.class, new ResponseListener<Object>() {
                @Override
                public void onErrorResponse(@Nullable VolleyError error) {
                    request.start(AppContext.mContext, null);

                }

                @Override
                public void onResponse(Object response, boolean result, @Nullable String exception, @Nullable String msg) {

                }
            });
            request.start(AppContext.mContext, null);

        }
    }
}
