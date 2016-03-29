package co.quchu.quchu.dialog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * FlickrGridAdapter
 * User: Chenhs
 * Date: 2015-11-20
 */
public class DialogAccountSettingAdapter extends BaseAdapter {
    private Context mContext;
    private int[] imageLIst = {R.mipmap.ic_account_setting_camare, R.mipmap.ic_account_setting_album, R.mipmap.ic_account_setting_qu_photo};
    private String[] textArray = {"拍照", "相册", "趣头像"};

    public DialogAccountSettingAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return textArray.length;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_dialog_share, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.dialogItemShareIv.setImageDrawable(mContext.getResources().getDrawable(imageLIst[position]));
        holder.itemImageGridText.setText(textArray[position]);
        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_dialog_share.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    class ViewHolder {
        @Bind(R.id.dialog_item_share_iv)
        ImageView dialogItemShareIv;
        @Bind(R.id.item_image_grid_text)
        TextView itemImageGridText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
