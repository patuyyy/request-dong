package com.patuyyy.requestdong_frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.patuyyy.requestdong_frontend.model.BaseResponse;
import com.patuyyy.requestdong_frontend.model.Event;
import com.patuyyy.requestdong_frontend.request.BaseApiService;
import com.patuyyy.requestdong_frontend.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private int currentPage = 0;
    private int pageSize = 4;
    private Button[] btns;
    private int listSize;
    private int noOfPages;

    public static List<Event> listEvent = new ArrayList<>();
    private Button prevButton = null;
    private Button nextButton = null;
    private ListView eventListView = null;
    private HorizontalScrollView pageScroll = null;
    private BaseApiService mApiService;
    private Context mContext;
    private String selectedFilter = "all";
    private String currentTextSearch = "";
    private androidx.appcompat.widget.SearchView searchView;
    public static Event selectedEventTemp;
    public static int position;
    ArrayList<Event> filteredEvent = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mApiService = UtilsApi.getApiService();

        handleEventList();
        initSearchWidgets();
        prevButton = findViewById(R.id.prev_page);
        nextButton = findViewById(R.id.next_page);
        pageScroll = findViewById(R.id.page_number_scroll);
        eventListView = findViewById(R.id.listbus);

        prevButton.setOnClickListener(v -> {
            currentPage = currentPage != 0? currentPage-1 : 0;
            goToPage(currentPage);
        });
        nextButton.setOnClickListener(v -> {
            currentPage = currentPage != noOfPages -1? currentPage+1 : currentPage;
            goToPage(currentPage);
        });

    }
    protected void handleEventList() {
        mApiService.getAllEvent().enqueue(new Callback<List<Event>>(){
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Event> res = response.body();
                listEvent = res;
                listSize = listEvent.size();
                paginationFooter();
                goToPage(currentPage);
                eventListView.setClickable(true);
                eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedEventTemp = res.get((currentPage) * pageSize + i);
                        position = i;
                        moveActivity(getApplicationContext(), EventDetailActivity.class);
                    }
                });

            }
            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.d("DEBUG_DATA", t.getMessage());
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.account_btn){
            moveActivity(this, AboutMeActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initSearchWidgets() {
        searchView = (androidx.appcompat.widget.SearchView) findViewById(R.id.searchbar);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentTextSearch = newText;
                filteredEvent = new ArrayList<Event>();
                for(Event b : listEvent) {
                    if(b.getName().toLowerCase().contains(newText.toLowerCase())) {
                        if(selectedFilter.equals("all")) {
                            filteredEvent.add(b);
                        }
                        else {
                            if(b.getName().toLowerCase().contains(selectedFilter)) {
                                filteredEvent.add(b);
                            }
                        }
                    }
                }
                EventArrayAdapter paginatedAdapter = new EventArrayAdapter(mContext, filteredEvent);
                eventListView = findViewById(R.id.listbus);
                eventListView.setAdapter(paginatedAdapter);
                return false;
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
                viewPaginatedList(listEvent, currentPage);
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

    private void viewPaginatedList(List<Event> listBus, int page) {
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, listBus.size());
        List<Event> paginatedList = listBus.subList(startIndex, endIndex);

        EventArrayAdapter paginatedAdapter = new EventArrayAdapter(this, paginatedList);
        eventListView = findViewById(R.id.listbus);
        eventListView.setAdapter(paginatedAdapter);
    }
    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    private void viewToast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }


}