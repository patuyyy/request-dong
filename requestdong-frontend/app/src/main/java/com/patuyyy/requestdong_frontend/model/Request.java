package com.patuyyy.requestdong_frontend.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Request {
    @SerializedName("request_id")
    private int request_id;
    @SerializedName("request_by")
    private String request_by;
    @SerializedName("taken_by")
    private String taken_by;
    @SerializedName("event_name")
    private String event_name;
    @SerializedName("requested_thing")
    private String requested_thing;
    @SerializedName("amount")
    private int amount;
    @SerializedName("deadline")
    private Date deadline;
    @SerializedName("status")
    private String status;
    public int getRequest_id() { return request_id; }

    public String getRequest_by() {
        return request_by;
    }
    public String getTaken_by() { return taken_by; }
    public String getEvent() { return event_name; }
    public String getRequested_thing() { return requested_thing; }
    public int getAmount() { return amount; }
    public Date getDeadline() { return deadline; }

    public String getStatus() { return status; }

}
