package com.patuyyy.requestdong_frontend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.patuyyy.requestdong_frontend.model.Event;
import com.patuyyy.requestdong_frontend.model.Request;
import com.patuyyy.requestdong_frontend.model.User;
import com.patuyyy.requestdong_frontend.request.BaseApiService;
import com.patuyyy.requestdong_frontend.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailActivity extends AppCompatActivity {
    private int currentPage = 0;
    private int pageSize = 4;
    private Button[] btns;
    private int listSize;
    private int noOfPages;
    private HorizontalScrollView pageScroll = null;
    private ListView requestListView = null;
    public static List<Request> listRequest = new ArrayList<>();
    public static List<User> listUser = new ArrayList<>();
    public static Request selectedRequestTemp;
    private Button prevButton = null;
    private Button nextButton = null;
    private Button acarabtn, opsbtn = null;
    public static int position;
    private TextView name, datee, description, staff;

    private BaseApiService mApiService;
    private Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){

        }

        mContext = this;
        mApiService = UtilsApi.getApiService();

        handleRequestList();
        prevButton = findViewById(R.id.prev_page);
        nextButton = findViewById(R.id.next_page);
        pageScroll = findViewById(R.id.page_number_scroll);
        requestListView = findViewById(R.id.listrequest);

        acarabtn = findViewById(R.id.acarabtn);
        opsbtn = findViewById(R.id.operasionalbtn);

        acarabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAcara();
            }
        });

        opsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOps();
            }
        });


        name = findViewById(R.id.eventname);
        datee = findViewById(R.id.eventdate);
        description = findViewById(R.id.description);
        staff = findViewById(R.id.stafftxt);

        name.setText(MainActivity.selectedEventTemp.getName());
        datee.setText(MainActivity.selectedEventTemp.getTime().toString());
        description.setText(MainActivity.selectedEventTemp.getDescription());

        prevButton.setOnClickListener(v -> {
            currentPage = currentPage != 0? currentPage-1 : 0;
            goToPage(currentPage);
        });
        nextButton.setOnClickListener(v -> {
            currentPage = currentPage != noOfPages -1? currentPage+1 : currentPage;
            goToPage(currentPage);
        });

        handleRegistered();


    }

    protected void handleAcara() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF, MODE_PRIVATE);
        String uid = sharedPreferences.getString("user_id", "");
        Log.d("CEKKKK", uid);
        mApiService.addToAcara(MainActivity.selectedEventTemp.getEvent_id(), Integer.parseInt(uid)).enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(mContext, "Success add to event as staff acara",
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), EventDetailActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();


            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("DEBUG_DATA", t.getMessage());
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void handleOps() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF, MODE_PRIVATE);
        String uid = sharedPreferences.getString("user_id", "");
        Log.d("CEKKKK", uid);
        mApiService.addToOperasional(MainActivity.selectedEventTemp.getEvent_id(), Integer.parseInt(uid)).enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(mContext, "Success add to event as staff operasionalh",
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), EventDetailActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();


            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("DEBUG_DATA", t.getMessage());
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void handleRegistered() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF, MODE_PRIVATE);
        String uid = sharedPreferences.getString("user_id", "");
        Log.d("CEKKKK", uid);
        mApiService.checkifregistered(MainActivity.selectedEventTemp.getEvent_id(), Integer.parseInt(uid)).enqueue(new Callback<List<User>>(){
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<User> res = response.body();
                listUser = res;

                if (res == null || res.isEmpty()) {
                    acarabtn.setVisibility(View.VISIBLE);
                    opsbtn.setVisibility(View.VISIBLE);
                }


            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("DEBUG_DATA", t.getMessage());
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void handleRequestList() {
        mApiService.getRequestByEvent(MainActivity.selectedEventTemp.getEvent_id()).enqueue(new Callback<List<Request>>(){
            @Override
            public void onResponse(Call<List<Request>> call, Response<List<Request>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Request> res = response.body();
                listRequest = res;
                listSize = listRequest.size();
                paginationFooter();
                goToPage(currentPage);
                requestListView.setClickable(true);
                requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedRequestTemp = res.get((currentPage) * pageSize + i);
                        position = i;
                        moveActivity(getApplicationContext(), EventDetailActivity.class);
                    }
                });

            }
            @Override
            public void onFailure(Call<List<Request>> call, Throwable t) {
                Log.d("DEBUG_DATA", t.getMessage());
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void paginationFooter() {
        int val = listSize % pageSize;
        val = val == 0 ? 0:1;
        noOfPages = listSize / pageSize + val;
        LinearLayout ll = findViewById(R.id.btn_layout);
        btns = new Button[noOfPages];
        if (noOfPages <= 6) {
            ((FrameLayout.LayoutParams) ll.getLayoutParams()).gravity = Gravity.CENTER;
        }
        for (int i = 0; i < noOfPages; i++) {
            btns[i]=new Button(this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText(""+(i+1));
// ganti dengan warna yang kalian mau
            btns[i].setTextColor(getResources().getColor(R.color.black));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100,
                    100);
            ll.addView(btns[i], lp);
            final int j = i;
            btns[j].setOnClickListener(v -> {
                currentPage = j;
                goToPage(j);
            });
        }
    }
    private void goToPage(int index) {
        for (int i = 0; i< noOfPages; i++) {
            if (i == index) {
                btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.oval));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
                scrollToItem(btns[index]);
                viewPaginatedList(listRequest, currentPage);
            } else {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    private void scrollToItem(Button item) {
        int scrollX = item.getLeft() - (pageScroll.getWidth() - item.getWidth()) /
                2;
        pageScroll.smoothScrollTo(scrollX, 0);
    }

    private void viewPaginatedList(List<Request> listBus, int page) {
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, listBus.size());
        List<Request> paginatedList = listBus.subList(startIndex, endIndex);

        RequestArrayAdapter paginatedAdapter = new RequestArrayAdapter(this, paginatedList);
        if (requestListView == null) {
            requestListView = findViewById(R.id.listrequest);
        }
        if (requestListView != null) {
            requestListView.setAdapter(paginatedAdapter);
        } else {
            Log.e("ERROR", "requestListView is null");
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