package com.justagroup.justabeer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by shekz on 06-12-2017.
 */

public class CardHolder extends RecyclerView.ViewHolder {
    private static final String TAG = CardHolder.class.getSimpleName();
    public TextView hangoutName;
    public ImageView hangoutImage;
    public Button owner;
    public TextView desc;
    public TextView attendees;
    public TextView comments;
    public Button seeMore;

    public CardHolder(View itemView) {
        super(itemView);
        hangoutName = (TextView)itemView.findViewById(R.id.hangout_card_title);
        hangoutImage = (ImageView)itemView.findViewById(R.id.hangout_card_pic);
        owner = (Button)itemView.findViewById(R.id.hangout_card_owner);
        desc = (TextView)itemView.findViewById(R.id.hangout_card_desc);
        attendees = (TextView)itemView.findViewById(R.id.hangout_card_people);
        comments = (TextView)itemView.findViewById(R.id.hangout_card_comments);
        seeMore = (Button)itemView.findViewById(R.id.hangout_seemore);
    }
}
