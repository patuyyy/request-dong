package com.patuyyy.requestdong_frontend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.patuyyy.requestdong_frontend.request.BaseApiService;
import com.patuyyy.requestdong_frontend.request.UtilsApi;

public class AboutMeActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    Button logoutBtn = null;
    TextView initial, username, name;
    public static final String SHARED_PREF = "SharedPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){

        }

        SharedPreferences sp = getApplicationContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        initial = findViewById(R.id.initial);
        username = findViewById(R.id.usernametxt);
        logoutBtn = findViewById(R.id.logoutButton);
        name = findViewById(R.id.nametxt);

        String username1 = sp.getString("username", "");
        String name1 = sp.getString("name", "");
        char initial1 = sp.getString("name", "").charAt(0);

        initial.setText("" + initial1);
        username.setText(username1);
        name.setText(name1);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("key", "");
                editor.apply();

                Intent i = new Intent(getApplicationContext(), LandingActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
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