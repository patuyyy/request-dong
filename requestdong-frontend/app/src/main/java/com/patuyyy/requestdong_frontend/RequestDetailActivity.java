package com.patuyyy.requestdong_frontend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.patuyyy.requestdong_frontend.model.BaseResponse;
import com.patuyyy.requestdong_frontend.model.Request;
import com.patuyyy.requestdong_frontend.model.Staff;
import com.patuyyy.requestdong_frontend.model.User;
import com.patuyyy.requestdong_frontend.request.BaseApiService;
import com.patuyyy.requestdong_frontend.request.UtilsApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RequestDetailActivity extends AppCompatActivity {

    private TextView requested_thing, date, request_by, taken_by;
    private TextView event, amount, status;
    private Button acceptBtn, rejectBtn, finishBtn;
    private BaseApiService mApiService;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){

        }
        mContext = this;
        mApiService = UtilsApi.getApiService();

        requested_thing = findViewById(R.id.requestedthing);
        date = findViewById(R.id.deadline);
        request_by = findViewById(R.id.requestby);
        taken_by = findViewById(R.id.takenby);
        event = findViewById(R.id.eventname);
        amount = findViewById(R.id.amount);
        status = findViewById(R.id.status);

        requested_thing.setText(EventDetailActivity.selectedRequestTemp.getRequested_thing());
        date.setText(EventDetailActivity.selectedRequestTemp.getDeadline().toString());
        request_by.setText(EventDetailActivity.selectedRequestTemp.getRequest_by());
        taken_by.setText(EventDetailActivity.selectedRequestTemp.getTaken_by());
        event.setText(EventDetailActivity.selectedRequestTemp.getEvent());
        amount.setText(EventDetailActivity.selectedRequestTemp.getAmount());
        status.setText(EventDetailActivity.selectedRequestTemp.getStatus());

        acceptBtn = findViewById(R.id.acceptbtn);
        rejectBtn = findViewById(R.id.rejectbtn);
        finishBtn = findViewById(R.id.finishbtn);

        handleStaff();

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAcc();
            }
        });
        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleReject();
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFinish();
            }
        });


    }
    protected void handleStaff() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF, MODE_PRIVATE);
        String uid = sharedPreferences.getString("user_id", "");
        Log.d("CEKKKK", uid);
        mApiService.staffCheck(MainActivity.selectedEventTemp.getEvent_id(), Integer.parseInt(uid)).enqueue(new Callback<List<Staff>>(){
            @Override
            public void onResponse(Call<List<Staff>> call, Response<List<Staff>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Staff> res = response.body();
                if (!res.isEmpty()) {
                    EventDetailActivity.staffTemp = res.get(0);
                    if(!usingTernaryOperator(EventDetailActivity.staffTemp.getP_acara_id())) {
                        acceptBtn.setVisibility(View.GONE);
                        rejectBtn.setVisibility(View.GONE);
                        finishBtn.setVisibility(View.GONE);
                    } else  {
                        acceptBtn.setVisibility(View.VISIBLE);
                        rejectBtn.setVisibility(View.VISIBLE);
                        finishBtn.setVisibility(View.VISIBLE);
                    }
                }

            }
            @Override
            public void onFailure(Call<List<Staff>> call, Throwable t) {
                Log.d("DEBUG_DATASTAFF", t.getMessage());
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void handleAcc() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF, MODE_PRIVATE);
        String uid = sharedPreferences.getString("user_id", "");
        Log.d("CEKKKK", uid);
        mApiService.takeRequest(Integer.parseInt(uid), EventDetailActivity.selectedRequestTemp.getRequest_id()).enqueue(new Callback<BaseResponse<Request>>(){
            @Override
            public void onResponse(Call<BaseResponse<Request>> call, Response<BaseResponse<Request>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(mContext, "Success take request",
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), RequestDetailActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
            @Override
            public void onFailure(Call<BaseResponse<Request>> call, Throwable t) {
                Log.d("DEBUG_DATA", t.getMessage());
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void handleFinish() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF, MODE_PRIVATE);
        String uid = sharedPreferences.getString("user_id", "");
        Log.d("CEKKKK", uid);
        mApiService.finishRequest(EventDetailActivity.selectedRequestTemp.getRequest_id()).enqueue(new Callback<BaseResponse<Request>>(){
            @Override
            public void onResponse(Call<BaseResponse<Request>> call, Response<BaseResponse<Request>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(mContext, "Success finish request",
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), RequestDetailActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
            @Override
            public void onFailure(Call<BaseResponse<Request>> call, Throwable t) {
                Log.d("DEBUG_DATA", t.getMessage());
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void handleReject() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF, MODE_PRIVATE);
        String uid = sharedPreferences.getString("user_id", "");
        Log.d("CEKKKK", uid);
        mApiService.rejectRequest(EventDetailActivity.selectedRequestTemp.getRequest_id()).enqueue(new Callback<BaseResponse<Request>>(){
            @Override
            public void onResponse(Call<BaseResponse<Request>> call, Response<BaseResponse<Request>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(mContext, "Success reject request",
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), RequestDetailActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
            @Override
            public void onFailure(Call<BaseResponse<Request>> call, Throwable t) {
                Log.d("DEBUG_DATA", t.getMessage());
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static boolean usingTernaryOperator(Integer num) {
        return 0 == (num == null ? 0 : num);
    }
    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    private void viewToast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
}