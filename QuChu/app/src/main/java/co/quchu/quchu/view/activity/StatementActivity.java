package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;



import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.dialog.ConfirmDialogFg;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.presenter.VersionInfoPresenter;

/**
 * AboutUsActivity
 * User: 通用文字说明
 * Date: 2016-01-12
 */
public class StatementActivity extends BaseBehaviorActivity {

    @Override
    public ArrayMap<String, Object> getUserBehaviorArguments() {
        return null;
    }

    @Override
    public int getUserBehaviorPageId() {
        return 128;
    }


    @Override
    protected String getPageNameCN() {
        return getString(R.string.pname_about_us);
    }

    @Bind(R.id.textView)
    TextView textView;
    int mClickTimes = 0;

    public static final String REQUEST_KEY_TITLE = "title";
    public static final String REQUEST_KEY_CONTENT = "content";
    @Bind(R.id.version)
    TextView version;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        getEnhancedToolbar();
        version.setText(AppContext.packageInfo.versionName);

        //toolbar.getTitleTv().setText(getIntent().getStringExtra(REQUEST_KEY_TITLE));
        textView.setText(getIntent().getStringExtra(REQUEST_KEY_CONTENT));
        getEnhancedToolbar().getRightTv().setText(" ");
        getEnhancedToolbar().getTitleTv().setText(R.string.about_us);
        getEnhancedToolbar().getRightIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickTimes += 1;
                if (mClickTimes >= 5) {
                    mClickTimes = 0;
                    getVersionInfo();
                }
                textView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mClickTimes = 0;
                    }
                }, 2000);
            }
        });

    }

    private ConfirmDialogFg instance;

    public void getVersionInfo() {

        VersionInfoPresenter.getVersionInfo(getApplicationContext(), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if (instance != null) {
                    instance.dismiss();
                }
                try {
                    instance = ConfirmDialogFg.newInstance("版本信息:", "SER_VN:" + response.getString("version") +
                            "\nAPP_VN:" + AppContext.packageInfo.versionName + "\nAPP_VC:" + AppContext.packageInfo.versionCode);
                    instance.setCancelable(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                instance.setCancelable(false);
                instance.show(getSupportFragmentManager(), "");
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }



    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
