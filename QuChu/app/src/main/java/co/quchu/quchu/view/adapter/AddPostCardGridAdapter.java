package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.photo.Bimp;
import co.quchu.quchu.utils.FileUtils;
import co.quchu.quchu.utils.LogUtils;

/**
 * FlickrGridAdapter
 * User: Chenhs
 * Date: 2015-11-20
 */
public class AddPostCardGridAdapter extends BaseAdapter {
    private Context mContext;

    public AddPostCardGridAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        LogUtils.json("count==" + (Bimp.bmp.size() + Bimp.imglist.size()));
        return (Bimp.bmp.size() + Bimp.imglist.size()) < 9 ? (Bimp.bmp.size() + Bimp.imglist.size()) + 1 : 9;
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
        LogUtils.json("position==" + position + "//rePosi=" + (position - Bimp.imglist.size()) + "///imagelistsize=" + Bimp.imglist.size() + "///bim==" + Bimp.bmp.size());

        if ((Bimp.bmp.size() + Bimp.imglist.size()) < 9) {
            if (position == 0) {
                holder.itemAddpostcardSdv.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_add_photo_image));
                holder.itemAddpostcardDelIv.setVisibility(View.GONE);
            } else {
                if (Bimp.imglist.size() > 0) {
                    if (position <= Bimp.imglist.size()) {
                        holder.itemAddpostcardSdv.setImageURI(Uri.parse(Bimp.imglist.get(position - 1).getPath()));
                        holder.itemAddpostcardDelIv.setVisibility(View.VISIBLE);
                        holder.itemAddpostcardSdv.setAspectRatio(1f);
                    } else {
                        if (Bimp.bmp.size() > 0) {
                            if ((position - Bimp.imglist.size()) <= Bimp.bmp.size()) {
                                holder.itemAddpostcardSdv.setImageBitmap(Bimp.bmp.get((position - Bimp.imglist.size()-1)));
                                holder.itemAddpostcardDelIv.setVisibility(View.VISIBLE);
                                holder.itemAddpostcardSdv.setAspectRatio(1f);
                            }
                        }
                    }
                } else {
                    if (Bimp.bmp.size() > 0) {
                        holder.itemAddpostcardSdv.setImageBitmap(Bimp.bmp.get(position - Bimp.imglist.size() - 1));
                        holder.itemAddpostcardDelIv.setVisibility(View.VISIBLE);
                        holder.itemAddpostcardSdv.setAspectRatio(1f);
                    }
                }

/*
                if (Bimp.bmp.size() > 0)
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
                    holder.itemAddpostcardSdv.setImageURI(Uri.parse(Bimp.imglist.get(position).getPath()));
                    holder.itemAddpostcardDelIv.setVisibility(View.VISIBLE);
                    holder.itemAddpostcardSdv.setAspectRatio(1f);
                } else {
                    if (Bimp.bmp.size() > 0) {
                        holder.itemAddpostcardSdv.setImageBitmap(Bimp.bmp.get(position - Bimp.imglist.size()));
                        holder.itemAddpostcardDelIv.setVisibility(View.VISIBLE);
                        holder.itemAddpostcardSdv.setAspectRatio(1f);
                    }
                }
            } else {
                if (Bimp.bmp.size() > 0) {
                    holder.itemAddpostcardSdv.setImageBitmap(Bimp.bmp.get(position - Bimp.imglist.size()));
                    holder.itemAddpostcardDelIv.setVisibility(View.VISIBLE);

                    holder.itemAddpostcardSdv.setAspectRatio(1f);
                }
            }
            holder.itemAddpostcardDelIv.setTag(position);
            holder.itemAddpostcardDelIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positions = (int) v.getTag();
                    if (Bimp.bmp.size() + Bimp.imglist.size() < 9) {
                        if (Bimp.imglist.size() > 0) {
                            if (positions <= Bimp.imglist.size()) {
                                Bimp.imglist.remove(positions - 1);
                                notifyDataSetChanged();
                            } else if (positions > Bimp.imglist.size()) {
                                Bimp.bmp.remove(positions - Bimp.imglist.size() - 1);
                                Bimp.drr.remove(positions - Bimp.imglist.size() - 1);
                                notifyDataSetChanged();
                            }
                        } else {
                            if (positions <= Bimp.bmp.size()) {
                                Bimp.bmp.remove(positions - 1);
                                Bimp.drr.remove(positions - 1);
                                notifyDataSetChanged();
                            }
                        }
                    } else {
                        if (Bimp.imglist.size() > 0) {
                            if (positions < Bimp.imglist.size()) {
                                Bimp.imglist.remove(positions);
                                notifyDataSetChanged();
                            } else if (positions > Bimp.imglist.size() && positions <= (Bimp.bmp.size() + Bimp.imglist.size())) {
                                Bimp.bmp.remove(positions - Bimp.imglist.size());
                                Bimp.drr.remove(positions - Bimp.imglist.size());
                                notifyDataSetChanged();
                            }
                        } else {
                            if (positions < Bimp.bmp.size()) {
                                Bimp.bmp.remove(positions);
                                Bimp.drr.remove(positions);
                                notifyDataSetChanged();
                            }
                        }
                    }

                }
            });
        }


        return convertView;
    }

    public void update() {
        loading();
    }

    class ViewHolder {
        @Bind(R.id.item_addpostcard_sdv)
        SimpleDraweeView itemAddpostcardSdv;
        @Bind(R.id.item_addpostcard_del_iv)
        ImageView itemAddpostcardDelIv;
        @Bind(R.id.item_addpostcard_root_rl)
        RelativeLayout itemAddpostcardRootRl;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);

        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void loading() {
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
                            String path = Bimp.drr.get(Bimp.max);
                            System.out.println(path);
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
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
