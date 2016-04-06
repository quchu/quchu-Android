package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.net.AccoundMegerHelp;
import co.quchu.quchu.thirdhelp.UserLoginListener;
import co.quchu.quchu.thirdhelp.WechatHelper;
import co.quchu.quchu.thirdhelp.WeiboHelper;

public class BindActivity extends BaseActivity implements UserLoginListener {

    @Bind(R.id.bind_sina)
    Button bindSina;
    @Bind(R.id.bind_wecha)
    Button bindWecha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
        ButterKnife.bind(this);
        initListener();
    }

    private void initListener() {
        bindSina.setOnClickListener(this);
        bindWecha.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_wecha:
                new WechatHelper(this, this).login();
                break;
            case R.id.bind_sina:
                new WeiboHelper(this, this).weiboLogin(this);
                break;
        }
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


    @Override
    public void loginSuccess(int type, String token, String appId) {
        new AccoundMegerHelp().merger(type, token, appId);

    }
}
