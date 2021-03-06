package co.quchu.quchu.presenter;

import com.android.volley.VolleyError;

/**
 * Created by linqipeng@ycode.cn on 2015/11/18.
 * email:437943145@qq.com
 * desc: 通用的回调
 */
public interface CommonListener<T> {


    void successListener(T response);

    void errorListener(VolleyError error, String exception, String msg);
}
