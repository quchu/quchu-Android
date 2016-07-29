package co.quchu.quchu.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.AppUtil;
import co.quchu.quchu.utils.StringUtils;

/**
 * FeedbackActivity
 * User: Chenhs
 * Date: 2015-12-04
 * 快速反馈
 */
public class FeedbackActivity extends BaseBehaviorActivity {

    @Override
    public ArrayMap<String, Object> getUserBehaviorArguments() {
        return null;
    }

    @Override
    public int getUserBehaviorPageId() {
        return 127;
    }


    @Override
    protected String getPageNameCN() {
        return getString(R.string.pname_feedback);
    }

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
        EnhancedToolbar toolbar = getEnhancedToolbar();
        feedbackHintTv.setText(String.format(feedBackStr, AppUtil.getVerName(this)));

        TextView textView = toolbar.getRightTv();
        textView.setText("提交");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StringUtils.isEmpty(feedbackEditerBet.getText().toString())) {
                    Toast.makeText(FeedbackActivity.this, "请填写您在使用趣处过程中产生的任何疑问、反馈或建议", Toast.LENGTH_SHORT).show();
                } else {
                    NetService.post(FeedbackActivity.this, String.format(NetApi.FeedBack, feedbackEditerBet.getText().toString()), null, new IRequestListener() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Toast.makeText(FeedbackActivity.this, "感谢您的反馈!", Toast.LENGTH_SHORT).show();
                            FeedbackActivity.this.finish();
                        }

                        @Override
                        public boolean onError(String error) {
                            Toast.makeText(FeedbackActivity.this,getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });
                }
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        feedbackEditerBet.requestFocus();

        feedbackEditerBet.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(feedbackEditerBet, 0);
            }
        }, 200);
    }
}
