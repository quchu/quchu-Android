package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.SimpleQuchuSearchResultModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.NearbyPresenter;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.PickingQuchuAdapter;

/**
 * Created by Nikolai on 2016/4/17.
 */
public class PickingQuchuActivity extends BaseActivity {


    public String mKeyword;
    public static final String BUNDLE_KEY_KEYWORD = "BUNDLE_KEY_KEYWORD";
    public static final String BUNDLE_KEY_PICKING_RESULT_NAME = "BUNDLE_KEY_PICKING_RESULT_NAME";
    public static final String BUNDLE_KEY_PICKING_RESULT_ID= "BUNDLE_KEY_PICKING_RESULT_ID";
    @Bind(R.id.etSearchField)
    EditText mEtSearchField;
    @Bind(R.id.rvContent)
    RecyclerView mRvContent;
    PickingQuchuAdapter mAdapter;
    List<SimpleQuchuSearchResultModel> mData = new ArrayList<>();
    private TextWatcher mTextWatcher;

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    protected void onDestroy() {
        if (null!=mEtSearchField){
            mEtSearchField.removeTextChangedListener(mTextWatcher);
        }
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picking_quchu);
        ButterKnife.bind(this);
        getEnhancedToolbar();

        mKeyword = getIntent().getStringExtra(BUNDLE_KEY_KEYWORD);
        mAdapter = new PickingQuchuAdapter(mData, new PickingQuchuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent   = new Intent();
                intent.putExtra(BUNDLE_KEY_PICKING_RESULT_ID,mData.get(position).getId());
                intent.putExtra(BUNDLE_KEY_PICKING_RESULT_NAME,mData.get(position).getName());
                setResult(RESULT_OK,intent);
            }
        });
        mRvContent.setAdapter(mAdapter);
        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (null!=s && !StringUtils.isEmpty(s.toString()))
                    doSearch();
            }
        };
        mEtSearchField.addTextChangedListener(mTextWatcher);
    }

    private void doSearch(){
        NearbyPresenter.getSearchResult(this, SPUtils.getCityId(), mKeyword, new CommonListener<List<SimpleQuchuSearchResultModel>>() {
            @Override
            public void successListener(List<SimpleQuchuSearchResultModel> response) {
                mData.clear();
                mData.addAll(response);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {

            }
        });
    }
}
