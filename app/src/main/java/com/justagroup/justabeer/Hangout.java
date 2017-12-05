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
        Beer, Food, Sports
    }

    String id;
    String title;
    Date fromTime;
    Date toTime;
    String description;
    LatLng location;
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
            Date fromTime,
            Date toTime,
            String description,
            LatLng location,
            EventType type,
            String owner,
            List<String> pendingUsers,
            List<String> confirmedUsers,
            List<String> rejectedUsers,
            List<String> commentIds,
            List<String> privateMessageIds)
    {
        this.id = id;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.description = description;
        this.location = location;
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
        dest.writeLong(fromTime.getTime());
        dest.writeLong(toTime.getTime());
        dest.writeString(description);
        dest.writeParcelable(location, flags);
        dest.writeString(type.name());
        dest.writeString(owner);
        dest.writeList(pendingUsers);
        dest.writeList(confirmedUsers);
        dest.writeList(rejectedUsers);
        dest.writeList(commentIds);
        dest.writeList(privateMessageIds);
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
        fromTime = new Date(in.readLong());
        toTime = new Date(in.readLong());
        description = in.readString();
        location = in.readParcelable(LatLng.class.getClassLoader());
        type = EventType.valueOf(in.readString());
        owner = in.readString();
        pendingUsers = (List<String>) in.readSerializable();
        confirmedUsers = (List<String>) in.readSerializable();
        rejectedUsers = (List<String>) in.readSerializable();
        commentIds = (List<String>) in.readSerializable();
        privateMessageIds = (List<String>) in.readSerializable();
    }

}
