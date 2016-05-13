package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

public class LoginMainActivity extends AppCompatActivity {

    @Bind(R.id.thirdLoginContainer)
    LinearLayout thirdLoginContainer;
    @Bind(R.id.hellWord)
    TextView hellWord;
    @Bind(R.id.photoLogin)
    Button photoLogin;
    @Bind(R.id.phoneRegister)
    Button phoneRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        ButterKnife.bind(this);

        TextPaint paint = hellWord.getPaint();
        paint.setFakeBoldText(true);
    }
}
