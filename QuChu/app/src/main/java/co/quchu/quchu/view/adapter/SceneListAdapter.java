package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.SceneInfoModel;

/**
 * 场景列表适配器
 * <p>
 * Created by mwb on 16/11/7.
 */
public class SceneListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private Context mContext;
  private List<SceneInfoModel> mSceneList;

  public SceneListAdapter(Context context, List<SceneInfoModel> sceneList) {
    mContext = context;
    mSceneList = sceneList;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new SceneViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_scene_list, parent, false));
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    SceneInfoModel sceneInfoModel = mSceneList.get(position);

    if (viewHolder instanceof SceneViewHolder) {
      SceneViewHolder holder = (SceneViewHolder) viewHolder;
      holder.mSceneCoverImg.setImageURI(Uri.parse(sceneInfoModel.getSceneCover()));
      holder.mSceneTitleTv.setText(sceneInfoModel.getSceneName());

      holder.itemView.setTag(sceneInfoModel);
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          SceneInfoModel sceneInfoModel = (SceneInfoModel) v.getTag();
          if (sceneInfoModel != null && mListener != null) {
            mListener.onItemClick(sceneInfoModel);
          }
        }
      });
    }
  }

  @Override
  public int getItemCount() {
    return mSceneList != null ? mSceneList.size() : 0;
  }

  public class SceneViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.scene_cover_img) SimpleDraweeView mSceneCoverImg;
    @Bind(R.id.scene_title_tv) TextView mSceneTitleTv;

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
    void onItemClick(SceneInfoModel sceneInfoModel);
  }
}
