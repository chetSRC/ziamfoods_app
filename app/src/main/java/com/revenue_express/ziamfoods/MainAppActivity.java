package com.revenue_express.ziamfoods;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Button;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainAppActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    ListView LV_Review;
    ReviewsAdapter listAdepter;
    SwipeRefreshLayout swipeRefreshLayout;
    ReviewsListItemDao dao;
    String bssh_title, bsrh_desc, bsrh_score, memh_firstname, bsrh_title;
    ArrayList<String> actorsList = new ArrayList<String>();

    ViewFlipper simpleViewFlipper;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    String lat;
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;

    Button btnNearBy,btndeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
                        Picasso.with(getApplicationContext()).load("http://www.ziamthai.com/admin/" + bssh_imghead).into(imgView);
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
                        LinearLayout layout = (LinearLayout) findViewById(R.id.ll_all);

                        layout.addView(rl);
                        rl.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "Show ID Shop" + bssh_title,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                        rl.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "Show ID Shop" + bssh_title,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();


        //// get location ////
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        btnNearBy = (Button)findViewById(R.id.btnNearBy);
        btnNearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NearByActivity.class);
                intent.putExtra("POS",latitude+","+longitude);
                startActivity(intent);
            }
        });
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
        getMenuInflater().inflate(R.menu.main_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
            intent.putExtra("POS",latitude+","+longitude);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /// get location ///

    @Override
    public void onLocationChanged(Location location) {
//        txtLat = (TextView) findViewById(R.id.textview1);
//        txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());


        Toast.makeText(MainAppActivity.this, "Latitude:" +  new DecimalFormat("0.00000").format(location.getLatitude()) + ", Longitude:" + new DecimalFormat("0.00000").format(location.getLongitude()), Toast.LENGTH_SHORT).show();
//        new DecimalFormat("0.00").format(location.getLongitude());
        latitude = new DecimalFormat("0.00000").format(location.getLatitude());
        longitude = new DecimalFormat("0.00000").format(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }
}
