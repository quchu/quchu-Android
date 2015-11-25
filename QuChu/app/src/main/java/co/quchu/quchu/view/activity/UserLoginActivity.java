package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.view.fragment.UserLoginMainFragment;

/**
 * UserLoginActivity
 * User: Chenhs
 * Date: 2015-11-25
 * 用户登录界面
 */
public class UserLoginActivity extends BaseActivity {

    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        transaction = getSupportFragmentManager().beginTransaction();
         /*       transaction.setCustomAnimations(R.anim.in_push_right_to_left,R.anim.out_push_left_to_right);*/
        transaction.replace(R.id.user_login_fl, new UserLoginMainFragment());
             /*   transaction.addToBackStack(null);*/
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void clickPhone(){
      /*  transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_login_fl, new UserLoginMainFragment());
        transaction.commit();*/
        Toast.makeText(this,"selected Phone",0).show();
    }
}
