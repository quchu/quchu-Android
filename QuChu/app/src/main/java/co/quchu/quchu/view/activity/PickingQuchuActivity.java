package co.quchu.quchu.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.SimpleQuchuSearchResultModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.NearbyPresenter;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.PickingQuchuAdapter;

/**
 * Created by Nikolai on 2016/4/17.
 */
public class PickingQuchuActivity extends BaseActivity {


    public static final String BUNDLE_KEY_KEYWORD = "BUNDLE_KEY_KEYWORD";
    public static final String BUNDLE_KEY_PICKING_RESULT_NAME = "BUNDLE_KEY_PICKING_RESULT_NAME";
    public static final String BUNDLE_KEY_PICKING_RESULT_ID = "BUNDLE_KEY_PICKING_RESULT_ID";
    //是否从我的脚印跳转
    public static final String REQUEST_KEY_FROM_MY_FOOTPRINT = "from";

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
        if (null != mEtSearchField) {
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
        EnhancedToolbar toolbar = getEnhancedToolbar();

        String mKeyword = getIntent().getStringExtra(BUNDLE_KEY_KEYWORD);
        final boolean fromMyfootprint = getIntent().getBooleanExtra(REQUEST_KEY_FROM_MY_FOOTPRINT, false);
        mAdapter = new PickingQuchuAdapter(mData, new PickingQuchuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (fromMyfootprint) {
                    Intent intent = new Intent(PickingQuchuActivity.this, AddFootprintActivity.class);

                    intent.putExtra(AddFootprintActivity.REQUEST_KEY_ID, mData.get(position).getId());
                    intent.putExtra(AddFootprintActivity.REQUEST_KEY_NAME, mData.get(position).getName());
                    intent.putExtra(AddFootprintActivity.REQUEST_KEY_ALLOW_PICKING_STORE, true);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(BUNDLE_KEY_PICKING_RESULT_ID, mData.get(position).getId());
                    intent.putExtra(BUNDLE_KEY_PICKING_RESULT_NAME, mData.get(position).getName());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        mRvContent.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mRvContent.setAdapter(mAdapter);
        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (null != s && !StringUtils.isEmpty(s.toString()))
                doSearch(s.toString().trim());
            }
        };

        mEtSearchField.addTextChangedListener(mTextWatcher);
        doSearch(mKeyword);

        TextView textView = toolbar.getRightTv();
        textView.setText("下一步");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = mEtSearchField.getText().toString().trim();

                boolean filterSucceed = false;
                for (SimpleQuchuSearchResultModel item : mData) {
                    if (item.getName().equals(trim)) {
                        filterSucceed = true;
                        if (fromMyfootprint) {
                            Intent intent = new Intent(PickingQuchuActivity.this, AddFootprintActivity.class);
                            intent.putExtra(AddFootprintActivity.REQUEST_KEY_ID, item.getId());
                            intent.putExtra(AddFootprintActivity.REQUEST_KEY_NAME, item.getName());
                            intent.putExtra(AddFootprintActivity.REQUEST_KEY_ALLOW_PICKING_STORE, true);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra(BUNDLE_KEY_PICKING_RESULT_ID, item.getId());
                            intent.putExtra(BUNDLE_KEY_PICKING_RESULT_NAME, item.getName());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                }
                if (!filterSucceed) {
                    Toast.makeText(PickingQuchuActivity.this, "您输入的趣处不存在", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEtSearchField.requestFocus();

        mEtSearchField.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(mEtSearchField, 0);
            }
        }, 200);
    }

    private void doSearch(String keyWord) {
        if (!NetUtil.isNetworkConnected(this)) {
            Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
            return;
        }

        NearbyPresenter.getSearchResult(this, SPUtils.getCityId(), keyWord, new CommonListener<List<SimpleQuchuSearchResultModel>>() {
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
