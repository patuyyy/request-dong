package com.patuyyy.requestdong_frontend;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.patuyyy.requestdong_frontend.request.BaseApiService;
import com.patuyyy.requestdong_frontend.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandingActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private Button loginBtn = null;
    private Button registerBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){

        }

        mContext = this;
        mApiService = UtilsApi.getApiService();

        loginBtn = findViewById(R.id.loginbutton);
        registerBtn = findViewById(R.id.registerbutton);

        checkAuth();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveActivity(getApplicationContext(), LoginActivity.class);

            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveActivity(getApplicationContext(), RegisterActivity.class);
            }
        });


    }
    private void checkAuth() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF, MODE_PRIVATE);
        String check = sharedPreferences.getString("name", "");
        if (check.equals("true")) {
            moveActivity(getApplicationContext(), MainActivity.class);
            Toast.makeText(mContext, "Login Successful", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    private void viewToast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
}