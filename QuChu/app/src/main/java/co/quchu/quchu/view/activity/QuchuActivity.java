package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.view.fragment.FavoriteFragment;
import co.quchu.quchu.view.fragment.FindFragment;

/**
 * 趣处 包含我收藏的和我发现的
 */
public class QuchuActivity extends BaseActivity {

    @Bind(R.id.Favorite)
    TextView Favorite;
    @Bind(R.id.find)
    TextView find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quchu);
        ButterKnife.bind(this);
        initListener();
        Favorite.callOnClick();
    }

    private void initListener() {
        Favorite.setOnClickListener(this);
        find.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (v.getId()) {
            case R.id.Favorite:
                if (!Favorite.isSelected()) {
                    Favorite.setSelected(true);
                    find.setSelected(false);
                    transaction.replace(R.id.container, new FavoriteFragment()).commit();
                }
                break;
            case R.id.find:
                if (!find.isSelected()) {
                    Favorite.setSelected(false);
                    find.setSelected(true);
                    transaction.replace(R.id.container, new FindFragment()).commit();
                }
                break;
        }
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


}