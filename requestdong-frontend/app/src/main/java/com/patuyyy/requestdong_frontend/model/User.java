package com.patuyyy.requestdong_frontend.model;

import com.google.gson.annotations.SerializedName;
public class User {
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("name")
    private String name;

    public int getUserId() {
        return user_id;
    }
    public String getName() {
        return name;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}
