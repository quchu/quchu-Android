package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.photo.Bimp;
import co.quchu.quchu.utils.FileUtils;

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
        return Bimp.drr.size() < 9 ? Bimp.drr.size() + 1 : Bimp.drr.size();
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


        if (Bimp.bmp.size() < 9) {
            if (position == 0) {
                //  holder.itemAddpostcardSdv.setImageURI(Uri.parse("res://" + mContext.getPackageName() + "/" + R.drawable.ic_add_photo_image));
                holder.itemAddpostcardSdv.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_add_photo_image));
                holder.itemAddpostcardDelIv.setVisibility(View.GONE);
            } else {
                // holder.itemAddpostcardSdv.setImageURI(Uri.parse("file://" + Bimp.drr.get(position - 1)));
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
                });
            }
        } else {
            holder.itemAddpostcardSdv.setImageBitmap(Bimp.bmp.get(position));
            holder.itemAddpostcardDelIv.setVisibility(View.VISIBLE);
            holder.itemAddpostcardDelIv.setTag(position);
            holder.itemAddpostcardDelIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positions = (int) v.getTag();
                    Bimp.drr.remove(positions);
                    Bimp.bmp.remove(positions);
                    notifyDataSetChanged();
                }
            });
            holder.itemAddpostcardSdv.setAspectRatio(1f);
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
