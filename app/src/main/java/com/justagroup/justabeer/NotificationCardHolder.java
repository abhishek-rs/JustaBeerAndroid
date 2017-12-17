package com.justagroup.justabeer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by shekz on 06-12-2017.
 */

public class NotificationCardHolder extends RecyclerView.ViewHolder {
    private static final String TAG = NotificationCardHolder.class.getSimpleName();
    public LinearLayout commentsMessagesLayout;
    public LinearLayout requestsLayout;
    public TextView commentsMessagesContent;
    public Button commentsMessagesShow;
    public TextView requestsContent;
    public Button requestsAccept;
    public Button requestsReject;
    public Button requestsProfile;
    public TextView notificationTime;

    public NotificationCardHolder(View itemView) {
        super(itemView);
        commentsMessagesLayout = (LinearLayout) itemView.findViewById(R.id.comments_messages);
        requestsLayout = (LinearLayout) itemView.findViewById(R.id.requests);
        commentsMessagesContent = (TextView)itemView.findViewById(R.id.comments_messages_content);
        commentsMessagesShow = (Button)itemView.findViewById(R.id.comments_messages_show);
        requestsContent = (TextView)itemView.findViewById(R.id.requests_content);
        requestsAccept = (Button)itemView.findViewById(R.id.request_accept);
        requestsReject = (Button)itemView.findViewById(R.id.request_reject);
        requestsProfile = (Button)itemView.findViewById(R.id.request_profile);
        notificationTime = (TextView)itemView.findViewById(R.id.notification_time);
    }

    public Notification notification;
}
