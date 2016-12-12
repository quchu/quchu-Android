package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.gallery.utils.Utils;
import co.quchu.quchu.model.SceneInfoModel;

/**
 * 场景列表适配器
 * <p>
 * Created by mwb on 16/11/7.
 */
public class SceneListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private Context mContext;
  private List<SceneInfoModel> mSceneList;
  private int mLimitation = 0;

  public SceneListAdapter(Context context, List<SceneInfoModel> sceneList) {
    mContext = context;
    mSceneList = sceneList;
  }

  public SceneListAdapter(Context context, List<SceneInfoModel> sceneList, int limitation) {
    mContext = context;
    mSceneList = sceneList;
    mLimitation = limitation;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new SceneViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_scene_list, parent, false));
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
    SceneInfoModel sceneInfoModel = mSceneList.get(position);

    if (viewHolder instanceof SceneViewHolder) {
      SceneViewHolder holder = (SceneViewHolder) viewHolder;

      LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.rl_cover.getLayoutParams();
      if (mLimitation > 0) {
        //首页
        lp.width = Utils.dip2px(mContext, 38);
        lp.height = Utils.dip2px(mContext, 38);
        holder.rl_cover.setLayoutParams(lp);

      } else {
        lp.width = Utils.dip2px(mContext, 64);
        lp.height = Utils.dip2px(mContext, 64);
        holder.rl_cover.setLayoutParams(lp);
      }

      //首页显示所有场景
      if (mLimitation > 0 && position == 3) {
        holder.mSceneCoverImg.getHierarchy().setPlaceholderImage(R.drawable.ic_suoyouchangjing_main);
        holder.mSceneTitleTv.setText("所有场景");
        holder.mSceneTitleTv.setTextColor(mContext.getResources().getColor(R.color.standard_color_h1_dark));

      } else {
        if (!TextUtils.isEmpty(sceneInfoModel.getIconUrlSmall())) {
          holder.mSceneCoverImg.setImageURI(Uri.parse(sceneInfoModel.getIconUrlSmall()));
        }
        holder.mSceneTitleTv.setText(sceneInfoModel.getSceneName());
        holder.mSceneTitleTv.setTextColor(mContext.getResources().getColor(mLimitation > 0 ? R.color.standard_color_h1_dark : R.color.standard_color_h3_dark));
      }

      holder.tvHot.setVisibility(sceneInfoModel.isHot() ? View.VISIBLE : View.GONE);

      holder.itemView.setTag(sceneInfoModel);
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          SceneInfoModel sceneInfoModel = (SceneInfoModel) v.getTag();
          if (sceneInfoModel != null && mListener != null) {
            mListener.onItemClick(sceneInfoModel, position);
          }
        }
      });
    }
  }

  @Override
  public int getItemCount() {
    return mSceneList != null ? mLimitation > 0 ? Math.min(mSceneList.size(), mLimitation) : mSceneList.size() : 0;
  }

  public class SceneViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.scene_cover_img) SimpleDraweeView mSceneCoverImg;
    @Bind(R.id.scene_title_tv) TextView mSceneTitleTv;
    @Bind(R.id.rl_cover) RelativeLayout rl_cover;
    @Bind(R.id.tvHot) TextView tvHot;

    public SceneViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  private OnSceneListListener mListener;

  public void setOnSceneListListener(OnSceneListListener listener) {
    mListener = listener;
  }

  public interface OnSceneListListener {
    void onItemClick(SceneInfoModel sceneInfoModel, int position);
  }
}
