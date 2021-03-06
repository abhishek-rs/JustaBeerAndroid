package com.justagroup.justabeer;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shekz on 22-11-2017.
 */

public class Hangout implements Parcelable {
    public enum EventType {
        Beer, Food, Coffee
    }

    public Hangout(){}

    String id;
    String title;
    String fromTime;
    String toTime;
    String description;
    Double lat;
    Double lng;
    EventType type;
    String owner;
    List<String> pendingUsers;
    List<String> confirmedUsers;
    List<String> rejectedUsers;
    List<String> commentIds;
    List<String> privateMessageIds;

    @Override public int describeContents() {
        return 0;
    }

    public Hangout(String id,
            String title,
            String fromTime,
            String toTime,
            String description,
            Double lat,
            Double lng,
            EventType type,
            String owner,
            List<String> pendingUsers,
            List<String> confirmedUsers,
            List<String> rejectedUsers,
            List<String> commentIds,
            List<String> privateMessageIds)
    {
        this.id = id;
        this.title = title;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.type = type;
        this.owner = owner;
        this.pendingUsers = pendingUsers;
        this.confirmedUsers = confirmedUsers;
        this.rejectedUsers = rejectedUsers;
        this.commentIds = commentIds;
        this.privateMessageIds = privateMessageIds;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(fromTime);
        dest.writeString(toTime);
        dest.writeString(description);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(type.name());
        dest.writeString(owner);
        dest.writeStringList(pendingUsers);
        dest.writeStringList(confirmedUsers);
        dest.writeStringList(rejectedUsers);
        dest.writeStringList(commentIds);
        dest.writeStringList(privateMessageIds);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public Hangout createFromParcel(Parcel in) {
            return new Hangout(in);
        }

        public Hangout[] newArray(int size) {
            return new Hangout[size];
        }
    };
    // "De-parcel object
    public Hangout(Parcel in) {
        id = in.readString();
        title = in.readString();
        fromTime = in.readString();
        toTime = in.readString();
        description = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        type = EventType.valueOf(in.readString());
        owner = in.readString();
        pendingUsers = new ArrayList<String>();
        confirmedUsers = new ArrayList<String>();
        rejectedUsers = new ArrayList<String>();
        commentIds = new ArrayList<String>();
        privateMessageIds = new ArrayList<String>();
        in.readStringList(pendingUsers);
        in.readStringList(confirmedUsers);
        in.readStringList(rejectedUsers);
        in.readStringList(commentIds);
        in.readStringList(privateMessageIds);
    }

    public String getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getFromTime() { return fromTime; }

    public String getToTime() {
        return toTime;
    }

    public String getDescription() { return description; }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public EventType getType() {
        return type;
    }

    public String getOwner() { return owner; }

    public List<String> getPendingUsers() { return pendingUsers; }

    public void setPendingUsers(List<String> pendingUsers) { this.pendingUsers = pendingUsers; }

    public List<String> getConfirmedUsers() { return confirmedUsers; }

    public void setConfirmedUsers(List<String> confirmedUsers) { this.confirmedUsers = confirmedUsers; }

    public List<String> getRejectedUsers() { return rejectedUsers; }

    public void setRejectedUsers(List<String> rejectedUsers) { this.rejectedUsers = rejectedUsers; }

    public List<String> getCommentIds() { return commentIds; }

    public void setCommentIds(List<String> commentIds) { this.commentIds = commentIds; }

    public List<String> getPrivateMessageIds() { return privateMessageIds; }

    public void setPrivateMessageIds(List<String> privateMessageIds) { this.privateMessageIds = privateMessageIds; }
}
