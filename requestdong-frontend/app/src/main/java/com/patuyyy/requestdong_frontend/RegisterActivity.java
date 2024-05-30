package com.patuyyy.requestdong_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.patuyyy.requestdong_frontend.model.BaseResponse;
import com.patuyyy.requestdong_frontend.model.User;
import com.patuyyy.requestdong_frontend.request.BaseApiService;
import com.patuyyy.requestdong_frontend.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    EditText name, username, password, confirmPassword;
    Button registerBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){

        }
        mContext = this;
        mApiService = UtilsApi.getApiService();
        name = findViewById(R.id.nameedit);
        username = findViewById(R.id.usernameedit);
        password = findViewById(R.id.passwordedit);
        confirmPassword = findViewById(R.id.confirmedit);
        registerBtn = findViewById(R.id.registerbutton);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();
            }
        });

    }
    protected void handleRegister() {
        // handling empty field
        String nameS = name.getText().toString();
        String usernameS = username.getText().toString();
        String passwordS = password.getText().toString();
        String confirmPasswordS = confirmPassword.getText().toString();

        if (nameS.isEmpty() || usernameS.isEmpty() || passwordS.isEmpty() || confirmPasswordS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!passwordS.equals(confirmPasswordS)) {
            Toast.makeText(mContext, "Password does not match",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.register(nameS, usernameS, passwordS).enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(Call<BaseResponse<User>> call,
                                   Response<BaseResponse<User>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<User> res = response.body();
                // if success finish this activity (go to main activity)
                if (res.success) {
                    Intent i = new Intent(mContext, LandingActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                Log.d("DEBUG_DATA", t.getMessage());
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    private void viewToast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
}