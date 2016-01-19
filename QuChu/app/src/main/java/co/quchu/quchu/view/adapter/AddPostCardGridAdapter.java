package co.quchu.quchu.view.adapter;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.galleryfinal.model.PhotoInfo;
import co.quchu.quchu.R;
import co.quchu.quchu.photo.Bimp;
import co.quchu.quchu.photoselected.FrescoImageLoader;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;

/**
 * FlickrGridAdapter
 * User: Chenhs
 * Date: 2015-11-20
 */
public class AddPostCardGridAdapter extends BaseAdapter {
    private Activity mContext;
    List<PhotoInfo> mPhotoList;
    private FrescoImageLoader imageLoader;
    DisplayImageOptions options;

    public AddPostCardGridAdapter(Activity context, List<PhotoInfo> mPhotoList) {
        this.mContext = context;
        this.mPhotoList = mPhotoList;
        options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.ic_gf_default_photo)
                .showImageForEmptyUri(R.drawable.ic_gf_default_photo)
                .showImageOnLoading(R.drawable.ic_gf_default_photo).build();
    }

    @Override
    public int getCount() {
        LogUtils.json("count==" + (mPhotoList.size() + Bimp.imglist.size()));
        return (mPhotoList.size() + Bimp.imglist.size()) < 5 ? (mPhotoList.size() + Bimp.imglist.size()) + 1 : 5;
        // return 9;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_add_postcard_gridview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LogUtils.json("position==" + position + "//rePosi=" + (position - Bimp.imglist.size()) + "///imagelistsize=" + Bimp.imglist.size() + "///bim==" + mPhotoList.size());

        if ((mPhotoList.size() + Bimp.imglist.size()) < 5) {
            if (position == 0) {
                holder.itemAddpostcardSdv.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_add_photo_image));
                holder.itemAddpostcardDelIv.setVisibility(View.GONE);
            } else {
                if (Bimp.imglist.size() > 0) {
                    if (position <= Bimp.imglist.size()) {
                        //    holder.itemAddpostcardSdv.setImageURI(Uri.parse(Bimp.imglist.get(position - 1).getPath()));
                        ImageLoader.getInstance().displayImage(Bimp.imglist.get(position - 1).getPath(), holder.itemAddpostcardSdv, options);
                        //   imageLoader.displayImage(mContext, Bimp.imglist.get(position - 1).getPath()), holder.itemAddpostcardSdv, null, mRowWidth, mRowWidth);
                        holder.itemAddpostcardDelIv.setVisibility(View.VISIBLE);
                        //     holder.itemAddpostcardSdv.setAspectRatio(1f);
                    } else {
                        if (mPhotoList.size() > 0) {
                            if ((position - Bimp.imglist.size()) <= mPhotoList.size()) {
                                //        holder.itemAddpostcardSdv.setImageURI(Uri.parse("file://" + mPhotoList.get(position - Bimp.imglist.size() - 1).getPhotoPath()));
                                ImageLoader.getInstance().displayImage("file:/" + mPhotoList.get(position - Bimp.imglist.size() - 1).getPhotoPath(), holder.itemAddpostcardSdv, options);
                                holder.itemAddpostcardDelIv.setVisibility(View.VISIBLE);
                                //       holder.itemAddpostcardSdv.setAspectRatio(1f);
                            }
                        }
                    }
                } else {
                    if (mPhotoList.size() > 0) {
                        //     holder.itemAddpostcardSdv.setImageURI(Uri.parse("file://" + ));
                        ImageLoader.getInstance().displayImage("file:/" + mPhotoList.get(position - Bimp.imglist.size() - 1).getPhotoPath(), holder.itemAddpostcardSdv, options);
                        holder.itemAddpostcardDelIv.setVisibility(View.VISIBLE);
                        //     holder.itemAddpostcardSdv.setAspectRatio(1f);
                    }
                }

/*
                if (mPhotoList.size() > 0)
                    holder.itemAddpostcardSdv.setImageBitmap(Bimp.bmp.get(position - 1));
                holder.itemAddpostcardDelIv.setVisibility(View.VISIBLE);
                holder.itemAddpostcardDelIv.setTag(position - 1);
                holder.itemAddpostcardDelIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int positions = (int) v.getTag();
                        Toast.makeText(mContext, positions + "del==" + position, Toast.LENGTH_SHORT).show();
                        Bimp.drr.remove(positions);
                        Bimp.bmp.remove(positions);
                        notifyDataSetChanged();
                    }
                });*/
            }
        } else {
            if (Bimp.imglist.size() > 0) {
                if (position < Bimp.imglist.size()) {
                    //     holder.itemAddpostcardSdv.setImageURI(Uri.parse(Bimp.imglist.get(position).getPath()));
                    ImageLoader.getInstance().displayImage(Bimp.imglist.get(position).getPath(), holder.itemAddpostcardSdv, options);
                    holder.itemAddpostcardDelIv.setVisibility(View.VISIBLE);
                    //   holder.itemAddpostcardSdv.setAspectRatio(1f);
                } else {
                    if (mPhotoList.size() > 0) {
                        //        holder.itemAddpostcardSdv.setImageURI(Uri.parse("file://" + mPhotoList.get(position - Bimp.imglist.size()).getPhotoPath()));
                        ImageLoader.getInstance().displayImage("file:/" + mPhotoList.get(position - Bimp.imglist.size()).getPhotoPath(), holder.itemAddpostcardSdv, options);
                        holder.itemAddpostcardDelIv.setVisibility(View.VISIBLE);
                        //   holder.itemAddpostcardSdv.setAspectRatio(1f);
                    }
                }
            } else {
                if (mPhotoList.size() > 0) {
                    //   holder.itemAddpostcardSdv.setImageURI(Uri.parse("file://" + mPhotoList.get(position - Bimp.imglist.size()).getPhotoPath()));
                    ImageLoader.getInstance().displayImage("file:/" + mPhotoList.get(position - Bimp.imglist.size()).getPhotoPath(), holder.itemAddpostcardSdv, options);
                    holder.itemAddpostcardDelIv.setVisibility(View.VISIBLE);

                    // holder.itemAddpostcardSdv.setAspectRatio(1f);
                }
            }

        }
        holder.itemAddpostcardDelIv.setTag(position);
        holder.itemAddpostcardDelIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KeyboardUtils.isFastDoubleClick())
                    return;
                int positions = (int) v.getTag();
                Toast.makeText(mContext, R.string.word_delete_image_text, Toast.LENGTH_SHORT).show();
                if (mPhotoList.size() + Bimp.imglist.size() < 5) {
                    if (Bimp.imglist.size() > 0) {
                        if (positions <= Bimp.imglist.size()) {
                            Bimp.delImageIdList.add(Bimp.imglist.get(positions - 1).getImgId());
                            Bimp.imglist.remove(positions - 1);
                            notifyDataSetChanged();
                        } else if (positions > Bimp.imglist.size()) {
                            mPhotoList.remove(positions - Bimp.imglist.size() - 1);
                          /*  Bimp.drr.remove(positions - Bimp.imglist.size() - 1);
                            if (Bimp.max > 0)
                                Bimp.max -= 1;
                            if (0 == Bimp.drr.size())
                                Bimp.max = 0;*/
                            notifyDataSetChanged();
                        }
                    } else {
                        if (positions <= mPhotoList.size()) {
                            mPhotoList.remove(positions - 1);
                          /*  Bimp.drr.remove(positions - 1);
                            if (Bimp.max > 0)
                                Bimp.max -= 1;
                            if (0 == Bimp.drr.size())
                                Bimp.max = 0;*/
                            notifyDataSetChanged();
                        }
                    }
                } else {
                    if (Bimp.imglist.size() > 0) {
                        if (positions < Bimp.imglist.size()) {
                            Bimp.delImageIdList.add(Bimp.imglist.get(positions).getImgId());
                            Bimp.imglist.remove(positions);
                            notifyDataSetChanged();
                        } else if (positions > Bimp.imglist.size() && positions <= (mPhotoList.size() + Bimp.imglist.size())) {
                            mPhotoList.remove(positions - Bimp.imglist.size());
                          /*  Bimp.drr.remove(positions - Bimp.imglist.size());
                            if (Bimp.max > 0)
                                Bimp.max -= 1;
                            if (0 == Bimp.drr.size())
                                Bimp.max = 0;*/
                            notifyDataSetChanged();
                        }
                    } else {
                        if (positions < mPhotoList.size()) {
                            mPhotoList.remove(positions);
                         /*   Bimp.drr.remove(positions);
                            //   Bimp.max -= 1;
                            if (Bimp.max > 0)
                                Bimp.max -= 1;
                            if (0 == Bimp.drr.size())
                                Bimp.max = 0;*/
                            notifyDataSetChanged();
                        }
                    }
                }
            }
        });
        return convertView;
    }

    /*  public void update() {
          loading();
      }
  */
    class ViewHolder {
        @Bind(R.id.item_addpostcard_sdv)
        ImageView itemAddpostcardSdv;
        @Bind(R.id.item_addpostcard_del_iv)
        ImageView itemAddpostcardDelIv;
        @Bind(R.id.item_addpostcard_root_rl)
        RelativeLayout itemAddpostcardRootRl;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);

        }
    }

  /*  Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };*/

  /*  public void loading() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (Bimp.max == Bimp.drr.size()) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        break;
                    } else {
                        try {
                            if (Bimp.max <= Bimp.drr.size()) {
                                LogUtils.json("Bimp.max=" + Bimp.max + "///drrSize=" + Bimp.drr.size());
                                String path = Bimp.drr.get(Bimp.max);
                                Bitmap bm = Bimp.revitionImageSize(path);
                                Bimp.bmp.add(bm);
                                String newStr = path.substring(
                                        path.lastIndexOf("/") + 1,
                                        path.lastIndexOf("."));
                                FileUtils.saveBitmap(bm, "" + newStr);
                                Bimp.max += 1;
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }*/
}
