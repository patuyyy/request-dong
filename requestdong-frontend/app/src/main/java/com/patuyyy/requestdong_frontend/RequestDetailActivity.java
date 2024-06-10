package com.patuyyy.requestdong_frontend;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


public class RequestDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){

        }
    }
}