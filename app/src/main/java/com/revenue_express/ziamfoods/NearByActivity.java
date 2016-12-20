package com.revenue_express.ziamfoods;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.revenue_express.ziamfoods.adapter.NearByAdapter;
import com.revenue_express.ziamfoods.adapter.ReviewsAdapter;
import com.revenue_express.ziamfoods.dao.NearByDao;
import com.revenue_express.ziamfoods.dao.NearByDataDao;
import com.revenue_express.ziamfoods.dao.ReviewsDao;
import com.revenue_express.ziamfoods.dao.ReviewsDataDao;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NearByActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    ImageView imgBack;
    private ListView LV_NearBy;
    private NearByAdapter mAdapter;

    private final String serverUrl= "http://www2.ziamthai.com/index.php/api/foods_store/finder/?q=&loc=&pos=&page=2";
    String API_Key = "sd23g125sdf1gc10b3yhik58l4o4";
    String pos ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LV_NearBy = (ListView)findViewById(R.id.LV_NearBy);
        imgBack =(ImageView)findViewById(R.id.imgBack);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);

//        reloadData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncDataClass asyncRequestObject = new AsyncDataClass();
                asyncRequestObject.execute(serverUrl, API_Key,pos);
            }
        });
        LV_NearBy.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        pos = getIntent().getStringExtra("POS");


        // request authentication with remote server4
        AsyncDataClass asyncRequestObject = new AsyncDataClass();
        asyncRequestObject.execute(serverUrl, API_Key,pos);

        LV_NearBy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    //-----Login user general-----//
    public class AsyncDataClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(params[0]);
            String jsonResult = "";

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//                System.out.println("Email Value: " +  params[1]);
//                nameValuePairs.add(new BasicNameValuePair("m_user", params[1]));
//                nameValuePairs.add(new BasicNameValuePair("m_pass", params[2]));
                nameValuePairs.add(new BasicNameValuePair("api_key", params[1]));
                nameValuePairs.add(new BasicNameValuePair("pos", params[2]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            // Create Show ProgressBar
        }

        @Override

        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            System.out.println("Resulted Value: " + result);

            showData(result);
        }
    }

    private void showData(String result) {

        Gson gson = new Gson();
        NearByDao blog = gson.fromJson(result,NearByDao.class);

        List<NearByDataDao> data = blog.getData();

        mAdapter = new NearByAdapter(this,data);
        LV_NearBy.setAdapter(mAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    private StringBuilder inputStreamToString(InputStream is) {

        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {

            while ((rLine = br.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return answer;
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
