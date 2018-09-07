package com.example.youngk.communication.admin;

/**
 * Created by Eun_A on 2017-07-31.
 */

import android.graphics.drawable.Drawable;

/**
 * Created by YoungK on 2017-07-28.
 */

public class UserList {
    private Drawable profile,status;
    private String name;

    public Drawable getProfile() {
        return profile;
    }

    public void setProfile(Drawable profile) {
        this.profile = profile;
    }

    public Drawable getStatus() {
        return status;
    }

    public void setStatus(Drawable status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
