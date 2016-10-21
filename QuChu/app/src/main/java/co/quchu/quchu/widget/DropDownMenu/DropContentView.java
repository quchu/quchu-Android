package co.quchu.quchu.widget.DropDownMenu;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * Created by mwb on 16/10/20.
 */
public class DropContentView extends LinearLayout {

  @Bind(R.id.drop_left_recycler_view) RecyclerView mLeftRecyclerView;
  @Bind(R.id.drop_right_recycler_view) RecyclerView mRightRecyclerView;
  @Bind(R.id.drop_both_sides_layout) LinearLayout mBothSidesLayout;
  @Bind(R.id.drop_recycler_view) RecyclerView mRecyclerView;

  public DropContentView(Context context) {
    this(context, null);
  }

  public DropContentView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DropContentView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    init(context);
  }

  private void init(Context context) {
    LayoutInflater.from(context).inflate(R.layout.view_drop_content, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);

    mLeftRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    ContentAdapter leftContentAdapter = new ContentAdapter();
    mLeftRecyclerView.setAdapter(leftContentAdapter);

    mRightRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    ContentAdapter rightContentAdapter = new ContentAdapter();
    mRightRecyclerView.setAdapter(rightContentAdapter);

    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    ContentAdapter contentAdapter = new ContentAdapter();
    mRecyclerView.setAdapter(contentAdapter);
  }

  public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_drop_conent, parent, false));
    }

    @Override
    public void onBindViewHolder(ContentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
      return 0;
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {

      @Bind(R.id.item_drop_content_tv) TextView mContentTv;
      @Bind(R.id.item_drop_content_iv) ImageView mContentIv;
      @Bind(R.id.item_drop_content_divider) View mContentDivider;

      public ContentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
      }
    }
  }
}
