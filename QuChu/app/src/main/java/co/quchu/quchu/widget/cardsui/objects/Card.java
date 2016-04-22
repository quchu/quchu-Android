package co.quchu.quchu.widget.cardsui.objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import co.quchu.quchu.R;
import co.quchu.quchu.widget.cardsui.Utils;


public abstract class Card extends AbstractCard {

    protected View mCardLayout;
    private boolean longClickable = false;

    private OnCardSwiped onCardSwipedListener;
    private OnClickListener mListener;
    private OnLongClickListener onLongListener;

    public Card() {

    }

    public Card(String title) {
        this.title = title;
    }

    public Card(String title, String color) {
        this.title = title;
        this.color = color;
    }

    public Card(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public Card(String title, String desc, int image) {
        this.title = title;
        this.desc = desc;
        this.image = image;
    }

    public Card(String titlePlay, String description, String color,
                String titleColor, Boolean hasOverflow, Boolean isClickable) {

        this.titlePlay = titlePlay;
        this.description = description;
        this.color = color;
        this.titleColor = titleColor;
        this.hasOverflow = hasOverflow;
        this.isClickable = isClickable;
    }

    public Card(String titlePlay, String description, int imageRes, String titleColor, Boolean hasOverflow,
                Boolean isClickable) {

        this.titlePlay = titlePlay;
        this.description = description;
        this.titleColor = titleColor;
        this.hasOverflow = hasOverflow;
        this.isClickable = isClickable;
        this.imageRes = imageRes;
    }

    @Override
    public View getView(Context context, boolean swipable) {
        return getView(context, false);
    }

    public void setBackgroundResourceId(int id, Context context) {
        View view = LayoutInflater.from(context).inflate(getCardLayout(), null);
        view.setBackgroundResource(id);
    }

    @Override
    public View getView(Context context) {

        View view = LayoutInflater.from(context).inflate(getCardLayout(), null);

        mCardLayout = view;

        try {
            ((FrameLayout) view.findViewById(R.id.cardContent))
                    .addView(getCardContent(context));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int bottom = Utils.convertDpToPixelInt(context, 12);
        lp.setMargins(0, 0, 0, bottom);

        view.setLayoutParams(lp);

        return view;
    }



    public abstract View getCardContent(Context context);

    public OnClickListener getClickListener() {
        return mListener;
    }

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public OnLongClickListener getOnLongClickListener() {
        return onLongListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongListener) {
        this.onLongListener = onLongListener;
    }

    public void OnSwipeCard() {
        if (onCardSwipedListener != null)
            onCardSwipedListener.onCardSwiped(this, mCardLayout);
        // TODO: find better implementation to get card-object's used content
        // layout (=> implementing getCardContent());
    }

    public OnCardSwiped getOnCardSwipedListener() {
        return onCardSwipedListener;
    }

    public void setOnCardSwipedListener(OnCardSwiped onEpisodeSwipedListener) {
        this.onCardSwipedListener = onEpisodeSwipedListener;
    }

    protected int getCardLayout() {
        return R.layout.item_card;
    }



    public int getId() {
        return R.id.cardContent;
    }


    public interface OnCardSwiped {
        void onCardSwiped(Card card, View layout);
    }

    /**
     * Attempt to reuse convertCardView.  Should not modify convertCardView if it's
     * not compatible.  The implementer should check the card content part and
     * verify that it matches.
     *
     * @param convertCardView the view to convert, with root Id equal to Card.getId()
     * @return true on success, false if not compatible
     */
    public abstract boolean convert(View convertCardView);

}