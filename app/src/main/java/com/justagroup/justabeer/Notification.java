package com.justagroup.justabeer;

/**
 * Created by shekz on 09-12-2017.
 */

public class Notification {

    public enum NotificationType {
        Comment, Request, CancelRequest, CancelConfirmation, PrivateMessage, AcceptedRequest, RejectedRequest
    }

    String id;
    String fromUser;
    String toUser;
    String timestamp;
    String hangoutId;
    NotificationType type;

    public Notification(){}

    public Notification(String id, String fromUser, String toUser, String timestamp, String hangoutId, NotificationType type){
        this.id = id;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.timestamp = timestamp;
        this.hangoutId = hangoutId;
        this.type = type;
    }

    public String getId() { return id; }
    public String getFromUser() { return fromUser; }
    public String getToUser() { return toUser; }
    public String getTimestamp() { return timestamp; }
    public String getHangoutId() { return  hangoutId; }
    public NotificationType getType() { return type; }

}
