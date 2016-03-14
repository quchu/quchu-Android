package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.view.adapter.RecommendFragmentAdapter;
import co.quchu.quchu.view.fragment.ClassifyFragment;
import co.quchu.quchu.view.fragment.RecommendFragment;
import co.quchu.quchu.widget.AnimationViewPager.NoScrollViewPager;
import co.quchu.quchu.widget.AnimationViewPager.RotatePageTransformer;

/**
 * PostCardFromImageActivity
 * User: Chenhs
 * Date: 2016-03-11
 * 明信片界面 （从相册跳转）
 */
public class PostCardFromImageActivity extends BaseActivity {

    @Bind(R.id.recommend_body_vp)
    NoScrollViewPager recommendBodyVp;
    @Bind(R.id.buttonPanel)
    Button buttonPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcard_from_image);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();

    }

    private void initData() {

    }

    private void initView() {
        InitViewPager();

        buttonPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                         recommendBodyVp.setCurrentItem(indexView == 0 ? 1 : 0);
            }
        });
    }

    int indexView = 0;

    /*
   * 初始化ViewPager
   */
    public void InitViewPager() {
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        RecommendFragment recoFragment = new RecommendFragment();
        ClassifyFragment classifyFragment = new ClassifyFragment();
        fragmentList.add(recoFragment);
        fragmentList.add(classifyFragment);
        //给ViewPager设置适配器
        recommendBodyVp.setAdapter(new RecommendFragmentAdapter(getSupportFragmentManager(), fragmentList));
        recommendBodyVp.setPageTransformer(true, new RotatePageTransformer());
        recommendBodyVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indexView = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
