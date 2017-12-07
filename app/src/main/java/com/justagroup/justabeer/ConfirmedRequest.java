package com.justagroup.justabeer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by shekz on 07-12-2017.
 */

public class ConfirmedRequest implements Parcelable {
    public ConfirmedRequest(){}

    String hangoutId;
    String guestId;

    @Override public int describeContents() {
        return 0;
    }

    public ConfirmedRequest(String hangoutId,
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

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public ConfirmedRequest createFromParcel(Parcel in) {
            return new ConfirmedRequest(in);
        }

        public ConfirmedRequest[] newArray(int size) {
            return new ConfirmedRequest[size];
        }
    };
    // "De-parcel object
    public ConfirmedRequest(Parcel in) {
        hangoutId = in.readString();
        guestId = in.readString();
    }

    public String getHangoutId() { return hangoutId; }
    public String getGuestId() { return guestId; }

}
