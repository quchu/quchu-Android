package co.quchu.quchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import java.util.List;

/**
 * Created by Nico on 16/11/18.
 */

public class TextOptionAdapter extends RecyclerView.Adapter<TextOptionAdapter.TextOptionViewHolder> {

  private List<String> options;
  private int type;//type=2 服务器出错
  private String additionalShit;
  private OnInteractiveClick mOnInteractiveListener;
  private boolean mVertical = false;

  public TextOptionAdapter(List<String> options, String additional,int type,OnInteractiveClick listener) {
    this.options = options;
    this.additionalShit = additional;
    this.type = type;
    this.mOnInteractiveListener = listener;
  }

  public void updateGravity(boolean vertical){
    this.mVertical = vertical;
    notifyDataSetChanged();
  }

  @Override public TextOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new TextOptionViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_ai_conversation_txt_opt, parent, false));
  }

  @Override public void onBindViewHolder(TextOptionViewHolder holder, final int position) {
    final String answer = String.valueOf(options.get(position));
    holder.tvOption.setText(answer);
    holder.tvOption.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (type==2){
          if (position==0){
            mOnInteractiveListener.onRetry();
          }else{
            mOnInteractiveListener.onSearch();
          }
        }else{
          mOnInteractiveListener.onAnswer(answer, additionalShit,position);
        }
      }
    });


      holder.llContent.setGravity(Gravity.CENTER_HORIZONTAL);




  }

  @Override public int getItemCount() {
    return null != options ? options.size() : 0;
  }

  public class TextOptionViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.tvOption) TextView tvOption;
    @Bind(R.id.llContent) LinearLayout llContent;

    public TextOptionViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  public interface OnInteractiveClick {
    void onAnswer(String answer, String additionalShit,int index);
    void onRetry();
    void onSearch();
  }

}

