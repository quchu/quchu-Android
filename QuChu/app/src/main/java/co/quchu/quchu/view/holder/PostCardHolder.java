package co.quchu.quchu.view.holder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;

import co.quchu.quchu.R;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.widget.cardsui.objects.Card;


/**
 * PostCardHolder
 * User: Chenhs
 * Date: 2015-12-15
 */
public class PostCardHolder extends Card {
    private PostCardModel.PostCardItem item;

    public PostCardHolder(PostCardModel.PostCardItem item) {
        this.item = item;
    }

    @Override
    public View getCardContent(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CardView v = (CardView) inflater.inflate(R.layout.item_my_postcard_view, null);
        v.setCardBackgroundColor(Color.parseColor("#33ff33"));
        return v;
    }

    @Override
    public boolean convert(View convertCardView) {
        return false;
    }
}
