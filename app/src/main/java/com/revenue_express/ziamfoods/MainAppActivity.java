package com.revenue_express.ziamfoods;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.revenue_express.ziamfoods.adapter.ReviewsAdapter;
import com.revenue_express.ziamfoods.dao.ReviewsListItemDao;
import com.revenue_express.ziamfoods.manager.HttpManager;
import com.revenue_express.ziamfoods.manager.ReviewsListManager;
import com.revenue_express.ziamfoods.view.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainAppActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView LV_Review ;
    ReviewsAdapter listAdepter;
    SwipeRefreshLayout swipeRefreshLayout;
    ReviewsListItemDao dao;
    String bssh_title,bsrh_desc,bsrh_score,memh_firstname,bsrh_title;
    ArrayList<String> actorsList = new ArrayList<String>();

    ViewFlipper simpleViewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        LV_Review = (ListView)findViewById(R.id.LV_Review);
        listAdepter = new ReviewsAdapter();
        LV_Review.setAdapter((ListAdapter) listAdepter);
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

        // handler to set duration and to upate animation
        final Handler mHandler = new Handler();

        // Create runnable for posting
        final Runnable mUpdateResults = new Runnable() {
            public void run() {
                AnimateandSlideShow();
            }
        };

        int delay = 500;
        int period = 4000;

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                mHandler.post(mUpdateResults);
            }
        }, delay, period);

        // get The references of ViewFlipper
        simpleViewFlipper = (ViewFlipper) findViewById(R.id.simpleViewFlipper); // get the reference of ViewFlipper

        final String URL = "http://www.ziamthai.com/index.php/shopInfo/index/1";
        final String URL_Review = "http://www.ziamthai.com/index.php/Review/getReviewApp";

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient okHttpClient = new OkHttpClient();

                Request.Builder builder = new Request.Builder();

                Request request = builder.url(URL).build();
                Request request_review = builder.url(URL_Review).build();

                try {
                    okhttp3.Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string();
                    } else {
                        return "Not Success - code : " + response.code();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error - " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                try {

                    JSONObject jsnobject = new JSONObject(string);
                    JSONArray jArray = jsnobject.getJSONArray("data");

//                    slide image top
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject object = jArray.getJSONObject(i);

                        String image_slide = String.valueOf(object.getString("image"));
                        //  Actors actor = new Actors();
                        actorsList.add(image_slide);
                    }
                    setFlipperImage(actorsList);

//                  slide image category
                    for (int i = 0; i < jArray.length(); i++) {
                        ImageView imgView = new ImageView(getApplicationContext()); //create imageview dynamically
                        JSONObject actor = jArray.getJSONObject(i);
                        String bssh_imghead = String.valueOf(actor.getString("image"));
                        Picasso.with(getApplicationContext()).load("http://www.ziamthai.com/admin/"+bssh_imghead).into(imgView);
                        TextView textView = new TextView(getApplicationContext());//create textview dynamically
                        bssh_title = String.valueOf(actor.getString("name"));
                        textView.setText(bssh_title);

                        LinearLayout rl = new LinearLayout(getApplicationContext());
                        LinearLayout.LayoutParams lp;
                        lp = new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.WRAP_CONTENT);
                        LinearLayout.LayoutParams lp1;
                        lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        rl.setLayoutParams(lp1);
                        imgView.setLayoutParams(lp);
                        textView.setLayoutParams(lp);

                        rl.addView(imgView);//add imageview to relativelayout
                        LinearLayout layout =(LinearLayout) findViewById(R.id.ll_all);

                        layout.addView(rl);
                        rl.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(),"Show ID Shop"+bssh_title,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();


//      Get AsyncTask Review Url
//        new AsyncTask<Void, Void, String>() {
//
//            @Override
//            protected String doInBackground(Void... voids) {
//                OkHttpClient okHttpClient = new OkHttpClient();
//
//                Request.Builder builder = new Request.Builder();
//
//                Request request_review = builder.url(URL_Review).build();
//
//                try {
//                    okhttp3.Response response = okHttpClient.newCall(request_review).execute();
//                    if (response.isSuccessful()) {
//                        return response.body().string();
//                    } else {
//                        return "Not Success - code : " + response.code();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return "Error - " + e.getMessage();
//                }
//            }
//
//            @Override
//            protected void onPostExecute(String string) {
//                super.onPostExecute(string);
//                try {
//
//                    JSONObject jsnobject = new JSONObject(string);
//                    JSONArray jArray = jsnobject.getJSONArray("data");
//
////                  Data review
//                    for (int i = 0; i < jArray.length(); i++) {
//                        JSONObject actor = jArray.getJSONObject(i);
//
//
//                        ImageView imgView = new ImageView(getApplicationContext()); //create imageview dynamically
//                        Picasso.with(getApplicationContext()).load(R.drawable.banner1).into(imgView);
//
//                        TextView textView = new TextView(getApplicationContext());//create textview dynamically
//                        bsrh_title = String.valueOf(actor.getString("bsrh_title"));
//                        textView.setText(bsrh_title);
//
//                        bsrh_desc = String.valueOf(actor.getString("bsrh_desc"));
//                        textView.setText(bsrh_desc);
//
//                        bsrh_score = String.valueOf(actor.getString("bsrh_score"));
//                        textView.setText(bsrh_score);
//
//                        memh_firstname = String.valueOf(actor.getString("memh_firstname"));
//                        textView.setText(memh_firstname);
//
//                        RelativeLayout rl = new RelativeLayout(getApplicationContext());
//                        RelativeLayout.LayoutParams lp;
//                        lp = new RelativeLayout.LayoutParams(400, RelativeLayout.LayoutParams.WRAP_CONTENT);
//
//                        RelativeLayout.LayoutParams lp1;
//                        lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//
//                        rl.setLayoutParams(lp1);
//                        imgView.setLayoutParams(lp);
//                        textView.setLayoutParams(lp);
//
//                        rl.addView(imgView);//add imageview to relativelayout
//                        rl.addView(textView);//add textView to relativelayout
//                        RelativeLayout layout =(RelativeLayout) findViewById(R.id.ll_review);
//
//                        layout.addView(rl);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }.execute();

    }

    private void setFlipperImage(ArrayList<String> actorsList) {

        for(int i=0;i<actorsList.size();i++){
            Log.i("Set Filpper Called", actorsList.get(i).toString()+"");
            ImageView image = new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load("http://www.ziamthai.com/admin/"+actorsList.get(i).toString()).into(image);
            simpleViewFlipper.addView(image);
        }
    }

    // method to show slide show
    private void AnimateandSlideShow() {
        simpleViewFlipper.showNext();
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent intent = new Intent(getApplicationContext(), ReviewsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(getApplicationContext(), DealActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(getApplicationContext(), NearByActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
