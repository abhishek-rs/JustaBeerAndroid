package com.justagroup.justabeer;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by shekz on 02-12-2017.
 */

@IgnoreExtraProperties
public class User {
    private String id;
    private String fullName;
    private String photo;
    private String email;
    private Integer age;
    private String about;
    private String timestampJoined;

    public User() {
    }

    public User(String id, String mFullName, String photo, String mEmail, Integer age, String about, String timestampJoined) {
        this.id = id;
        this.fullName = mFullName;
        this.photo = photo;
        this.email = mEmail;
        this.age = age;
        this.about = about;
        this.timestampJoined = timestampJoined;
    }

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

    public String getAbout() {
        return about;
    }

    public String getTimestampJoined() {
        return timestampJoined;
    }
}
