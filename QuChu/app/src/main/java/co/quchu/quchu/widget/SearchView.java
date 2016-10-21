package co.quchu.quchu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * Created by mwb on 16/10/20.
 */
public class SearchView extends LinearLayout {

  @Bind(R.id.search_back_btn) ImageView mSearchBackBtn;
  @Bind(R.id.search_input_et) EditText mSearchInputEt;
  @Bind(R.id.search_btn) TextView mSearchBtn;

  public SearchView(Context context, AttributeSet attrs) {
    super(context, attrs);

    LayoutInflater.from(context).inflate(R.layout.view_search, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  public EditText getSearchInputEt() {
    return mSearchInputEt;
  }

  public TextView getSearchBtn() {
    return mSearchBtn;
  }

  public ImageView getSearchBackBtn() {
    return mSearchBackBtn;
  }
}
