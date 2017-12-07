package com.justagroup.justabeer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by shekz on 07-12-2017.
 */

public class PendingRequest implements Parcelable {
    public PendingRequest(){}

    String hangoutId;
    String guestId;

    @Override public int describeContents() {
        return 0;
    }

    public PendingRequest(String hangoutId,
                          String guestId)
    {
        this.hangoutId = hangoutId;
        this.guestId = guestId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hangoutId);
        dest.writeString(guestId);
    }

    public static final Creator CREATOR = new Creator()
    {
        public PendingRequest createFromParcel(Parcel in) {
            return new PendingRequest(in);
        }

        public PendingRequest[] newArray(int size) {
            return new PendingRequest[size];
        }
    };
    // "De-parcel object
    public PendingRequest(Parcel in) {
        hangoutId = in.readString();
        guestId = in.readString();
    }

    public String getHangoutId() { return hangoutId; }

    public String getGuestId() { return guestId; }

}
