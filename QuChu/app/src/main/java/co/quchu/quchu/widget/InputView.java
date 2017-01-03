package co.quchu.quchu.widget;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.utils.SoftInputUtils;

/**
 * Created by W.b on 2017/1/3.
 */
public class InputView extends LinearLayout {

  @Bind(R.id.inputEditText) EditText mInputEditText;
  @Bind(R.id.submitBtn) TextView mSubmitBtn;

  public InputView(Context context) {
    this(context, null);
  }

  public InputView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public InputView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    LayoutInflater.from(context).inflate(R.layout.view_common_input, this);

    ButterKnife.bind(this);

    mInputEditText.addTextChangedListener(textChangedListener);
  }

  public void init() {
    mInputEditText.setText("");
    hideSoftInput();
  }

  public void hideSoftInput() {
    SoftInputUtils.hideSoftInput((Activity) getContext());
  }

  private TextWatcher textChangedListener = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      if (s.toString().trim().length() > 0) {
        mSubmitBtn.setEnabled(true);
      } else {
        mSubmitBtn.setEnabled(false);
      }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
  };

  @OnClick(R.id.submitBtn)
  public void onClick() {
    if (mListener != null) {
      mListener.onClick(mInputEditText.getText().toString());
    }
  }

  public OnInputViewClickListener mListener;

  public void setOnInputViewClickListener(OnInputViewClickListener listener) {
    mListener = listener;
  }

  public interface OnInputViewClickListener {
    void onClick(String inputStr);
  }
}
