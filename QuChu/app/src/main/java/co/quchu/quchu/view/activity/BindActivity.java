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
                    new WechatHelper(this, this).login();
                }
                break;
            case R.id.bind_sina:
                if (AppContext.user.isIsweibo()) {
                    unBind(TYPE_WEIBO);
                } else {
                    new WeiboHelper(this, this).weiboLogin(this);
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
        Map<String, String> params = new HashMap<>();

        params.put("open_type", type + "");
        params.put("open_token", token);
        params.put("open_id", appId);
        params.put("token", SPUtils.getUserToken(this));

        LogUtils.e("open_type:" + type + "open_token:" + token + "open_id:" + appId);

        GsonRequest request = new GsonRequest<>(Request.Method.POST, NetApi.accoundMerger, params, Object.class, new ResponseListener<Object>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                Toast.makeText(BindActivity.this, "绑定失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, boolean result, @Nullable String exception, @Nullable String msg) {
                Toast.makeText(BindActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();

            }
        });
        request.start(AppContext.mContext, null);
    }

    private void unBind(final String type) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
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
