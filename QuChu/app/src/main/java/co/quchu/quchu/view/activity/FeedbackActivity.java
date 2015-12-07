package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.AppUtil;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.widget.MoreButtonView;

/**
 * FeedbackActivity
 * User: Chenhs
 * Date: 2015-12-04
 * 快速反馈
 */
public class FeedbackActivity extends BaseActivity {
    @Bind(R.id.title_back_rl)
    RelativeLayout titleBackRl;
    @Bind(R.id.title_back_iv)
    ImageView titleBackIv;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.title_more_rl)
    MoreButtonView titleMoreRl;
    @Bind(R.id.feedback_hint_tv)
    TextView feedbackHintTv;
    @Bind(R.id.feedback_editer_bet)
    EditText feedbackEditerBet;
    private String feedBackStr = "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _   \r\n请忽略此处  \r\nModel: Android  \r\nVersion: %s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        titleContentTv.setText(getTitle());
        feedbackHintTv.setText(String.format(feedBackStr, AppUtil.getVerName(this)));
        titleBackIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_menus_title_more));
        titleMoreRl.setImage(R.drawable.ic_menus_title_more);
        titleMoreRl.isNeedAnimation(false);
        titleMoreRl.setMoreClick(new MoreButtonView.MoreClicklistener() {
            @Override
            public void moreClick() {
                if (StringUtils.isEmpty(feedbackEditerBet.getText().toString())){
                    Toast.makeText(FeedbackActivity.this,"请填写您在使用趣处过程中产生的任何疑问、反馈或建议",Toast.LENGTH_SHORT).show();
                }else {
                    NetService.post(FeedbackActivity.this, String.format(NetApi.FeedBack, feedbackEditerBet.getText().toString()), null, new IRequestListener() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Toast.makeText(FeedbackActivity.this,"感谢您的反馈！",Toast.LENGTH_SHORT).show();
                            FeedbackActivity.this.finish();
                        }

                        @Override
                        public boolean onError(String error) {
                            Toast.makeText(FeedbackActivity.this,"网络连接异常，请稍后重试！",Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });
                }
            }
        });
    }


    @OnClick(R.id.title_back_rl)
    public void feedClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_rl:
                this.finish();
                break;

        }
    }
}
