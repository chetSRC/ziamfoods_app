package com.revenue_express.ziamfoods;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.SortedList;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.revenue_express.ziamfoods.adapter.ReviewsAdapter;
import com.revenue_express.ziamfoods.dao.ReviewsListItemDao;
import com.revenue_express.ziamfoods.manager.HttpManager;
import com.revenue_express.ziamfoods.manager.ReviewsListManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsActivity extends AppCompatActivity {
    ListView LV_Review ;
    ReviewsAdapter listAdepter;
    ReviewsListItemDao dao;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LV_Review = (ListView)findViewById(R.id.LV_Review);
        listAdepter = new ReviewsAdapter();
        LV_Review.setAdapter((ListAdapter) listAdepter);
        imgBack =(ImageView)findViewById(R.id.imgBack);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);

        reloadData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });
        LV_Review.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void reloadData() {
        Call<ReviewsListItemDao> call = HttpManager.getInstance().getService().loadReviewsList();
        call.enqueue(new Callback<ReviewsListItemDao>() {
            @Override
            public void onResponse(Call<ReviewsListItemDao> call,
                                   Response<ReviewsListItemDao> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()){
                    dao = response.body();
                    ReviewsListManager.getInstance().setDao(dao);
                    listAdepter.notifyDataSetChanged();
//                    Toast.makeText(Contextor.getInstance().getContext(),
//                    Toast.makeText(getContext(),
//                    Toast.makeText(getActivity(),
//                            dao.getData().get(0).getName(),
//
//                            Toast.LENGTH_LONG)
//                            .show();
                } else {
                    try {
//                        Toast.makeText(Contextor.getInstance().getContext(),
//                        Toast.makeText(getContext(),
                        Toast.makeText(getApplicationContext(),
                                response.errorBody().string(),
                                Toast.LENGTH_LONG)
                                .show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReviewsListItemDao> call,
                                  Throwable t) {
//                Toast.makeText(Contextor.getInstance().getContext(),
//                Toast.makeText(getContext(),
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(),
                        t.toString(),
                        Toast.LENGTH_LONG)
                        .show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_app, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
//                Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainAppActivity.class);
                startActivity(intent);
                return true;
//            case R.id.item2:
//                Toast.makeText(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG).show();
//                return true;
//            case R.id.item3:
//                Toast.makeText(getApplicationContext(),"Item 3 Selected",Toast.LENGTH_LONG).show();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
