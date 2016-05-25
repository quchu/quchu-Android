package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.BindPhoneNumDialog;
import co.quchu.quchu.dialog.CommonDialog;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.thirdhelp.UserInfoHelper;
import co.quchu.quchu.thirdhelp.UserLoginListener;
import co.quchu.quchu.thirdhelp.WechatHelper;
import co.quchu.quchu.thirdhelp.WeiboHelper;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;

public class BindActivity extends BaseActivity implements UserLoginListener, View.OnClickListener {

    @Bind(R.id.bind_sina)
    Button bindSina;
    @Bind(R.id.bind_wecha)
    Button bindWecha;
    @Bind(R.id.bind_phone)
    Button bindPhone;
    public static final String TYPE_WEIBO = "weibo";
    public static final String TYPE_Wecha = "weixin";


    private SsoHandler ssoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
        ButterKnife.bind(this);
        EnhancedToolbar toolbar = getEnhancedToolbar();

        toolbar.getTitleTv().setText("绑定第三方账号");
    }

    private void initListener() {
        bindSina.setOnClickListener(this);
        bindWecha.setOnClickListener(this);
        bindPhone.setOnClickListener(this);
        if (AppContext.user.isIsweixin()) {
            bindWecha.setText("解除绑定");
            bindWecha.setBackgroundColor(getResources().getColor(R.color.standard_color_h2_dark));
        } else {
            bindWecha.setText("绑定");
            bindWecha.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        if (AppContext.user.isIsweibo()) {
            bindSina.setText("解除绑定");
            bindSina.setBackgroundColor(getResources().getColor(R.color.standard_color_h2_dark));
        } else {
            bindSina.setText("绑定");
            bindSina.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        if (AppContext.user.isphone()) {
            bindPhone.setText("解除绑定");
            bindPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_h2_dark));
        } else {
            bindPhone.setText("绑定");
            bindPhone.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_phone:
                if (AppContext.user.isphone()) {
                    CommonDialog dialog = CommonDialog.newInstance("出错了", "手机号是唯一的身份标示,解除绑定后将无法正常登录.", "知道了", null);
                    dialog.show(getSupportFragmentManager(), "");
                } else {
                    BindPhoneNumDialog dialog = BindPhoneNumDialog.newInstance();
                    dialog.show(getSupportFragmentManager(), "");
                }
                break;
            case R.id.bind_wecha:
                if (AppContext.user.isIsweixin()) {
                    if (SPUtils.getLoginType().equals(SPUtils.LOGIN_TYPE_WEIXIN)) {
                        Toast.makeText(this, "当前登录账号不能解绑", Toast.LENGTH_SHORT).show();
                    } else {
                        unBind(true, TYPE_Wecha);
                    }
                } else {
                    WechatHelper helper = WechatHelper.getInstance(this);
                    helper.bind(this);
                }
                break;
            case R.id.bind_sina:
                if (AppContext.user.isIsweibo()) {
                    if (SPUtils.getLoginType().equals(SPUtils.LOGIN_TYPE_WEIBO)) {
                        Toast.makeText(this, "当前登录账号不能解绑", Toast.LENGTH_SHORT).show();
                    } else {
                        unBind(false, TYPE_WEIBO);
                    }
                } else {
                    WeiboHelper instance = WeiboHelper.getInstance(this);
                    ssoHandler = new SsoHandler(this, instance.getmAuthInfo());
                    instance.weiboLogin(ssoHandler, this, false);
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

    @Override
    public void loginFail(String message) {

    }

    private void merger(final int type, final String token, final String appId) {
        CommonDialog commonDialog = CommonDialog.newInstance("注意", "该账号已被使用,是否将此账号与当前账号进行合并,合并后不影响使用", "确定", "取消", "什么是账号合并");
        commonDialog.setListener(new CommonDialog.OnActionListener() {
            @Override
            public boolean dialogClick(int id) {
                switch (id) {
                    case CommonDialog.CLICK_ID_ACTIVE:
                        Map<String, String> params = new HashMap<>();
                        params.put("open_type", type + "");
                        params.put("open_token", token);
                        params.put("open_id", appId);
                        params.put("token", SPUtils.getUserToken(BindActivity.this));

                        LogUtils.e("open_type:" + type + "open_token:" + token + "open_id:" + appId);

                        GsonRequest request = new GsonRequest<>(Request.Method.POST, NetApi.accoundMerger, params, UserInfoModel.class, new ResponseListener<UserInfoModel>() {
                            @Override
                            public void onErrorResponse(@Nullable VolleyError error) {
                                Toast.makeText(BindActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(UserInfoModel response, boolean result, @Nullable String exception, @Nullable String msg) {
                                Toast.makeText(BindActivity.this, "合并成功", Toast.LENGTH_SHORT).show();
                                UserInfoHelper.saveUserInfo(response);
                                //数据不变 暂时不刷新
//                            EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_USER_INFO_UPDATA, response));
                            }
                        });
                        request.start(AppContext.mContext, null);
                        break;
                    case CommonDialog.CLICK_ID_SUBBUTTON:
                        Intent intent = new Intent(BindActivity.this, StatementActivity.class);
                        intent.putExtra(StatementActivity.REQUEST_KEY_TITLE, "什么是账号合并");
                        intent.putExtra(StatementActivity.REQUEST_KEY_CONTENT, "账号合并是将用户已经绑定的第三方账号与当前帐号的数据进行合并，合并后允许用户使用多种方式登录，个人信息和行为会被融合，例如收藏，点赞，发表的脚印等，合并后趣处将为用户更全面的推荐内容。\n" +
                                "账号合并的目的：由于人工智能算法是针对用户行为进行分析的，所以每个账号都记录了用户的行为数据，趣处认为如果用户曾经使用其他账号登录过并留下行为数据，那些数据应当被视为用户行为的一个重要组成部分被保存，所以在工程师的努力下，我们有幸能将自己的特长转化为你的愉悦体验，并将为此一直努力下去。\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "——趣处人工智能实验室");
                        startActivity(intent);
                        return false;

                }
                return true;
            }
        });
        commonDialog.setCancelable(false);
        commonDialog.show(getSupportFragmentManager(), "");

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
            public void onResponse(Object response, boolean isNull, String exception, @Nullable String msg) {
                if (isNull) {
                    Toast.makeText(BindActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
                    saveInfo(true, isWecha);
                } else {
                    if (exception.equals("10132")) {
                        saveInfo(true, isWecha);

                        merger(isWecha ? 2 : 3, token, appId);
                    } else {
                        Toast.makeText(BindActivity.this, "该账号不允许绑定", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        request.start(this, null);
    }

    /**
     * 绑定成功后更新数据
     *
     * @param isWecha 是不是绑定了微信
     */
    private void saveInfo(boolean isBind, boolean isWecha) {
        UserInfoModel user = AppContext.user;
        if (isWecha) {
            user.setIsweixin(isBind);
        } else {
            user.setIsweibo(isBind);
        }

        UserInfoHelper.saveUserInfo(user);
        if (isBind) {
            if (isWecha) {
                bindWecha.setText("取消绑定我的微信");
            } else {
                bindSina.setText("取消绑定我的微博");
            }
        } else {
            if (isWecha) {
                bindWecha.setText("绑定我的微信");
            } else {
                bindSina.setText("绑定我的微博");
            }
        }
    }

    private void unBind(final boolean isWache, final String type) {
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
                if (result) {
                    LogUtils.e("解绑的type为" + type);
                    Toast.makeText(BindActivity.this, "解绑成功", Toast.LENGTH_SHORT).show();
                    if (isWache) {
                        bindWecha.setText("绑定我的微信");
                        saveInfo(false, true);
                    } else {
                        bindSina.setText("绑定我的微博");
                        saveInfo(false, false);
                    }
                } else {
                    Toast.makeText(BindActivity.this, "该账号不允许解除绑定", Toast.LENGTH_SHORT).show();
                }
            }
        });
        request.start(this, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e("onActivityResult" + data.toString());
        if (ssoHandler != null) {
            LogUtils.e("onActivityResult:不为空");
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("connectsocial");
        initListener();
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd("connectsocial");
        super.onPause();
    }
}
