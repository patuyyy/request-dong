package com.patuyyy.requestdong_frontend.model;

import com.google.gson.annotations.SerializedName;
public class Staff {
    @SerializedName("p_acara_id")
    private int p_acara_id;
    @SerializedName("p_ops_id")
    private int p_ops_id;
    @SerializedName("username")
    private String username;
    @SerializedName("event_name")
    private String event_name;
    @SerializedName("user_id")
    private int user_id;

    @SerializedName("event_id")
    private int event_id;


    public int getUserId() {
        return user_id;
    }
    public int getP_acara_id() {
        return p_acara_id;
    }
    public int getP_ops_id() {
        return p_acara_id;
    }
    public int getUser_id() {
        return user_id;
    }
    public int getEvent_id() {
        return event_id;
    }
    public String getUsername() {
        return username;
    }
    public String getEvent_name() {
        return event_name;
    }
}
