package co.quchu.quchu.view.activity;

import android.os.Bundle;

import co.quchu.quchu.base.BaseActivity;

/**
 * Created by Nico on 16/5/25.
 */
public class QuchuListSpecifyTagActivity extends BaseActivity {
    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        /place/getPlaceByTagId
//        tagId:
//        required
//                int3001
//        标签ID
//        cityId:
//        required
//                int1
//        城市ID
//        latitude:
//        required
//        double24.485162665399
//        精度
//        longitude:
//        required
//        double118.03803057957
//        纬度
    }
}
