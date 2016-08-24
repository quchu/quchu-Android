package co.quchu.quchu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.ToastManager;
import co.quchu.quchu.widget.LengthFilter;

/**
 * 用户反馈提交对话框
 * <p/>
 * Created by mwb on 16/8/23.
 */
public class FeedbackDialog extends Dialog {

    private DialogConfirmListener confirmListener;

    @Bind(R.id.title_edit)
    EditText titleEdit;
    @Bind(R.id.content_edit)
    EditText contentEdit;
    @Bind(R.id.cancel_btn)
    TextView cancelBtn;
    @Bind(R.id.submit_btn)
    TextView submitBtn;
    private boolean hasFeedbackTitle;
    private boolean hasFeedbackContent;

    public FeedbackDialog(Context context, DialogConfirmListener confirmListener) {
        super(context, R.style.loading_dialog);

        this.confirmListener = confirmListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_feedback);
        ButterKnife.bind(this);

        titleEdit.setFilters(new InputFilter[]{new LengthFilter(15)});
        contentEdit.setFilters(new InputFilter[]{new LengthFilter(140)});

        titleEdit.addTextChangedListener(titleEditWatchListener);
        contentEdit.addTextChangedListener(contentEditWatchListener);

        //自动填充最近一条未提交的反馈
        String feedback = SPUtils.getFeedback();
        if (!feedback.equals("/")) {
            titleEdit.setText(feedback.substring(0, feedback.indexOf("/")));
            contentEdit.setText(feedback.substring(feedback.indexOf("/") + 1, feedback.length()));

            submitBtn.setEnabled(true);
        }
    }

    private TextWatcher titleEditWatchListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().length() > 0) {
                hasFeedbackTitle = true;
            } else {
                hasFeedbackTitle = false;
            }

            if (hasFeedbackTitle && hasFeedbackContent) {
                submitBtn.setEnabled(true);
            } else {
                submitBtn.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private TextWatcher contentEditWatchListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().length() > 0) {
                hasFeedbackContent = true;
            } else {
                hasFeedbackContent = false;
            }

            if (hasFeedbackTitle && hasFeedbackContent) {
                submitBtn.setEnabled(true);
            } else {
                submitBtn.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @OnClick({R.id.cancel_btn, R.id.submit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                //取消
                dismiss();
                break;

            case R.id.submit_btn:
                //提交
                String title = titleEdit.getText().toString().trim();
                String content = contentEdit.getText().toString().trim();

                if (TextUtils.isEmpty(title)) {
                    ToastManager.getInstance(getContext()).show("请输入标题");
                    return;
                }

                if (TextUtils.isEmpty(content)) {
                    ToastManager.getInstance(getContext()).show("请输入内容");
                    return;
                }

                //保存最近一条反馈
                SPUtils.setFeedback(title, content);

                if (confirmListener != null) {
                    confirmListener.confirm(title, content);
                }
                break;
        }
    }

    public interface DialogConfirmListener {
        void confirm(String title, String content);
    }
}
