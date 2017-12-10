package com.justagroup.justabeer;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by shekz on 02-12-2017.
 */

@IgnoreExtraProperties
public class User implements Parcelable {

    private String id;
    private String fullName;
    private String photo;
    private String email;
    private Integer age;
    private String gender;
    private String about;
    private String timestampJoined;

    public User() {
    }

    @Override public int describeContents() {
        return 0;
    }

    public User(String id, String mFullName, String photo, String mEmail, Integer age, String gender, String about, String timestampJoined) {
        this.id = id;
        this.fullName = mFullName;
        this.photo = photo;
        this.email = mEmail;
        this.age = age;
        this.gender = gender;
        this.about = about;
        this.timestampJoined = timestampJoined;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(fullName);
        dest.writeString(photo);
        dest.writeString(email);
        dest.writeInt(age);
        dest.writeString(gender);
        dest.writeString(about);
        dest.writeString(timestampJoined);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
    // "De-parcel object
    public User(Parcel in) {
        id = in.readString();
        fullName = in.readString();
        photo = in.readString();
        email = in.readString();
        age = in.readInt();
        gender = in.readString();
        about = in.readString();
        timestampJoined = in.readString();
    }

    //getters
    public String getId() { return id; }

    public String getFullName() {
        return fullName;
    }

    public String getPhoto() { return photo; }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getAbout() {
        return about;
    }

    public String getTimestampJoined() {
        return timestampJoined;
    }

    //setters
    public void setId(String id) {
        this.id = id;
    }

    public void setFullName(String name){
        this.fullName = name;
    }

    public void setPhoto(String url) {
        this.photo = url;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setTimestampJoined(String timestamp) {
        this.timestampJoined = timestamp;
    }

}
