package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.view.adapter.DiscoverAdapter;
import co.quchu.quchu.widget.PostCardRecyclerView;

/**
 * CareAboutFriendsActivity
 * User: Chenhs
 * Date: 2015-11-11
 * 最关心的趣星人
 */
public class CareAboutFriendsActivity extends BaseActivity {

 /*   @Bind(R.id.care_about_friends_tv)
    TextView careAboutFriendsTv;*/
    @Bind(R.id.atmosphere_rv)
 PostCardRecyclerView atmosphereRv;

    /**
     * title
     ***/
    @Bind(R.id.title_back_rl)
    RelativeLayout titleBackRL;
    @Bind(R.id.title_more_rl)
    RelativeLayout titleMoreRl;
    @Bind(R.id.title_content_tv)
    TextView title_content_tv;
    private final List<PostCardRecyclerView.OnBackPressedListener> listeners = new ArrayList<PostCardRecyclerView.OnBackPressedListener>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_about_friends);
        ButterKnife.bind(this);
        title_content_tv.setText(getTitle());

        atmosphereRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        atmosphereRv.setAdapter(new DiscoverAdapter(this));
    }

    public void addOnBackPressedListener( PostCardRecyclerView.OnBackPressedListener onBackPressedListener ) {
        if ( this.listeners.indexOf( onBackPressedListener ) == -1 ) {
            this.listeners.add( onBackPressedListener );
        }
    }
    @Override
    public void onBackPressed() {
        if ( this.listeners.size() > 0 ) {
            for ( PostCardRecyclerView.OnBackPressedListener item : this.listeners ) {
                if ( item.onBackPressed() ) {
                    return;
                }
            }
        }
        super.onBackPressed();
    }
}
