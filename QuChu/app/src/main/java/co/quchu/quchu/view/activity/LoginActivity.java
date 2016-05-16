package co.quchu.quchu.view.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.view.fragment.LoginByPhoneFragment;
import co.quchu.quchu.view.fragment.LoginFragment;
import co.quchu.quchu.view.fragment.PhoneValidationFragment;
import co.quchu.quchu.view.fragment.RegistrationFragment;
import co.quchu.quchu.view.fragment.RestorePasswordFragment;

public class LoginActivity extends BaseActivity {


    @Bind(R.id.flContent)
    FrameLayout flContent;

    LoginByPhoneFragment loginByPhoneFragment;
    LoginFragment loginFragment;
    PhoneValidationFragment phoneValidationFragment;
    RegistrationFragment registrationFragment;
    RestorePasswordFragment restorePasswordFragment;
    FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getEnhancedToolbar().hide();

        loginFragment = new LoginFragment();
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.flContent,loginFragment,LoginFragment.TAG).commit();
        getFragmentManager().executePendingTransactions();


    }

    @Override
    public void onBackPressed() {
        System.out.println("fragmentManager.getBackStackEntryCount() "+fragmentManager.getBackStackEntryCount());
        if (getFragmentManager().getBackStackEntryCount() > 0 ) {
            getFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }
//        int lastIndex = fragmentManager.getBackStackEntryCount();//
//        if (lastIndex>0){
//            fragmentManager.beginTransaction().setCustomAnimations(
//                    R.animator.card_flip_horizontal_right_in,
//                    R.animator.card_flip_horizontal_left_out,
//                    R.animator.card_flip_horizontal_left_in,
//                    R.animator.card_flip_horizontal_right_out)
//                    .remove(fragmentManager.findFragmentById(fragmentManager.getBackStackEntryAt(lastIndex).getId()))
//                    .commitAllowingStateLoss();
//
////            fragmentManager = getSupportFragmentManager();
////            lastIndex = fragmentManager.getFragments().size()-1;//
////            lastIndex-=1;
////            fragmentManager.beginTransaction().show(fragmentManager.getFragments().get(lastIndex-1)).commitAllowingStateLoss();
//        }
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }
}
