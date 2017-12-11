package com.justagroup.justabeer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shekz on 06-12-2017.
 */

public class CommentCardHolder extends RecyclerView.ViewHolder {
    private static final String TAG = CommentCardHolder.class.getSimpleName();
    public ImageView userImage;
    public TextView userName;
    public TextView content;
    public TextView timestamp;

    public CommentCardHolder(View itemView) {
        super(itemView);
        userImage = (ImageView)itemView.findViewById(R.id.comment_image);
        userName = (TextView)itemView.findViewById(R.id.comment_username);
        content = (TextView)itemView.findViewById(R.id.comment_content);
        timestamp = (TextView)itemView.findViewById(R.id.comment_time);
    }

    public Comment comment;
}
