package com.justagroup.justabeer;

/**
 * Created by shekz on 09-12-2017.
 */

public class PrivateMessage {

    String id;
    String owner;
    String hangoutId;
    String timestamp;
    String content;

    public PrivateMessage(){}

    public PrivateMessage(String id, String owner, String hangoutid, String timestamp, String content){
        this.id = id;
        this.owner = owner;
        this.hangoutId = hangoutid;
        this.timestamp = timestamp;
        this.content = content;
    }

    public String getId() { return id; }
    public String getOwner() { return owner; }
    public String getHangoutId() { return hangoutId; }
    public String getTimestamp() { return timestamp; }
    public String getContent() { return  content; }

}
