package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.ConfirmDialogFg;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.presenter.VersionInfoPresenter;

/**
 * AboutUsActivity
 * User: Chenhs
 * Date: 2016-01-12
 */
public class AboutUsActivity extends BaseActivity {


    @Bind(R.id.textView)
    TextView textView;
    int mClickTimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        EnhancedToolbar toolbar = getEnhancedToolbar();
        toolbar.getTitleTv().setText(getTitle());

        String about = String.format(Locale.CHINA, getString(R.string.about_us_text), AppContext.packageInfo.versionName);
        textView.setText(about);


        getEnhancedToolbar().getRightTv().setText(" ");
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                instance.setCancelable(false);
                instance.show(getFragmentManager(), "");
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
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AboutUsActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AboutUsActivity");
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
