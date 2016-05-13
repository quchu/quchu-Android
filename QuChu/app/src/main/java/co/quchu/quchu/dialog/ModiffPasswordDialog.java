package co.quchu.quchu.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.sina.weibo.sdk.utils.MD5;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;

/**
 * Created by no21 on 2016/5/12.
 * email:437943145@qq.com
 * desc :
 */
public class ModiffPasswordDialog extends DialogFragment implements View.OnClickListener {
    @Bind(R.id.originPassw)
    TextInputEditText originPassw;
    @Bind(R.id.newPassw)
    TextInputEditText newPassw;
    @Bind(R.id.commit)
    Button commit;
    @Bind(R.id.inputLayoutNewPass)
    TextInputLayout inputLayoutNewPass;


    public static ModiffPasswordDialog newInstance() {
        return new ModiffPasswordDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_modiff_password, null);
        ButterKnife.bind(this, view);
        builder.setView(view);

        commit.setOnClickListener(this);
        newPassw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newPassword = s.toString().trim();
                if (!(newPassword.length() < 6) && !(newPassword.length() > 12)) {
                    inputLayoutNewPass.setError(null);
                } else {
                    inputLayoutNewPass.setError("请输入6-12位新密码");
                }
            }
        });

        return builder.create();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.commit:
                String newPassword = newPassw.getText().toString().trim();
                String originPassword = originPassw.getText().toString().trim();
                if (originPassword.equals(newPassword)) {
                    inputLayoutNewPass.setError("新旧密码不能相同");
                    return;
                }
                if (newPassword.length() < 6 || newPassword.length() > 12) {
                    inputLayoutNewPass.setError("请输入6-12位新密码");
                    return;
                }
                HashMap<String, String> params = new HashMap<>();
                params.put("oldPassword", MD5.hexdigest(originPassword));
                params.put("newPassword", MD5.hexdigest(newPassword));
                final GsonRequest<String> request = new GsonRequest<>(NetApi.midiff_password, String.class, params, new ResponseListener<String>() {
                    @Override
                    public void onErrorResponse(@Nullable VolleyError error) {
                        inputLayoutNewPass.setError("网络异常,请重试");
                    }

                    @Override
                    public void onResponse(String response, boolean result, String errorCode, @Nullable String msg) {
                        if (!result) {
                            inputLayoutNewPass.setError(msg);
                        } else {
                            dismiss();
                            Toast.makeText(getActivity(), "密码修改成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                request.start(getActivity());
                break;

        }
    }
}
