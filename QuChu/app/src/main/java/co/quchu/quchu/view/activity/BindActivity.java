package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.ConfirmDialogFg;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.thirdhelp.UserLoginListener;
import co.quchu.quchu.thirdhelp.WechatHelper;
import co.quchu.quchu.thirdhelp.WeiboHelper;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;

public class BindActivity extends BaseActivity implements UserLoginListener {

    @Bind(R.id.bind_sina)
    Button bindSina;
    @Bind(R.id.bind_wecha)
    Button bindWecha;

    public static final String TYPE_WEIBO = "weibo";
    public static final String TYPE_Wecha = "weixin";
    @Bind(R.id.title_back_iv)
    ImageView titleBackIv;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
        ButterKnife.bind(this);
        initListener();
        titleContentTv.setText("绑定社交账号");
    }

    private void initListener() {
        bindSina.setOnClickListener(this);
        bindWecha.setOnClickListener(this);
        titleBackIv.setOnClickListener(this);
        if (AppContext.user.isIsweixin()) {
            bindWecha.setText("取消绑定我的微信");
        }
        if (AppContext.user.isIsweibo()) {
            bindSina.setText("取消绑定我的微博");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_iv:
                finish();
                break;
            case R.id.bind_wecha:
                if (AppContext.user.isIsweixin()) {
                    unBind(TYPE_Wecha);
                } else {
                    WechatHelper.isBind = true;
                    WechatHelper helper = new WechatHelper(this, this);
                    helper.bind();
                }
                break;
            case R.id.bind_sina:
                if (AppContext.user.isIsweibo()) {
                    unBind(TYPE_WEIBO);
                } else {
                    new WeiboHelper(this, this).weiboLogin(this, false);
                }
                break;
        }
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


    @Override
    public void loginSuccess(int type, String token, String appId) {
        switch (type) {
            case 2://微信
                bind(true, token, appId);
                break;
            case 3://微bo
                bind(false, token, appId);
                break;
        }
    }

    private void merger(final int type, final String token, final String appId) {


        final ConfirmDialogFg dialogFg = ConfirmDialogFg.newInstance();
        dialogFg.setTitleString("合并数据");
        dialogFg.setBody("是否将两个账号数据合并 ");
        dialogFg.setActionListener(new ConfirmDialogFg.OnActionListener() {
            @Override
            public void onClick(int index) {
                dialogFg.dismiss();
                if (index == 1) {
                    Map<String, String> params = new HashMap<>();
                    params.put("open_type", type + "");
                    params.put("open_token", token);
                    params.put("open_id", appId);
                    params.put("token", SPUtils.getUserToken(BindActivity.this));

                    LogUtils.e("open_type:" + type + "open_token:" + token + "open_id:" + appId);

                    GsonRequest request = new GsonRequest<>(Request.Method.POST, NetApi.accoundMerger, params, Object.class, new ResponseListener<Object>() {
                        @Override
                        public void onErrorResponse(@Nullable VolleyError error) {
                            Toast.makeText(BindActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Object response, boolean result, @Nullable String exception, @Nullable String msg) {
                            Toast.makeText(BindActivity.this, "合并成功", Toast.LENGTH_SHORT).show();

                        }
                    });
                    request.start(AppContext.mContext, null);
                }
            }
        });
        dialogFg.show(getFragmentManager(), null);


    }

    private void bind(final boolean isWecha, final String token, final String appId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("openId", appId);
        params.put("type", "bind");
        params.put("accesstoken", SPUtils.getUserToken(this));

        GsonRequest<Object> request = new GsonRequest<>(isWecha ? NetApi.WechatBind : NetApi.WeiboBind, Object.class, params, new ResponseListener<Object>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                Toast.makeText(BindActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, boolean isNull, @Nullable String exception, @Nullable String msg) {
                if ("已被绑定".equals(msg)) {
                    Toast.makeText(BindActivity.this, "绑定失败", Toast.LENGTH_SHORT).show();
                } else {
                    AppContext.user.setIsweixin(true);
                    // TODO: 2016/4/16    提醒数据绑定
                    merger(isWecha ? 2 : 3, token, appId);
                }

            }
        });
        request.start(this, null);
    }


    private void unBind(final String type) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("accesstoken", SPUtils.getUserToken(this));

        GsonRequest<Object> request = new GsonRequest<>(NetApi.unBindThrid, Object.class, params, new ResponseListener<Object>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                Toast.makeText(BindActivity.this, "解绑失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, boolean result, @Nullable String exception, @Nullable String msg) {
                if (type.endsWith(TYPE_Wecha)) {
                    bindWecha.setText("绑定我的微信");
                } else {
                    bindSina.setText("绑定我的微博");
                }
            }
        });
        request.start(this, null);
    }
}
