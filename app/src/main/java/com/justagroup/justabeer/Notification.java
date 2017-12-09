package com.justagroup.justabeer;

/**
 * Created by shekz on 09-12-2017.
 */

public class Notification {

    enum NotificationType {
        Comment, Request, PrivateMessage
    }

    String id;
    String fromUser;
    String toUser;
    String timestamp;
    NotificationType type;

    public Notification(){}

    public Notification(String id, String fromUser, String toUser, String timestamp, NotificationType type){
        this.id = id;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.timestamp = timestamp;
        this.type = type;
    }

    public String getId() { return id; }
    public String getFromUser() { return fromUser; }
    public String getToUser() { return toUser; }
    public String getTimestamp() { return timestamp; }
    public NotificationType getType() { return type; }

}
