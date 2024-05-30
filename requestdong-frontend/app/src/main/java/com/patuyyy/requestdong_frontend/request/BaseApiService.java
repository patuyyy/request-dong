package com.patuyyy.requestdong_frontend.request;

import com.patuyyy.requestdong_frontend.model.BaseResponse;
import com.patuyyy.requestdong_frontend.model.Event;
import com.patuyyy.requestdong_frontend.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {
    @FormUrlEncoded
    @POST("users/login")
    Call<BaseResponse<User>> login(
            @Field("username") String username,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("users/register")
    Call<BaseResponse<User>> register(
            @Field("name") String name,
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("events")
    Call<List<Event>> getAllEvent(

    );
}
