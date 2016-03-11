package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;

/**
 * AboutUsActivity
 * User: Chenhs
 * Date: 2016-01-12
 */
public class AboutUsActivity extends BaseActivity {

    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.about_us_right_rl)
    RelativeLayout aboutUsRightRl;
    @Bind(R.id.about_us_title_back_rl)
    RelativeLayout aboutUsTitleBackRl;
    @Bind(R.id.about_us_title_bar_ll)
    LinearLayout aboutUsTitleBarLl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        titleContentTv.setText(getTitle());
      /*  aboutUsDescTv.setText(
                //- 趣处%1$s -\r\n带你重新认识你的城市，\r\n陪你从心定义你的生活方式。\r\n\r\n意见建议\r\nEmail：

                Html.fromHtml(
                        String.format(getResources().getString(R.string.about_us_text), AppUtil.getVerName(this)) +
                                "<font color=#f4e727><a href=\"http://service@quchu.co\">service@quchu.co</a> </font> "
                )
        );
        aboutUsDescTv.setMovementMethod(LinkMovementMethod.getInstance());*/
    }

    @OnClick({R.id.about_us_title_back_rl})
    public void aboutUsClick(View view) {
        switch (view.getId()) {
            case R.id.about_us_title_back_rl:
                this.finish();
                break;
        }
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
}
