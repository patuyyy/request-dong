package com.patuyyy.requestdong_frontend.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Event {
    @SerializedName("event_id")
    private int event_id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("time")
    private Date time;
    public int getEvent_id() { return event_id; }

    public String getName() {
        return name;
    }
    public String getDescription() { return description; }
    public Date getTime() {
        return time;
    }
}
