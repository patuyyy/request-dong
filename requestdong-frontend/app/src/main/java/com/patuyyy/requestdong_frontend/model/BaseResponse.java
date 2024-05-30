package com.patuyyy.requestdong_frontend.model;


public class BaseResponse<T> {
    public boolean success;
    public String message;
    public T data;
}